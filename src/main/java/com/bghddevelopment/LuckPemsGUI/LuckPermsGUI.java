/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package com.bghddevelopment.LuckPemsGUI;

import java.io.File;
import java.io.IOException;

import com.bghddevelopment.LuckPemsGUI.events.Events;
import com.bghddevelopment.LuckPemsGUI.util.updatechecker.UpdateChecker;
import lombok.Getter;
import com.bghddevelopment.LuckPemsGUI.commands.LPGUICommand;
import com.bghddevelopment.LuckPemsGUI.util.Metrics;
import com.bghddevelopment.LuckPemsGUI.util.updatechecker.JoinEvents;
import com.bghddevelopment.LuckPemsGUI.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.bghddevelopment.LuckPemsGUI.groups.EditGroup;
import com.bghddevelopment.LuckPemsGUI.groups.GroupsGUI;
import com.bghddevelopment.LuckPemsGUI.groups.Parents;
import com.bghddevelopment.LuckPemsGUI.groups.Permissions;
import com.bghddevelopment.LuckPemsGUI.groups.Prefix;
import com.bghddevelopment.LuckPemsGUI.groups.Suffix;
import com.bghddevelopment.LuckPemsGUI.tracks.EditTrack;
import com.bghddevelopment.LuckPemsGUI.tracks.TracksGUI;
import com.bghddevelopment.LuckPemsGUI.users.EditUser;
import com.bghddevelopment.LuckPemsGUI.users.UsersGUI;
import com.bghddevelopment.LuckPemsGUI.users.Wipe;

@Getter
public class LuckPermsGUI extends JavaPlugin {

	@Getter
	private static Plugin instance;
	public static LuckPermsGUI plugin;

	@Override
	public void onEnable() {
		createFiles();
		if (getConfig().getBoolean("SilentStart.Enabled")) {
			instance = this;
			plugin = this;
			new Metrics(this, 5970);
			registerEvents();
			registerCommands();
			setEnabled(true);
			Logger.log(Logger.LogLevel.SUCCESS, "LuckPermsGUI Version: " + getDescription().getVersion() + " Loaded.");
			if (getConfig().getBoolean("CheckForUpdates.Enabled")) {
				new UpdateChecker(this, 53460).getLatestVersion(version -> {
					if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
						Logger.log(Logger.LogLevel.SUCCESS, ("LuckPermsGUI is up to date!"));
					} else {
						Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
						Logger.log(Logger.LogLevel.WARNING, ("LuckPermsGUI is outdated!"));
						Logger.log(Logger.LogLevel.WARNING, ("Newest version: " + version));
						Logger.log(Logger.LogLevel.WARNING, ("Your version: " + getDescription().getVersion()));
						Logger.log(Logger.LogLevel.WARNING, ("Please Update Here: https://spigotmc.org/resources/53460"));
						Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
					}
				});
			}

		} else {
			instance = this;
			Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
			Logger.log(Logger.LogLevel.INFO, "Initializing LuckPermsGUI Version: " + getDescription().getVersion());
			Logger.log(Logger.LogLevel.INFO, "Created by: BGHDDevelopment LLC");
			Logger.log(Logger.LogLevel.INFO, "Spigot: https://spigotmc.org/resources/53460");
			Logger.log(Logger.LogLevel.INFO, "Support: https://bghddevelopment.com/discord");
			Logger.log(Logger.LogLevel.INFO, "Wiki: https://luckpermsgui.bghddevelopment.com");
			Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
			Logger.log(Logger.LogLevel.INFO, "Plugin Loading...");
			Logger.log(Logger.LogLevel.INFO, "Registering Managers...");
			plugin = this;
			Metrics metrics = new Metrics(this, 5970);
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
			Logger.log(Logger.LogLevel.SUCCESS, "LuckPermsGUI Version: " + getDescription().getVersion() + " Loaded.");
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
					Logger.log(Logger.LogLevel.WARNING, ("Your version: " + getDescription().getVersion()));
					Logger.log(Logger.LogLevel.WARNING, ("Please Update Here:  https://spigotmc.org/resources/53460"));
					Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
				}
			});
		}
	}

	public void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new GroupsGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinEvents(plugin), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EditGroup(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Permissions(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Parents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Prefix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Suffix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new UsersGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EditUser(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new com.bghddevelopment.LuckPemsGUI.users.Permissions(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new com.bghddevelopment.LuckPemsGUI.users.Parents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new com.bghddevelopment.LuckPemsGUI.users.Prefix(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new com.bghddevelopment.LuckPemsGUI.users.Suffix(), this);
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
}
