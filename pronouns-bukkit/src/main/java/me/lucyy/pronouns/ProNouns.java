package me.lucyy.pronouns;

import lombok.Getter;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.command.PronounsCommand;
import me.lucyy.pronouns.command.PronounsTabCompleter;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.listener.JoinLeaveListener;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.YamlFileStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class ProNouns extends JavaPlugin implements Listener {

    @Getter
    private PronounHandlerImpl pronounHandler;

    @Getter
    private ConfigHandler configHandler;
    private boolean updateAvailable = false;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 9519);
        configHandler = new ConfigHandler(this);
        metrics.addCustomChart(new Metrics.SimplePie("storage_backend", () -> configHandler.getConnectionType().name()));

        switch (configHandler.getConnectionType()) {
            case YML:
                pronounHandler = new PronounHandlerImpl(new YamlFileStorage(this));
                break;
            case MYSQL:
                pronounHandler = new PronounHandlerImpl(new MysqlFileStorage(this));
                break;
        }

        this.getServer().getServicesManager().register(PronounHandler.class, pronounHandler, this, ServicePriority.Normal);

        PronounsCommand cmd = new PronounsCommand(this);
        getCommand("pronouns").setExecutor(cmd);
        getCommand("pronouns").setTabCompleter(new PronounsTabCompleter(cmd));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PronounsPapiExpansion(this).register();

        if (configHandler.checkForUpdates()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection con = (HttpURLConnection)new URL("https://api.spigotmc.org/legacy/update.php?resource=86199").openConnection();
                        if (con.getResponseCode() != 200) throw new Exception();

                        String text = new BufferedReader(
                                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
                        ).lines().collect(Collectors.joining("\n"));

                        if (!text.equals(getDescription().getVersion())) {
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
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }
}