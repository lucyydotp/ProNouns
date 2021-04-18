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
import me.lucyy.common.format.Platform;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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

	private void warnAdmins(String player, String content) {
		final ConfigHandler cfg = pl.getConfigHandler();
		Bukkit.broadcast(cfg.getPrefix().append(cfg.formatMain("Player "))
						.append(cfg.formatAccent(player))
						.append(cfg.formatMain(" tried to use prohibited pronoun set "))
						.append(cfg.formatAccent(content)),
				"pronouns.admin");
	}

	private boolean checkInput(String arg, CommandSender sender) {
		final ConfigHandler cfg = pl.getConfigHandler();
		if (!cfg.filterEnabled()) return true;
		if (!sender.hasPermission("pronouns.bypass"))
			for (String pattern : cfg.getFilterPatterns()) {
				if (arg.toLowerCase().matches(".*" + pattern + ".*")) {
					Platform.send(sender, cfg.getPrefix()
							.append(cfg.formatMain("You can't use that set.")));
					warnAdmins(sender.getName(), arg);
					return false;
				}
			}
		return true;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {
		final ConfigHandler cfg = pl.getConfigHandler();
		if (!(target instanceof Player)) {
			Platform.send(sender, cfg.getPrefix().append(cfg.formatMain("This command can only be run by a player.")));
			return true;
		}

		if (args.length < 1) return false;

		for (String arg : args) {
			if (!checkInput(arg, sender)) return true;
		}
		Set<PronounSet> set;
		try {
			 set = pl.getPronounHandler().parseSets(args);
		} catch (IllegalArgumentException e) {
			Platform.send(sender, cfg.getPrefix()
					.append(cfg.formatMain("The pronoun '"))
					.append(cfg.formatAccent(e.getMessage()))
					.append(cfg.formatMain("' is unrecognised.\n"
							+ "To use it, just write it out like it's shown in /pronouns list.")));
			return true;
		}
		pl.getPronounHandler().setUserPronouns(((Player) target).getUniqueId(), set);
		Platform.send(sender, cfg.getPrefix()
				.append(cfg.formatMain("Set pronouns to "))
				.append(cfg.formatAccent(PronounSet.friendlyPrintSet(set))));
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
				if (!pronounsSoFar.contains(set.getSubjective()))
					allPronouns.add(soFarJoined + "/" + set.getSubjective());
			}
			allPronouns.add(soFarJoined);
		}

		return allPronouns;
	}
}
