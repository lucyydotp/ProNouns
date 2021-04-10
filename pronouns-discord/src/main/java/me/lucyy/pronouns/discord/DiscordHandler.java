package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DiscordHandler {

	public DiscordHandler(PronounsDiscord pl, DiscordSRV discord) {
		DiscordSRV.api.subscribe(new DiscordCommandListener(pl, discord));
		pl.getServer().getPluginManager().registerEvents(new DiscordPronounSetListener(pl, discord), pl);
	}

	public static UUID uuidFromDiscord(String id) {
		return UUID.nameUUIDFromBytes(("Discord:" + id).getBytes(StandardCharsets.UTF_8));
	}
}
