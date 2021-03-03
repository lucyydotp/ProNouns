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
import me.lucyy.pronouns.command.PronounsCommand;
import me.lucyy.pronouns.command.PronounsTabCompleter;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.listener.JoinLeaveListener;
import me.lucyy.pronouns.storage.MysqlConnectionException;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.YamlFileStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public final class ProNouns extends JavaPlugin implements Listener {

	@Getter
	private PronounHandlerImpl pronounHandler;

	@Getter
	private ConfigHandler configHandler;
	private boolean updateAvailable = false;

	@Override
	public void onEnable() {
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

		PronounsCommand cmd = new PronounsCommand(this);
		//noinspection ConstantConditions
		getCommand("pronouns").setExecutor(cmd);
		getCommand("pronouns").setTabCompleter(new PronounsTabCompleter(cmd));

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
			new PronounsPapiExpansion(this).register();

		if (configHandler.checkForUpdates()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					try {
						getLogger().info("Checking for updates...");

						HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=86199").openConnection();
						if (con.getResponseCode() != 200) throw new Exception();

						String text = new BufferedReader(
								new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
						).lines().collect(Collectors.joining("\n"));

						if (!text.equals(getDescription().getVersion())) {
							updateAvailable = true;
							getLogger().warning("A new version of ProNouns is available! Find it at https://lucyy.me/pronouns");
							this.cancel();
						} else getLogger().info("No update available.");

					} catch (Exception ignored) {
						getLogger().warning("Unable to check for ProNouns updates!");
					}
				}
			}.runTaskTimerAsynchronously(this, 0, 216000); // every 3 hours

			Bukkit.getPluginManager().registerEvents(this, this);
		} else {
			getLogger().warning("Update checking is disabled. You might be running an old version!");
		}
		getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
	}

	public boolean isUpdateAvailable() {
		return updateAvailable;
	}
}