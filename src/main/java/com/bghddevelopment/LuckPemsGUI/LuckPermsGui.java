package com.master86.Luckpermsgui;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.master86.Luckpermsgui.events.Events;
import com.master86.Luckpermsgui.util.updatechecker.UpdateChecker;
import com.master86.Luckpermsgui.commands.LPGUICommand;
import com.master86.Luckpermsgui.util.Metrics;
import com.master86.Luckpermsgui.util.updatechecker.JoinEvents;
import com.master86.Luckpermsgui.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.master86.Luckpermsgui.groups.EditGroup;
import com.master86.Luckpermsgui.groups.GroupsGUI;
import com.master86.Luckpermsgui.groups.Parents;
import com.master86.Luckpermsgui.groups.Permissions;
import com.master86.Luckpermsgui.groups.Prefix;
import com.master86.Luckpermsgui.groups.Suffix;
import com.master86.Luckpermsgui.tracks.EditTrack;
import com.master86.Luckpermsgui.tracks.TracksGUI;
import com.master86.Luckpermsgui.users.EditUser;
import com.master86.Luckpermsgui.users.UsersGUI;
import com.master86.Luckpermsgui.users.Wipe;


public final class LuckPermsGui extends JavaPlugin {


    public static LuckPermsGui plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        createFiles();
        Plugin instance = this;
        if (getConfig().getBoolean("SilentStart.Enabled")) {
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
            Logger.log(Logger.LogLevel.OUTLINE, "*********************************************************************");
            Logger.log(Logger.LogLevel.INFO, "Initializing LuckPermsGUI Version: " + getDescription().getVersion());
            Logger.log(Logger.LogLevel.INFO, "Created by: BGHDDevelopment LLC && updatet by master86");
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
        Bukkit.getServer().getPluginManager().registerEvents(new com.master86.Luckpermsgui.users.Permissions(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new com.master86.Luckpermsgui.users.Parents(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new com.master86.Luckpermsgui.users.Prefix(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new com.master86.Luckpermsgui.users.Suffix(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Wipe(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new TracksGUI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditTrack(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LPGUICommand(), this);

    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("lpgui")).setExecutor(new LPGUICommand());
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
