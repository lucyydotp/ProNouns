/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProNouns.
 *
 * ProNouns is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProNouns is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProNouns.  If not, see <http://www.gnu.org/licenses/>.
 */

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
