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

package me.lucyy.pronouns.command.admin;

import me.lucyy.common.command.Subcommand;
import me.lucyy.common.format.Platform;
import me.lucyy.pronouns.ProNouns;
import org.bukkit.command.CommandSender;

public class ReloadSubcommand implements Subcommand {

    private final ProNouns pl;

    public ReloadSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "ADMIN - reloads config";
    }

    @Override
    public String getUsage() {
        return "/pronouns reload";
    }

    @Override
    public String getPermission() {
        return "pronouns.admin";
    }

    @Override
    public boolean execute(final CommandSender sender, final CommandSender target, final String[] args) {
        pl.reloadConfig();
		Platform.send(sender, pl.getConfigHandler().getPrefix()
                .append(pl.getConfigHandler().formatMain("Reloaded"))
        );
        return true;
    }
}
