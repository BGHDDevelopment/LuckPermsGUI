/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.groups;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
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

public class Prefix implements Listener {

	public static Map<Player, Group> addPrefix = new HashMap<>();
	public static Map<Player, Group> addTempPrefix = new HashMap<>();

	@EventHandler
	public void onaddParent(AsyncPlayerChatEvent e) {
		if (!addPrefix.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Group g = addPrefix.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp group "+g.getName()+" meta addprefix "+message);
		addPrefix.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			EditGroup.open(e.getPlayer(), g);
		}, 5);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onaddTempParent(AsyncPlayerChatEvent e) {
		if (!addTempPrefix.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Group g = addTempPrefix.get(e.getPlayer());

		Tools.sendCommand(e.getPlayer(), "lp group "+g.getName()+" meta addtempprefix "+message);
		addTempPrefix.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			EditGroup.open(e.getPlayer(), g);
		}, 5);
		e.setCancelled(true);
	}

	static LuckPerms l = LuckPermsProvider.get();

	public static void open(Player p, Group group, int page) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms group prefixs");
		Tools.onAsync(() -> {

		
		// ----------------------- INFO ------------------------------
		int weight = group.getWeight().orElse(0);
		ItemStack info = Tools.button(Material.ARMOR_STAND,
				"&6Info",
				Arrays.asList(
						"&cGroup info: &e"+group.getName(),
						"&cDisplay name: &e"+group.getFriendlyName(),
						"&cWeight: &e"+weight,
						"&cCounts:",
						"  &cNodes: &e"+group.getNodes().size(),
						"  &cPermissions: &e"+group.getPermissions().size(),
						"  &cPrefixes: &e"+group.getCachedData().getMetaData(Contexts.global()).getPrefixes().size(),
						"  &cSuffixes: &e"+group.getCachedData().getMetaData(Contexts.global()).getSuffixes().size()),
				1);
		myInventory.setItem(4, info);
		// ----------------------- INFO ------------------------------
		
		int sk = 0;
		int i = 9;
		
		int from = 45*page-1;
		int to = 45*(page+1)-1;
		for (Node permission : group.getPermissions()) {
			if (!permission.isPrefix()) continue;
			if (from <= sk && sk < to) {
				String expiration = permission.isTemporary() ? Tools.getTime(permission.getExpiry().getTime()) : "Never";
				String server = permission.getServer().orElse("global");
				String world = permission.getWorld().orElse("global");
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
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms group prefixs")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						
						String group = ChatColor.stripColor(inv.getItem(4).getItemMeta().getLore().get(0).split(" ")[2]);
						Group g = l.getGroupManager().getGroup(group);
						
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Next")) {
							int current = Integer.parseInt(ChatColor.stripColor(inv.getItem(53).getItemMeta().getLore().get(1).split(" ")[1]));
							open(p, g, current+1);
						} else if (name.equals("Back")) {
							EditGroup.open(p, g);
						} else if (!name.equals("Info")) {
							
							int id = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(0).split(" ")[1]));

							int sk = 0;
							for (Node permission : g.getPermissions()) {
								if (!permission.isPrefix()) continue;
								if (sk == id) {
									if (Main.plugin.getConfig().getBoolean("UseLuckPerms5.Enabled")) {
										Map.Entry<Integer, String> prefix = permission.getPrefix();
										if (permission.isTemporary())
											Tools.sendCommand(p, "lp group " + g.getName() + " meta removetempprefix " + prefix.getKey() + " " + '"' + prefix.getValue() + '"' + " " + Tools.contextConverter(permission.getFullContexts()));
										else
											Tools.sendCommand(p, "lp group " + g.getName() + " meta removeprefix " + prefix.getKey() + " " + '"' + prefix.getValue() + '"' + " " + Tools.contextConverter(permission.getFullContexts()));
										break;
									} else {
										if (permission.isTemporary())
											Tools.sendCommand(p, "lp group " + g.getName() + " unsettemp " + '"' + permission.getPermission() + '"' + " " + Tools.contextConverter(permission.getFullContexts()));
										else
											Tools.sendCommand(p, "lp group " + g.getName() + " unset " + '"' + permission.getPermission() + '"' + " " + Tools.contextConverter(permission.getFullContexts()));
										break;
									}
								}
								sk++;
							}
								
							int current = 0;
							if (inv.getItem(53) != null)
								current = Integer.parseInt(ChatColor.stripColor(inv.getItem(53).getItemMeta().getLore().get(1).split(" ")[1]));

							int page = current;
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
								open(p, g, page);
							}, 3);
						}
					}
			}
	}
}
