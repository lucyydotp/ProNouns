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

package me.lucyy.pronouns;

import lombok.Getter;
import me.lucyy.common.DependencyChecker;
import me.lucyy.common.command.Command;
import me.lucyy.common.command.HelpSubcommand;
import me.lucyy.common.command.VersionSubcommand;
import me.lucyy.common.update.UpdateChecker;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.command.*;
import me.lucyy.pronouns.command.admin.ReloadSubcommand;
import me.lucyy.pronouns.command.admin.SudoSubcommand;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.listener.JoinLeaveListener;
import me.lucyy.pronouns.storage.MysqlConnectionException;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.YamlFileStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("ConstantConditions")
public final class ProNouns extends JavaPlugin implements Listener {

	@Getter
	private PronounHandlerImpl pronounHandler;

	@Getter
	private ConfigHandler configHandler;

	@Override
	public void onEnable() {
		if (!DependencyChecker.adventurePresent(this)) {
			getPluginLoader().disablePlugin(this);
			return;
		}
		Metrics metrics = new Metrics(this, 9519);
		configHandler = new ConfigHandler(this);
		metrics.addCustomChart(new Metrics.SimplePie("storage_backend", () -> configHandler.getConnectionType().name()));

		switch (configHandler.getConnectionType()) {
			case YML:
				pronounHandler = new PronounHandlerImpl(new YamlFileStorage(this));
				break;
			case MYSQL:
				try {
					pronounHandler = new PronounHandlerImpl(new MysqlFileStorage(this));
					break;
				} catch (MysqlConnectionException e) {
					getPluginLoader().disablePlugin(this);
					return;
				}
		}

		for (String set : configHandler.getPredefinedSets()) {
			try {
				pronounHandler.addToIndex(pronounHandler.fromString(set));
			} catch (IllegalArgumentException e) {
				getLogger().warning("'" + set + "' is an invalid set, ignoring");
			}
		}

		this.getServer().getServicesManager().register(PronounHandler.class, pronounHandler, this, ServicePriority.Normal);

		Command cmd = new Command(configHandler);

		cmd.register(new GetPronounsSubcommand(this));
		cmd.register(new SetPronounsSubcommand(this));
		cmd.register(new ClearPronounsSubcommand(this));
		cmd.register(new ListPronounsSubcommand(this));
		cmd.register(new PreviewSubcommand(this));
		cmd.register(new ReloadSubcommand(this));
		cmd.register(new SudoSubcommand(cmd, this));
		cmd.register(new VersionSubcommand(configHandler, this));

		HelpSubcommand defaultSub = new HelpSubcommand(cmd, configHandler, this, "pronouns");
		cmd.register(defaultSub);
		cmd.setDefaultSubcommand(defaultSub);

		//noinspection ConstantConditions
		getCommand("pronouns").setExecutor(cmd);
		getCommand("pronouns").setTabCompleter(cmd);

		if (getConfigHandler().checkForUpdates()) {
			new UpdateChecker(this,
					"https://api.spigotmc.org/legacy/update.php?resource=86199",
					"A new version of ProNouns is available!\nFind it at https://lucyy.me/pronouns",
					"pronouns.admin"
			);
		} else {
			getLogger().warning("Update checking is disabled. You might be running an old version!");
		}

		getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
	}
}