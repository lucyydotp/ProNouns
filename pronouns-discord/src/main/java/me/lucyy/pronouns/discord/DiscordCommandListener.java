package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import me.lucyy.pronouns.api.PronounHandler;
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
		if (!event.getMessage().getContentStripped().startsWith(pl.getConfig().getString("command"))) return;
		UUID uuid = discord.getAccountLinkManager().getUuid(event.getAuthor().getId());
		final boolean isDiscordUuid = uuid == null;
		if (isDiscordUuid) {
			uuid = DiscordHandler.uuidFromDiscord(event.getAuthor().getId());
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
		event.getMessage().reply("Set pronouns to " + PronounSet.friendlyPrintSet(set) + "."
				+ (isDiscordUuid ? "\nThis command works better if you link your Minecraft account. " +
				"Use /me.lucyy.pronouns.discord link ingame." : "")).queue();
		event.setCancelled(true);
	}
}
