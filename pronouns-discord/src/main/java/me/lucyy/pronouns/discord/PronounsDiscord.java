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

public final class PronounsDiscord extends JavaPlugin {

	private PronounHandler handler;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		getConfig().addDefault("command", "?pronouns");
		saveConfig();

		RegisteredServiceProvider<PronounHandler> rsp = getServer().getServicesManager().getRegistration(PronounHandler.class);
		assert rsp != null;
		handler = rsp.getProvider();
		DiscordSRV plugin = DiscordSRV.getPlugin();
		DiscordRoleManager roleManager = new DiscordRoleManager(this, plugin);

		DiscordSRV.api.subscribe(new DiscordCommandListener(this, plugin));
		getServer().getPluginManager().registerEvents(roleManager, this);

		//noinspection ConstantConditions - bukkit won't load if pronouns isnt present
		@SuppressWarnings("RedundantCast") // it wont compile without that cast, idk why
		FormatProvider provider = (FormatProvider)
				((ProNouns)Bukkit.getPluginManager().getPlugin("ProNouns")).getConfigHandler();

		Command cmd = new Command(provider);
		HelpSubcommand help = new HelpSubcommand(cmd, provider, this, "/pndc");
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
