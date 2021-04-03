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

import me.lucyy.common.command.Command;
import me.lucyy.common.command.Subcommand;
import me.lucyy.pronouns.ProNouns;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SudoSubcommand implements Subcommand {
	
	private final Command command;
	private final ProNouns plugin;

	public SudoSubcommand(Command command, ProNouns plugin) {
		this.command = command;
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "sudo";
	}

	@Override
	public String getDescription() {
		return "ADMIN - run as another user";
	}

	@Override
	public String getUsage() {
		return "/pronouns sudo <user> <subcommand> [args]";
	}

	@Override
	public String getPermission() {
		return "pronouns.admin";
	}

	@Override
	public boolean execute(final CommandSender sender, final CommandSender target, final String[] args) {
		if (args.length < 2) return false;
		if (args[1].equals("admin")) {
			sender.sendMessage( plugin.getConfigHandler().getPrefix().append(
					plugin.getConfigHandler().formatMain("plsno recursion"))
			);
			return true;
		}
		Player cmdTarget = Bukkit.getPlayer(args[0]);

		if (cmdTarget == null) {
			sender.sendMessage(plugin.getConfigHandler().getPrefix()
					.append(plugin.getConfigHandler().formatMain("Player '" + args[0] + "' couldn't be found"))
			);
			return true;
		}

		sender.sendMessage(plugin.getConfigHandler().getPrefix()
				.append(plugin.getConfigHandler().formatMain("Running command as " + cmdTarget.getDisplayName())));
		command.onCommand(sender, cmdTarget, "pronouns", Arrays.copyOfRange(args, 1, args.length));
		return true;
	}
}
