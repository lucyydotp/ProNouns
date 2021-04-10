package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;
import me.lucyy.pronouns.api.PronounHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PronounsDiscord extends JavaPlugin {

	private PronounHandler handler;

	@Override
	public void onEnable() {
		RegisteredServiceProvider<PronounHandler> rsp = getServer().getServicesManager().getRegistration(PronounHandler.class);
		assert rsp != null;
		handler = rsp.getProvider();

		new DiscordHandler(this, (DiscordSRV) Bukkit.getPluginManager().getPlugin("DiscordSRV"));
	}


	public PronounHandler getHandler() {
		return handler;
	}
}
