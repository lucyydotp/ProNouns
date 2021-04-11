package me.lucyy.pronouns.discord.command;

import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.command.Subcommand;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.discord.DiscordRoleManager;
import org.bukkit.command.CommandSender;

public class SyncAllSubcommand implements Subcommand {
	private final DiscordRoleManager manager;
	private final PronounHandler handler;
	private final FormatProvider format;

	public SyncAllSubcommand(DiscordRoleManager manager, PronounHandler handler, FormatProvider format) {
		this.manager = manager;
		this.handler = handler;
		this.format = format;
	}

	@Override
	public String getName() {
		return "syncall";
	}

	@Override
	public String getDescription() {
		return "Syncs all users";
	}

	@Override
	public String getUsage() {
		return "/pronounsdiscord syncall";
	}

	@Override
	public String getPermission() {
		return "pronouns.admin";
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender ignored, String[] args) {
		sender.sendMessage(format.getPrefix()
				.append(format.formatMain("Syncing all roles..."))
		);
		handler.getAllUserPronouns().forEach(manager::syncRole);
		sender.sendMessage(format.getPrefix()
				.append(format.formatMain("Roles synced."))
		);
		return true;
	}
}
