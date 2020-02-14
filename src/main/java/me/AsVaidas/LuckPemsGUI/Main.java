/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI;

import java.io.File;
import java.io.IOException;
import me.AsVaidas.LuckPemsGUI.commands.LPGUICommand;
import me.AsVaidas.LuckPemsGUI.events.Events;
import me.AsVaidas.LuckPemsGUI.updatechecker.JoinEvents;
import me.AsVaidas.LuckPemsGUI.updatechecker.UpdateChecker;
import me.AsVaidas.LuckPemsGUI.util.Logger;
import me.AsVaidas.LuckPemsGUI.util.MetricsLite;
import me.AsVaidas.LuckPemsGUI.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.AsVaidas.LuckPemsGUI.groups.EditGroup;
import me.AsVaidas.LuckPemsGUI.groups.GroupsGUI;
import me.AsVaidas.LuckPemsGUI.groups.Parents;
import me.AsVaidas.LuckPemsGUI.groups.Permissions;
import me.AsVaidas.LuckPemsGUI.groups.Prefix;
import me.AsVaidas.LuckPemsGUI.groups.Suffix;
import me.AsVaidas.LuckPemsGUI.tracks.EditTrack;
import me.AsVaidas.LuckPemsGUI.tracks.TracksGUI;
import me.AsVaidas.LuckPemsGUI.users.EditUser;
import me.AsVaidas.LuckPemsGUI.users.UsersGUI;
import me.AsVaidas.LuckPemsGUI.users.Wipe;

public class Main extends JavaPlugin {

	private static Plugin instance;
	private UpdateChecker checker;
	public static Main plugin;

	@Override
	public void onEnable() {
		this.createFiles();
		if (getConfig().getBoolean("SilentStart.Enabled")) {
			instance = this;
			plugin = this;
			MetricsLite metrics = new MetricsLite(this);
			registerEvents();
			registerCommands();
			this.setEnabled(true);
			if (getConfig().getBoolean("CheckForUpdates.Enabled")) {
				new UpdateChecker(this, 53460).getLatestVersion(version -> {
					if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
						Logger.log(Logger.LogLevel.SUCCESS, ("LuckPermsGUI is up to date!"));
					} else {
						Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
						Logger.log(Logger.LogLevel.WARNING, ("LuckPermsGUI is outdated!"));
						Logger.log(Logger.LogLevel.WARNING, ("Newest version: " + version));
						Logger.log(Logger.LogLevel.WARNING, ("Your version: " + Settings.VERSION));
						Logger.log(Logger.LogLevel.WARNING, ("Please Update Here: " + Settings.PLUGIN_URL));
						Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
					}
				});
			}

		} else {
			instance = this;
			Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
			Logger.log(Logger.LogLevel.INFO, "Initializing LuckPermsGUI Version: " + Settings.VERSION);
			Logger.log(Logger.LogLevel.INFO, "Created by: " + Settings.DEVELOPER_NAME);
			Logger.log(Logger.LogLevel.INFO, "Website: " + Settings.DEVELOPER_URL);
			Logger.log(Logger.LogLevel.INFO, "Spigot: " + Settings.PLUGIN_URL);
			Logger.log(Logger.LogLevel.INFO, "Support: " + Settings.SUPPORT_DISCORD_URL);
			Logger.log(Logger.LogLevel.INFO, "Suggestions/Feedback: " + Settings.FEEDBACK);
			Logger.log(Logger.LogLevel.INFO, "Wiki: " + Settings.WIKI);
			Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
			Logger.log(Logger.LogLevel.INFO, "Plugin Loading...");
			Logger.log(Logger.LogLevel.INFO, "Registering Managers...");
			plugin = this;
			MetricsLite metrics = new MetricsLite(this);
			Logger.log(Logger.LogLevel.INFO, "Managers Registered!");
			Logger.log(Logger.LogLevel.INFO, "Registering Listeners...");
			registerEvents();
			Logger.log(Logger.LogLevel.INFO, "Listeners Registered!");
			Logger.log(Logger.LogLevel.INFO, "Registering Commands...");
			registerCommands();
			Logger.log(Logger.LogLevel.INFO, "Commands Registered!");
			Logger.log(Logger.LogLevel.INFO, "Registering Config...");
			this.createFiles();
			Logger.log(Logger.LogLevel.INFO, "Config Registered!");
			Logger.log(Logger.LogLevel.SUCCESS, "LuckPermsGUI Version: " + Settings.VERSION + " Loaded.");
			this.setEnabled(true);
			Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
			Logger.log(Logger.LogLevel.INFO, "Checking for updates...");
			new UpdateChecker(this, 53460).getLatestVersion(version -> {
				if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
					Logger.log(Logger.LogLevel.SUCCESS, ("LuckPermsGUI is up to date!"));
				} else {
					Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
					Logger.log(Logger.LogLevel.WARNING, ("LuckPermsGUI is outdated!"));
					Logger.log(Logger.LogLevel.WARNING, ("Newest version: " + version));
					Logger.log(Logger.LogLevel.WARNING, ("Your version: " + Settings.VERSION));
					Logger.log(Logger.LogLevel.WARNING, ("Please Update Here: " + Settings.PLUGIN_URL));
					Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
				}
			});
		}
	}

	public void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new GroupsGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinEvents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EditGroup(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Permissions(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Parents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Prefix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Suffix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new UsersGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EditUser(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.AsVaidas.LuckPemsGUI.users.Permissions(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.AsVaidas.LuckPemsGUI.users.Parents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.AsVaidas.LuckPemsGUI.users.Prefix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.AsVaidas.LuckPemsGUI.users.Suffix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Wipe(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new TracksGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EditTrack(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new LPGUICommand(), this);

	}
	public void registerCommands() {
		this.getCommand("lpgui").setExecutor(new LPGUICommand());

	}

	private File configf;
	private FileConfiguration config;

	public void reloadFiles() {
		config = YamlConfiguration.loadConfiguration(configf);
	}

	private void createFiles() {
		configf = new File(getDataFolder(), "config.yml");
		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			saveResource("config.yml", false);
		}
		config = new YamlConfiguration();
		try {
			config.load(configf);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	public static Main getPlugin() {
		return plugin;
	}

	public static Plugin getInstance() {
		return instance;
	}
}
