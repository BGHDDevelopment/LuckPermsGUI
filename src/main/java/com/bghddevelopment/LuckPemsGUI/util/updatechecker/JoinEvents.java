/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package com.bghddevelopment.LuckPemsGUI.util.updatechecker;

import com.bghddevelopment.LuckPemsGUI.LuckPermsGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvents implements Listener {

    private LuckPermsGUI plugin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (plugin.getConfig().getBoolean("Update.Enabled")) {
            if (player.hasPermission("luckpermsgui.update")) {
                new UpdateChecker(plugin, 53460).getLatestVersion(version -> {
                    if (!LuckPermsGUI.getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
                        player.sendMessage(ChatColor.GRAY + "****************************************************************");
                        player.sendMessage(ChatColor.RED + "LuckPermsGUI is outdated!");
                        player.sendMessage(ChatColor.RED + "Newest version: " + version);
                        player.sendMessage(ChatColor.RED + "Your version: " + ChatColor.BOLD + plugin.getDescription().getVersion());
                        player.sendMessage(ChatColor.GOLD + "Please Update Here: https://spigotmc.org/resources/53460");
                        player.sendMessage(ChatColor.GRAY + "****************************************************************");
                    }
                });
            }
        }
    }


    @EventHandler
    public void onDevJoin(PlayerJoinEvent e) { //THIS EVENT IS USED FOR DEBUG REASONS ONLY!
        Player player = e.getPlayer();
        if (player.getName().equalsIgnoreCase("BGHDDev_YT") || player.getName().equalsIgnoreCase("BGHDDevelopment")) {
            player.sendMessage(ChatColor.RED + "BGHDDevelopment Debug Message");
            player.sendMessage(" ");
            player.sendMessage(ChatColor.GREEN + "This server is using LuckPermsGUI version " + plugin.getDescription().getVersion());
            player.sendMessage(" ");
        }
    }

}
    