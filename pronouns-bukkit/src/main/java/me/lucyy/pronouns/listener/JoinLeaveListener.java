package me.lucyy.pronouns.listener;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.config.ConnectionType;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinLeaveListener implements Listener {
    private final ProNouns pl;
    public JoinLeaveListener(ProNouns pl) {
        this.pl = pl;
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        if (pl.getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    MysqlFileStorage storage = (MysqlFileStorage) pl.getPronounHandler().getStorage();
                    storage.getPronouns(e.getPlayer().getUniqueId(), false);
                }
            }.runTaskAsynchronously(pl);
        }
        if(pl.isUpdateAvailable() && e.getPlayer().hasPermission("pronouns.admin"))
            e.getPlayer().sendMessage(pl.getConfigHandler().getPrefix() +
                    "A new version of ProNouns is available!\nFind it at "
                    + pl.getConfigHandler().getAccentColour() + "https://lucyy.me/pronouns");
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        if (pl.getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            MysqlFileStorage storage = (MysqlFileStorage) pl.getPronounHandler().getStorage();
            storage.onPlayerDisconnect(e.getPlayer().getUniqueId());
        }
    }
}
