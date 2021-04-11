package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import me.lucyy.pronouns.api.set.PronounSet;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class DiscordCommandListener {
	private final PronounsDiscord pl;
	private final DiscordSRV discord;

	public DiscordCommandListener(PronounsDiscord pl, DiscordSRV discord) {
		this.pl = pl;
		this.discord = discord;
	}

	@Subscribe
	@SuppressWarnings("unused")
	public void on(@NotNull DiscordGuildMessagePreProcessEvent event) {
		//noinspection ConstantConditions - default set elsewhere
		if (!event.getMessage().getContentStripped().startsWith(pl.getConfig().getString("command"))) return;
		UUID uuid = discord.getAccountLinkManager().getUuid(event.getAuthor().getId());

		if (uuid == null) {
			event.getMessage().reply("You need to link link your Minecraft account. to use this command." +
					"Use /me.lucyy.pronouns.discord link ingame.").queue();
		}

		String[] args = event.getMessage().getContentStripped().split(" ");
		args = Arrays.copyOfRange(args, 1, args.length);
		Set<PronounSet> set;
		try {
			set = pl.getHandler().parseSets(args);
		} catch (IllegalArgumentException e) {
			event.getMessage().reply("The pronoun '" + e.getMessage() + "' is unrecognised. "
					+ "To use it, just write it out like it's shown in /pronouns list.").queue();
			return;
		}

		pl.getHandler().setUserPronouns(uuid, set);
		event.getMessage().reply("Set pronouns to " + PronounSet.friendlyPrintSet(set) + ".").queue();
		event.setCancelled(true);
	}
}
