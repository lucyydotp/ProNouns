package me.lucyy.pronouns.command.admin;

import me.lucyy.pronouns.command.PronounsCommand;
import me.lucyy.pronouns.command.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.lucyy.pronouns.config.ConfigHandler;

import java.util.Arrays;

public class SudoSubcommand implements Subcommand {
	
	private final PronounsCommand command;

	public SudoSubcommand(PronounsCommand command) {
		this.command = command;
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
	public boolean execute(@NotNull final CommandSender sender, @NotNull final CommandSender target, @NotNull final String[] args) {
		if (args.length < 2) return false;
		if (args[1].equals("admin")) {
			sender.sendMessage(command.getPlugin().getConfigHandler().getPrefix() + "no recursion pls kthxbai");
			return true;
		}
		Player cmdTarget = Bukkit.getPlayer(args[0]);

		if (cmdTarget == null) {
			sender.sendMessage(command.getPlugin().getConfigHandler().getPrefix() + "Player '" + args[0] + "' couldn't be found");
			return true;
		}

		sender.sendMessage(command.getPlugin().getConfigHandler().getPrefix() + "Running command as " + cmdTarget.getDisplayName());
		command.onCommand(sender, cmdTarget, Arrays.copyOfRange(args, 1, args.length));
		return true;
	}
}
