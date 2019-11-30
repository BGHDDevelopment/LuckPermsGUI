/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.users;

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
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;

public class Prefix implements Listener {

	public static Map<Player, User> addPrefix = new HashMap<Player, User>();
	public static Map<Player, User> addTempPrefix = new HashMap<Player, User>();

	@EventHandler
	public void onaddParent(AsyncPlayerChatEvent e) {
		if (!addPrefix.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		User g = addPrefix.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp user "+g.getName()+" meta addprefix "+message);
		addPrefix.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			EditUser.open(e.getPlayer(), g);
		});
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onaddTempParent(AsyncPlayerChatEvent e) {
		if (!addTempPrefix.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		User g = addTempPrefix.get(e.getPlayer());

		Tools.sendCommand(e.getPlayer(), "lp user "+g.getName()+" meta addtempprefix "+message);
		addTempPrefix.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			EditUser.open(e.getPlayer(), g);
		});
		e.setCancelled(true);
	}
	
	static LuckPermsApi l = LuckPerms.getApi();

	public static void open(Player p, User user, int page) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms user prefixs");
		Tools.onAsync(() -> {

		
		// ----------------------- INFO ------------------------------
		ItemStack info = Tools.button(Material.ARMOR_STAND,
				"&6Info",
				Arrays.asList(
						"&cName: &e"+user.getName(),
						"&cUUID: &e"+user.getUuid(),
						"&cGroup: &e"+user.getPrimaryGroup(),
						"&cCounts:",
						"&c   Nodes: &e"+user.getNodes().size(),
						"&c   Permissions: &e"+user.getPermissions().size(),
						"&c   Prefixes: &e"+user.getCachedData().getMetaData(Contexts.global()).getPrefixes().size(),
						"&c   Suffixes: &e"+user.getCachedData().getMetaData(Contexts.global()).getSuffixes().size(),
						"&cCached data:",
						"&c   Current prefix: &e"+user.getCachedData().getMetaData(Contexts.global()).getPrefix(),
						"&c   Current suffix: &e"+user.getCachedData().getMetaData(Contexts.global()).getSuffix()
						),
				1);
		myInventory.setItem(4, info);
		// ----------------------- INFO ------------------------------
		
		int sk = 0;
		int i = 9;
		
		int from = 45*page-1;
		int to = 45*(page+1)-1;
		for (Node permission : user.getPermissions()) {
			if (!permission.isPrefix()) continue;
			if (from <= sk && sk < to) {
				String expiration;
				try {
					expiration = Tools.getTime(permission.getExpiry().getTime());
				} catch (Exception e) {
					expiration = "Never";
				}
				
				String server;
				try {
					server = permission.getServer().get();
				} catch (Exception e) {
					server = "global";
				}
				
				String world;
				try {
					world = permission.getWorld().get();
				} catch (Exception e) {
					world = "global";
				}
				
				ItemStack item = Tools.button(Material.TNT,
						"&6"+permission.getPrefix().getValue(),
						Arrays.asList(
								"&cID: &e"+sk,
								"&cPosition: &e"+permission.getPrefix().getKey(),
								"&cExpires in: &e"+expiration,
								"&cValue: &e"+permission.getValue(),
								"&cServer: &e"+server,
								"&cWorld: &e"+world,
								"&eClick to remove"
								), 1);
				myInventory.setItem(i, item);
				i++;
			}
			sk++;
		}

		if (to < sk) {
			ItemStack next = Tools.button(Material.SIGN, "&6Next", Arrays.asList("&eNext page", "&cCurrent: &e"+page), 1);
			myInventory.setItem(53, next);
		}
		
		ItemStack back = Tools.button(Material.BARRIER, "&6Back", Arrays.asList(""), 1);
		myInventory.setItem(8, back);
		
		});
		p.openInventory(myInventory);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms user prefixs")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						
						String group = ChatColor.stripColor(inv.getItem(4).getItemMeta().getLore().get(0).split(" ")[1]);
						User g = LuckPerms.getApi().getUser(group);
						
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Next")) {
							int current = Integer.valueOf(ChatColor.stripColor(inv.getItem(53).getItemMeta().getLore().get(1).split(" ")[1]));
							open(p, g, current+1);
						} else if (name.equals("Back")) {
							EditUser.open(p, g);
						} else if (!name.equals("Info")) {
							
							int id = Integer.valueOf(ChatColor.stripColor(item.getItemMeta().getLore().get(0).split(" ")[1]));

							int sk = 0;
							for (Node permission : g.getPermissions()) {
								if (!permission.isPrefix()) continue;
								if (sk == id) {
									if (permission.isTemporary())
										Tools.sendCommand(p, "lp user "+g.getName()+" unsettemp "+'"'+permission.getPermission()+'"'+" "+Tools.contextConverter(permission.getFullContexts()));
									else
										Tools.sendCommand(p, "lp user "+g.getName()+" unset "+'"'+permission.getPermission()+'"'+" "+Tools.contextConverter(permission.getFullContexts()));
									break;
								}
								sk++;
							}
								
							int current = 0;
							if (inv.getItem(53) != null)
								current = Integer.valueOf(ChatColor.stripColor(inv.getItem(53).getItemMeta().getLore().get(1).split(" ")[1]));

							int page = current;
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
								open(p, g, page);
							}, 3);
						}
					}
			}
	}
}
