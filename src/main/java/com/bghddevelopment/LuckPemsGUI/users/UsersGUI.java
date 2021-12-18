/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package com.bghddevelopment.LuckPemsGUI.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bghddevelopment.LuckPemsGUI.LuckPermsGUI;
import com.bghddevelopment.LuckPemsGUI.util.Tools;
import com.bghddevelopment.LuckPemsGUI.util.OpenGUI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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

public class UsersGUI implements Listener {

	static LuckPerms l = LuckPermsProvider.get();
	
	List<Player> editUser = new ArrayList<>();

	@EventHandler
	public void onUserEdit(AsyncPlayerChatEvent e) {
		if (!editUser.contains(e.getPlayer())) return;
		String message = e.getMessage();
		editUser.remove(e.getPlayer());

		Bukkit.getScheduler().scheduleSyncDelayedTask(LuckPermsGUI.getInstance(), () -> {
			EditUser.open(e.getPlayer(), message);
		}, 5);
			
		e.setCancelled(true);
	}
	
	public static void open(Player p) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms users");
		Tools.onAsync(() -> {
			
			int sk = 9;
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (sk >= 53) continue;
				ItemStack item = Tools.head(player.getName(),
						"&6"+player.getName(), 
						Arrays.asList("&eClick to edit"), 1);
						
				myInventory.setItem(sk, item);
				sk++;
			}

			ItemStack newGroup = Tools.button(Material.PAPER, "&6Edit offline user", Arrays.asList("&eClick to edit offline/online user"), 1);
			myInventory.setItem(0, newGroup);

			ItemStack wipe = Tools.button(Material.REDSTONE, "&4Remove ranks", Arrays.asList("&eRemove from all users", "&especific ranks.", "&e(Great for wipes)"), 1);
			myInventory.setItem(1, wipe);

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
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms users")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Back")) {
							OpenGUI.openGUI(p);
						} else if (name.equals("Edit offline user")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - User name");
							editUser.add(p);
							p.closeInventory();
						} else if (name.equals("Remove ranks")) {
							Wipe.open(p);
						} else EditUser.open(p, l.getUserManager().getUser(name));
					}
			}
	}
}
