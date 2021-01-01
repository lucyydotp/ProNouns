package me.lucyy.pronouns;

import me.lucyy.pronouns.command.PronounsCommand;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.config.ConnectionType;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.YamlFileStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class ProNouns extends JavaPlugin implements Listener {

    private PronounHandler handler;
    private Metrics metrics;
    private boolean updateAvailable = false;
    public PronounHandler getPronounHandler() {
        return handler;
    }

    @Override
    public void onEnable() {
        int pluginId = 9519;
        metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("storage_backend", () -> ConfigHandler.GetConnectionType().name()));
        ConfigHandler.SetPlugin(this);

        switch (ConfigHandler.GetConnectionType()) {
            case YML:
                handler = new PronounHandler(new YamlFileStorage(this));
                break;
            case MYSQL:
                handler = new PronounHandler(new MysqlFileStorage(this));
                break;
        }


        getCommand("pronouns").setExecutor(new PronounsCommand(this));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PronounsPapiExpansion(this).register();

        if (ConfigHandler.CheckForUpdates()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection con = (HttpURLConnection)new URL(ConfigHandler.GetUpdateUrl()).openConnection();
                        if (con.getResponseCode() != 200) throw new Exception();

                        String text = new BufferedReader(
                                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
                        ).lines().collect(Collectors.joining("\n"));

                        JSONObject json = new JSONObject(text);
                        if (!((JSONObject)json.get("latest")).get("version").equals(getDescription().getVersion())) {
                            updateAvailable = true;
                            getLogger().info("A new version of ProNouns is available! Find it at https://lucyy.me/pronouns");
                            this.cancel();
                        }

                    } catch (Exception ignored) {
                        getLogger().warning("Unable to check for ProNouns updates!");
                    }
                }
            }.runTaskTimerAsynchronously(this, 0, 216000); // every 3 hours

            Bukkit.getPluginManager().registerEvents(this, this);
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        if (ConfigHandler.GetConnectionType() == ConnectionType.MYSQL) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    MysqlFileStorage storage = (MysqlFileStorage)handler.getStorage();
                    storage.GetPronouns(e.getPlayer().getUniqueId(), false);
                }
            }.runTaskAsynchronously(this);
        }
        if(updateAvailable && e.getPlayer().hasPermission("pronouns.admin"))
            e.getPlayer().sendMessage(ConfigHandler.GetPrefix() +
                    "A new version of ProNouns is available!\nFind it at "
                    + ConfigHandler.GetAccentColour() + "https://lucyy.me/pronouns");
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        if (ConfigHandler.GetConnectionType() == ConnectionType.MYSQL) {
            MysqlFileStorage storage = (MysqlFileStorage) handler.getStorage();
            storage.onPlayerDisconnect(e.getPlayer().getUniqueId());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}