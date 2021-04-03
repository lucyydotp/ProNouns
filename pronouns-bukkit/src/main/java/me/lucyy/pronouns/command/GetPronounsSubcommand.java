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
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetPronounsSubcommand implements Subcommand {
    private final ProNouns pl;

    public GetPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "show";
    }

    public String getDescription() {
        return "Shows your, or another player's, pronouns.";
    }

    public String getUsage() {
        return "show [username]";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender target, String[] args) {
        Player commandTarget;
        ConfigHandler cfg = pl.getConfigHandler();
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(cfg.getPrefix().append(cfg.formatMain("Please specify a username.")));
            return true;
        }

        if (args.length > 0) {
            commandTarget = Bukkit.getPlayer(args[0]);
        } else commandTarget = (Player) sender;

        if (commandTarget == null) {
            sender.sendMessage(cfg.getPrefix()
                    .append(cfg.formatMain("Player '"))
                    .append(cfg.formatAccent(args[0]))
                    .append(cfg.formatMain("' could not be found.")));
            return true;
        }

        sender.sendMessage(cfg.getPrefix()
                .append(cfg.formatMain(commandTarget.getDisplayName() + "'s pronouns are "))
                .append(cfg.formatAccent(
                PronounSet.friendlyPrintSet(
                        pl.getPronounHandler().getUserPronouns(commandTarget.getUniqueId()))
                )
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toUpperCase().startsWith(args[1].toUpperCase())) names.add(player.getName());
        }
        return names;
    }
}
