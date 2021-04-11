package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;
import me.lucyy.common.command.Command;
import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.command.HelpSubcommand;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.discord.command.SyncAllSubcommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PronounsDiscord extends JavaPlugin {

	private PronounHandler handler;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		getConfig().addDefault("command", "?pronouns");
		saveConfig();

		// bukkit won't load it if pronouns isnt present
		ProNouns pronouns = Objects.requireNonNull((ProNouns)Bukkit.getPluginManager().getPlugin("ProNouns"));
		if (!pronouns.isEnabled()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		RegisteredServiceProvider<PronounHandler> rsp = getServer().getServicesManager().getRegistration(PronounHandler.class);
		assert rsp != null;
		handler = rsp.getProvider();
		DiscordSRV plugin = DiscordSRV.getPlugin();
		DiscordRoleManager roleManager = new DiscordRoleManager(this, plugin);

		DiscordSRV.api.subscribe(new DiscordCommandListener(this, plugin, roleManager));
		getServer().getPluginManager().registerEvents(roleManager, this);

		@SuppressWarnings("RedundantCast") // it wont compile without that cast, idk why
		FormatProvider provider = (FormatProvider) pronouns.getConfigHandler();

		Command cmd = new Command(provider);
		HelpSubcommand help = new HelpSubcommand(cmd, provider, this, "pndc");
		cmd.register(help);
		cmd.setDefaultSubcommand(help);

		cmd.register(new SyncAllSubcommand(roleManager, handler, provider));

		//noinspection ConstantConditions - command given in plugin.yml
		getCommand("pronounsdiscord").setExecutor(cmd);
	}

	public PronounHandler getHandler() {
		return handler;
	}
}
