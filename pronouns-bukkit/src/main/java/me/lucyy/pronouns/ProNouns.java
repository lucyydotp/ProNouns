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
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.command.*;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.listener.JoinLeaveListener;
import me.lucyy.pronouns.storage.MysqlConnectionException;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.Storage;
import me.lucyy.pronouns.storage.YamlFileStorage;
import me.lucyy.squirtgun.bukkit.BukkitNodeExecutor;
import me.lucyy.squirtgun.bukkit.BukkitPlatform;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.command.node.NodeBuilder;
import me.lucyy.squirtgun.command.node.SubcommandNode;
import me.lucyy.squirtgun.platform.PermissionHolder;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.update.PolymartUpdateChecker;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class ProNouns extends JavaPlugin {

	@Getter
	private PronounHandlerImpl pronounHandler;

	@Getter
	private ConfigHandler configHandler;

	@Override
	public void onEnable() {
		Metrics metrics = new Metrics(this, 9519);
		configHandler = new ConfigHandler(this);

		metrics.addCustomChart(new SimplePie("storage_backend", () -> configHandler.getConnectionType().name()));

		Storage storage = null;
		switch (configHandler.getConnectionType()) {
			case YML:
				storage = new YamlFileStorage(this);
				break;
			case MYSQL:
				try {
					storage = new MysqlFileStorage(this);
					break;
				} catch (MysqlConnectionException e) {
					getPluginLoader().disablePlugin(this);
					return;
				}
		}
		pronounHandler = new PronounHandlerImpl(this, storage);

		for (String set : configHandler.getPredefinedSets()) {
			try {
				pronounHandler.addToIndex(pronounHandler.fromString(set));
			} catch (IllegalArgumentException e) {
				getLogger().warning("'" + set + "' is an invalid set, ignoring");
			}
		}

		this.getServer().getServicesManager().register(PronounHandler.class, pronounHandler, this, ServicePriority.Normal);

		Platform platform = new BukkitPlatform(this);

		final CommandNode<PermissionHolder> rootNode = new SubcommandNode<>("pronouns",
				null,
				true,
				new SetPronounsNode(this),
				new ShowPronounsNode(platform, getPronounHandler()),
				new PreviewNode(this),
				new ClearPronounsNode(pronounHandler),
				new ListPronounsNode(pronounHandler),
				new NodeBuilder<>()
						.name("reload")
						.description("Reloads the plugin.")
						.executes(x -> {
							reloadConfig();
							return x.getFormat().getPrefix()
									.append(x.getFormat().formatMain("Reloaded"));
						}).build()
		);

		// cmd.register(new VersionSubcommand(configHandler, this));

		final TabExecutor executor = new BukkitNodeExecutor(rootNode, getConfigHandler());
		final PluginCommand cmd = getCommand("pronouns");
		Objects.requireNonNull(cmd);
		cmd.setExecutor(executor);
		cmd.setTabCompleter(executor);


		if (getConfigHandler().checkForUpdates()) {
			new PolymartUpdateChecker(platform,
					921,
					configHandler.getPrefix()
							.append(configHandler.formatMain("A new version of ProNouns is available!\nFind it at "))
							.append(configHandler.formatAccent("https://lucyy.me/pronouns", TextDecoration.UNDERLINED)
									.clickEvent(ClickEvent.openUrl("https://lucyy.me/pronouns"))),
					"pronouns.admin");
		} else {
			getLogger().warning("Update checking is disabled. You might be running an old version!");
		}

		getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
	}
}