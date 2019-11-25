package me.AsVaidas.LuckPemsGUI;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Track;
import me.lucko.luckperms.api.User;

public class Main extends JavaPlugin implements Listener {

	private static Plugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getServer().getPluginManager().registerEvents(new GroupsGUI(), this);
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
		
/*		Logger log = (Logger) LogManager.getRootLogger();
        log.addAppender((Appender) new Wipe());*/
        
		Bukkit.getServer().getPluginManager().registerEvents(new TracksGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EditTrack(), this);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sm(sender, "&cThis command can be done only by player");
			return true;
		}
		
		if (sender.hasPermission("luckperms.gui")) {
			Player p = ((Player) sender);
	
			if (cmd.getName().equalsIgnoreCase("lpgui")) {
				if (args.length == 0) 
					openGUI(p);
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
						EditGroup.open(p, LuckPerms.getApi().getGroup(args[1]));
					} else if (args[0].equals("user")) {
						EditUser.open(p, args[1]);
					} else if (args[0].equals("track")) {
						Track user = LuckPerms.getApi().getTrack(args[1]);
						EditTrack.open(p, user);
					} else
						sm(sender, "&cUsage: &e/lpgui user/group/track <user/group/track>");
			}
		} else sm(sender, "&cYou can't use this command! No permission");
		return true;
	}
	
	public static void openGUI(Player sender) {
		Inventory myInventory = Bukkit.createInventory(null, 9, ChatColor.AQUA+"LuckPerms");
		Tools.onAsync(() -> {
			ItemStack button1 = Tools.button(Material.ANVIL, "&6Groups", Arrays.asList("&eSelect to edit groups"), 1);
			ItemStack button2 = Tools.button(Material.ANVIL, "&6Users", Arrays.asList("&eSelect to edit online users"), 1);
			ItemStack button3 = Tools.button(Material.ANVIL, "&6Tracks", Arrays.asList("&eSelect to edit tracks"), 1);
			
			myInventory.setItem(2, button1);
			myInventory.setItem(4, button2);
			myInventory.setItem(6, button3);
		});
		sender.openInventory(myInventory);
	}

	private void sm(CommandSender sender, String txt) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', txt));
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Groups"))
							GroupsGUI.open(p);
						if (name.equals("Users"))
							UsersGUI.open(p);
						if (name.equals("Tracks"))
							TracksGUI.open(p);
					}
			}
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
					openGUI(p);
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
						EditGroup.open(p, LuckPerms.getApi().getGroup(args[3]));
					} else if (args[2].equals("user")) {
						User user = LuckPerms.getApi().getUser(args[3]);
						EditUser.open(p, user);
					} else if (args[2].equals("track")) {
						Track user = LuckPerms.getApi().getTrack(args[3]);
						EditTrack.open(p, user);
					} else
						sm(sender, "&cUsage: &e/lp gui user/group <user/group>");
		} else sm(sender, "&cYou can't use this command! No permission");
	}

	public static Plugin getInstance() {
		return instance;
	}
}
