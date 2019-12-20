/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.users;

import java.util.Arrays;
import org.apache.logging.log4j.core.LogEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.AsVaidas.LuckPemsGUI.Tools;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;

public class Wipe implements Listener {

	static LuckPermsApi l = LuckPerms.getApi();
	
	public static void open(Player p) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms WIPE");
		Tools.onAsync(() -> {

			int sk = 9;
			for (Group group : l.getGroups()) {
				String name = group.getName();
				ItemStack item = Tools.button(Material.TNT, "&6"+name, Arrays.asList("&eSelect to remove from users"), 1);
				myInventory.setItem(sk, item);
				sk++;
			}

			ItemStack back = Tools.button(Material.BARRIER, "&6Back", Arrays.asList(""), 1);
			myInventory.setItem(7, back);
			ItemStack confirm = Tools.button(Material.SIGN, "&2&lCONFIRM", Arrays.asList("&ePress when everything selected","&cThis will take some time!!"), 1);
			myInventory.setItem(8, confirm);
			
		});
		p.openInventory(myInventory);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms WIPE")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Back")) {
							UsersGUI.open(p);
						} else if (name.equals("CONFIRM")) {
							for (ItemStack groupItem : inv) {
								if (groupItem == null)
									continue;
								if (groupItem.getType().equals(Material.EMERALD)) {
									String groupName = ChatColor.stripColor(groupItem.getItemMeta().getDisplayName());
									Tools.sendConsole("lp bulkupdate users delete \"permission ~~ group."+groupName+"\"");
								}
							}
							UsersGUI.open(p);
							Tools.sendMessage(p, "&aAction was completed!");
						} else {
							if (item.getType().equals(Material.TNT)) {
								item.setType(Material.EMERALD);
							} else {
								item.setType(Material.TNT);
							}
						}
					}
			}
	}

	//@Override
	public void append(LogEvent e) {
		String message = e.getMessage().getFormattedMessage();
		if (message.contains("/lp bulkupdate confirm")) {
			String[] words = message.split(" ");
			String code = words[5];
			Tools.sendConsole("lp bulkupdate confirm "+code);
		}
	}
	
    //@Override
    public boolean isStarted() {
        return true;
    }
}
