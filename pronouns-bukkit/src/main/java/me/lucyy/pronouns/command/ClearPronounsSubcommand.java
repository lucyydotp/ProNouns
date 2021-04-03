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

package me.lucyy.pronouns.command;

import me.lucyy.common.command.Subcommand;
import me.lucyy.pronouns.ProNouns;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearPronounsSubcommand implements Subcommand {
    private final ProNouns pl;

    public ClearPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "clear";
    }

    public String getDescription() {
        return "Clear your pronouns.";
    }

    public String getUsage() {
        return "clear";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender target, String[] args) {
        if (!(target instanceof Player)) {
            sender.sendMessage(pl.getConfigHandler().getPrefix() + "This command can only be run by a player.");
            return true;
        }


        pl.getPronounHandler().clearUserPronouns(((Player) target).getUniqueId());
        sender.sendMessage(pl.getConfigHandler().getPrefix()
                .append(pl.getConfigHandler().formatMain("Cleared pronouns"))
        );
        return true;
    }
}
