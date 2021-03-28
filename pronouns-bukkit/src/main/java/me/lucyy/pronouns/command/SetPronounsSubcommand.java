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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SetPronounsSubcommand implements Subcommand {
    private final ProNouns pl;

    public SetPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "set";
    }

    public String getDescription() {
        return "Set your pronouns.";
    }

    public String getUsage() {
        return "set <pronoun> [pronoun] ...\nExample: /pronouns set she/they";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender target, String[] args) {
        final ConfigHandler cfg = pl.getConfigHandler();
        if (!(target instanceof Player)) {
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain("This command can only be run by a player."));
            return true;
        }

        if (args.length < 1) return false;

        ArrayList<PronounSet> set = new ArrayList<>();
        for (String arg : args) {
            try {
                String[] splitArg = arg.split("/");
                if (splitArg.length == 6) {
                    PronounSet parsed = pl.getPronounHandler().fromString(arg);
                    if (!set.contains(parsed)) set.add(parsed);
                } else {
                    for (String _splitArg : splitArg) {
                        // check for objective
                        boolean cont = true;
                        for (PronounSet _set : set) {
                            if (_set.objective.toUpperCase(Locale.ROOT).equals(_splitArg.toUpperCase())) {
                                cont = false;
                                break;
                            }
                        }
                        if (!cont) continue;
                        PronounSet parsed = pl.getPronounHandler().fromString(_splitArg);
                        if (!set.contains(parsed)) set.add(parsed);
                    }
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(cfg.getPrefix()
                        + cfg.formatMain("The pronoun '")
                        + cfg.formatAccent(e.getMessage())
                        + cfg.formatMain("' is unrecognised.\n"
                        + "To use it, just write it out like it's shown in /pronouns list."));
                return true;
            }
        }
        pl.getPronounHandler().setUserPronouns(((Player) target).getUniqueId(), set);
        sender.sendMessage(cfg.getPrefix() + cfg.formatMain("Set pronouns to ")
                + cfg.formatAccent(PronounSet.friendlyPrintSet(set)));
        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        String arg = args[args.length - 1];
        List<String> allPronouns = new ArrayList<>();

        allPronouns.add("<custom>");

        for (PronounSet set : pl.getPronounHandler().getAllPronouns()) {
            allPronouns.add(set.getName().toLowerCase());
        }

        if (arg.contains("/") && !allPronouns.contains(arg)) {
            List<String> pronounsSoFar = Arrays.asList(arg.split("/"));
            String soFarJoined = String.join("/", pronounsSoFar);
            for (PronounSet set : pl.getPronounHandler().getAllPronouns()) {
                if (!pronounsSoFar.contains(set.subjective))
                    allPronouns.add(soFarJoined + "/" + set.subjective);
            }
            allPronouns.add(soFarJoined);
        }

        return allPronouns;
    }
}
