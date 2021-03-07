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
import me.lucyy.common.format.TextFormatter;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class ListPronounsSubcommand implements Subcommand {
    private final ProNouns pl;
    public ListPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "list";
    }

    public String getDescription() {
        return "Show all predefined pronoun sets.";
    }

    public String getUsage() {
        return "list";
    }

    @Override
    public String getPermission() { return null; }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        final ConfigHandler cfg = pl.getConfigHandler();
        StringBuilder out = new StringBuilder();
        sender.sendMessage(TextFormatter.formatTitle("All Predefined Pronoun Sets:", cfg));
        for (PronounSet set : pl.getPronounHandler().getAllPronouns()) out.append(set.toString()).append("\n");
        sender.sendMessage(cfg.formatMain(out.toString()));
        sender.sendMessage(TextFormatter.formatTitle("*", cfg));
        return true;
    }
}
