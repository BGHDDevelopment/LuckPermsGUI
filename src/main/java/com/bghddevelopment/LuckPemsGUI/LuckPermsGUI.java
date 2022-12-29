/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package com.bghddevelopment.LuckPemsGUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.bghddevelopment.LuckPemsGUI.events.Events;
import com.bghddevelopment.LuckPemsGUI.util.Color;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import com.bghddevelopment.LuckPemsGUI.commands.LPGUICommand;
import com.bghddevelopment.LuckPemsGUI.util.Metrics;
import com.bghddevelopment.LuckPemsGUI.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
        instance = this;
        plugin = this;
        new Metrics(this, 5970);
        registerEvents();
        registerCommands();
        setEnabled(true);
        Logger.log(Logger.LogLevel.SUCCESS, "LuckPermsGUI Version: " + getDescription().getVersion() + " Loaded.");
        updateCheck(Bukkit.getConsoleSender(), true);
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new GroupsGUI(), this);
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

    public void updateCheck(CommandSender sender, boolean console) {
        try {
            String urlString = "https://updatecheck.bghddevelopment.com";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer response = new StringBuffer();
            while ((input = reader.readLine()) != null) {
                response.append(input);
            }
            reader.close();
            JsonObject object = new JsonParser().parse(response.toString()).getAsJsonObject();

            if (object.has("plugins")) {
                JsonObject plugins = object.get("plugins").getAsJsonObject();
                JsonObject info = plugins.get("LuckPermsGUI").getAsJsonObject();
                String version = info.get("version").getAsString();
                Boolean archived = info.get("archived").getAsBoolean();
                if(archived) {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cThis plugin has been marked as 'Archived' by BGHDDevelopment LLC."));
                    sender.sendMessage(Color.translate("&cThis version will continue to work but will not receive updates or support."));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    return;
                }
                if (version.equals(getDescription().getVersion())) {
                    if (console) {
                        sender.sendMessage(Color.translate("&aLuckPermsGUI is on the latest version."));
                    }
                } else {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour LuckPermsGUI version is out of date!"));
                    sender.sendMessage(Color.translate("&cWe recommend updating ASAP!"));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour Version: &e" + getDescription().getVersion()));
                    sender.sendMessage(Color.translate("&aNewest Version: &e" + version));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                }
            } else {
                sender.sendMessage(Color.translate("&cWrong response from update API, contact plugin developer!"));
            }
        } catch (
                Exception ex) {
            sender.sendMessage(Color.translate("&cFailed to get updater check. (" + ex.getMessage() + ")"));
        }
    }

    public void registerCommands() {
        this.getCommand("lpgui").setExecutor(new LPGUICommand());
    }
}
