package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;
import me.lucyy.pronouns.config.ConfigHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JdaCommandListener extends ListenerAdapter {
	private final ConfigHandler cfg;
	private final DiscordSRV discord;

	public JdaCommandListener(ConfigHandler cfg, DiscordSRV discord) {
		this.cfg = cfg;
		this.discord = discord;
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		super.onMessageReceived(event);
		if (!event.getMessage().getContentStripped().startsWith(cfg.getDiscordCmd())) return;
		UUID uuid = discord.getAccountLinkManager().getUuid(event.getAuthor().getId());
		if (uuid == null) {
			event.getMessage()
					.reply("You need to link your Minecraft account to do that. Use /discord link ingame.")
					.submit();
			return;
		}

		// TODO finish this
	}
}
