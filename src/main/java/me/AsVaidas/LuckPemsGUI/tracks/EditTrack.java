/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.tracks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.AsVaidas.LuckPemsGUI.Main;
import me.AsVaidas.LuckPemsGUI.Tools;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;

public class EditTrack implements Listener {

	public static Map<Player, Track> addgroup = new HashMap<Player, Track>();
	public static Map<Player, Track> insertgroup = new HashMap<Player, Track>();
	public static Map<Player, Track> rename = new HashMap<Player, Track>();
	public static Map<Player, Track> clone = new HashMap<Player, Track>();
	static LuckPermsApi l = LuckPerms.getApi();
	
	@EventHandler
	public void onGroupAdd(AsyncPlayerChatEvent e) {
		if (!addgroup.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Track g = addgroup.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp track "+g.getName()+" append "+message);
		addgroup.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onGroupInsert(AsyncPlayerChatEvent e) {
		if (!insertgroup.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Track g = insertgroup.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp track "+g.getName()+" insert "+message);
		insertgroup.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onRename(AsyncPlayerChatEvent e) {
		if (!rename.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Track g = rename.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp track "+g.getName()+" rename "+message);
		rename.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onClone(AsyncPlayerChatEvent e) {
		if (!clone.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Track g = clone.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp track "+g.getName()+" clone "+message);
		clone.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}

	public static void open(Player p, Track group) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms track");
		Tools.onAsync(() -> {
			
			ItemStack info = Tools.button(Material.ARMOR_STAND,
					"&6Info",
					Arrays.asList(
							"&cName: &e"+group.getName(),
							"&cAll groups: &e"+group.getSize()),
					1);
			myInventory.setItem(4, info);
			// ----------------------- INFO ------------------------------
	
			// ----------------------- PERMISSION ------------------------------
			ItemStack addnewgroup = Tools.button(Material.TORCH, "&6Add group", Arrays.asList("&eClick here to add a group"), 1);
			myInventory.setItem(0, addnewgroup);
			
			ItemStack insertgroup = Tools.button(Material.ARROW, "&6Insert group", Arrays.asList("&eClick to insert a group"), 1);
			myInventory.setItem(1, insertgroup);
			
			ItemStack clear = Tools.button(Material.REDSTONE_BLOCK, "&cClear", Arrays.asList("&eRemove all groups"), 1);
			myInventory.setItem(2, clear);

			ItemStack delete = Tools.button(Material.REDSTONE, "&cDelete", Arrays.asList("&eClick to delete the track"), 1);
			myInventory.setItem(5, delete);
			
			ItemStack rename = Tools.button(Material.NAME_TAG, "&6Rename", Arrays.asList("&eClick to rename the track"), 1);
			myInventory.setItem(6, rename);

			ItemStack clone = Tools.button(Material.PAPER, "&6Clone", Arrays.asList("&eClick to clone the track"), 1);
			myInventory.setItem(7, clone);
			// ----------------------- PERMISSION ------------------------------
	
			ItemStack back = Tools.button(Material.BARRIER, "&6Back", Arrays.asList(""), 1);
			myInventory.setItem(8, back);
			
			
			int sk = 9;
			for (String g : group.getGroups()) {
				ItemStack item = Tools.button(Material.TNT, "&6"+g, Arrays.asList("&cClick to remove"), 1);
				myInventory.setItem(sk, item);
				sk++;
			}
		});
		p.openInventory(myInventory);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms track")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						
						String group = ChatColor.stripColor(inv.getItem(4).getItemMeta().getLore().get(0).split(" ")[1]);
						Track g = LuckPerms.getApi().getTrack(group);
						
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Back")) {
							TracksGUI.open(p);
						} else if (name.equals("Add group")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Group&8>");
							Tools.sendMessage(p, "&aGroup - The group you want to add");
							addgroup.put(p, g);
							p.closeInventory();
						} else if (name.equals("Insert group")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Group&8> &8<&7Position&8>");
							Tools.sendMessage(p, "&aGroup - The group you want to add");
							Tools.sendMessage(p, "&aPosition - The position to insert the group at");
							insertgroup.put(p, g);
							p.closeInventory();
						} else if (name.equals("Clear")) {
							g.clearGroups();
							open(p, g);
						} else if (name.equals("Delete")) {
							Tools.sendCommand(p, "lp deletetrack "+g.getName());
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
								TracksGUI.open(p);
							}, 3);
						} else if (name.equals("Rename")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - The new name of the track");
							rename.put(p, g);
							p.closeInventory();
						} else if (name.equals("Clone")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - The name of the new track");
							clone.put(p, g);
							p.closeInventory();
						} else if (item.getType().equals(Material.TNT)) {
							Tools.sendCommand(p, "lp track "+g.getName()+" remove "+ChatColor.stripColor(item.getItemMeta().getDisplayName()));
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
								open(p, g);
							}, 3);
						}
					}
			}
	}
}
