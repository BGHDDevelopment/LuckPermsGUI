/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.commands;

import me.AsVaidas.LuckPemsGUI.groups.EditGroup;
import me.AsVaidas.LuckPemsGUI.tracks.EditTrack;
import me.AsVaidas.LuckPemsGUI.users.EditUser;
import me.AsVaidas.LuckPemsGUI.util.OpenGUI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class LPGUICommand implements CommandExecutor, Listener {

    static LuckPerms l = LuckPermsProvider.get();


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sm(sender, "&cThis command can be done only by player");
            return true;
        }

        if (sender.hasPermission("luckperms.gui")) {
            Player p = ((Player) sender);

            if (cmd.getName().equalsIgnoreCase("lpgui")) {
                if (args.length == 0)
                    OpenGUI.openGUI(p);
                if (args.length == 1)
                    if (args[0].equals("group"))
                        sm(sender, "&cUsage: &e/lpgui group <group>");
                    else if (args[0].equals("user"))
                        sm(sender, "&cUsage: &e/lpgui user <user>");
                    else if (args[0].equals("track"))
                        sm(sender, "&cUsage: &e/lpgui track <track>");
                    else
                        sm(sender, "&cUsage: &e/lpgui user/group <user/group>");
                if (args.length == 2)
                    if (args[0].equals("group")) {
                        EditGroup.open(p, l.getGroupManager().getGroup(args[1]));
                    } else if (args[0].equals("user")) {
                        EditUser.open(p, args[1]);
                    } else if (args[0].equals("track")) {
                        Track user = l.getTrackManager().getTrack(args[1]);
                        EditTrack.open(p, user);
                    } else
                        sm(sender, "&cUsage: &e/lpgui user/group/track <user/group/track>");
            }
        } else sm(sender, "&cYou can't use this command! No permission");
        return true;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!e.getMessage().contains("/lp gui")) return;
        e.setCancelled(true);
        Player sender = e.getPlayer();

        if (sender.hasPermission("luckperms.gui")) {
            Player p = ((Player) sender);

            String[] args = e.getMessage().split(" ");

            if (args.length == 2)
                OpenGUI.openGUI(p);
            if (args.length == 3)
                if (args[2].equals("group"))
                    sm(sender, "&cUsage: &e/lp gui group <group>");
                else if (args[2].equals("user"))
                    sm(sender, "&cUsage: &e/lp gui user <user>");
                else if (args[2].equals("track"))
                    sm(sender, "&cUsage: &e/lp gui track <track>");
                else
                    sm(sender, "&cUsage: &e/lp gui user/group <user/group>");
            if (args.length == 4)
                if (args[2].equals("group")) {
                    EditGroup.open(p, l.getGroupManager().getGroup(args[3]));
                } else if (args[2].equals("user")) {
                    User user = l.getUserManager().getUser(args[3]);
                    EditUser.open(p, user);
                } else if (args[2].equals("track")) {
                    Track user = l.getTrackManager().getTrack(args[3]);
                    EditTrack.open(p, user);
                } else
                    sm(sender, "&cUsage: &e/lp gui user/group <user/group>");
        } else sm(sender, "&cYou can't use this command! No permission");
    }


    private void sm(CommandSender sender, String txt) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', txt));
    }
}
