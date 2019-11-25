package me.AsVaidas.LuckPemsGUI.tracks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class TracksGUI implements Listener {

	List<Player> newGroup = new ArrayList<Player>();
	
	@EventHandler
	public void onGroupAdd(AsyncPlayerChatEvent e) {
		if (!newGroup.contains(e.getPlayer())) return;
		String message = e.getMessage();
		
		Tools.sendCommand(e.getPlayer(), "lp createtrack "+message);
		newGroup.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer());
		}, 5);
		e.setCancelled(true);
	}
	
	static LuckPermsApi l = LuckPerms.getApi();

	public static void open(Player p) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms tracks");
		Tools.onAsync(() -> {
		
		int sk = 9;
		for (Track group : l.getTracks()) {
			String name = group.getName();
			ItemStack item = Tools.button(Material.TNT, "&6"+name, Arrays.asList("&ePress to edit this track"), 1);
			myInventory.setItem(sk, item);
			sk++;
		}

		ItemStack back = Tools.button(Material.BARRIER, "&6Back", Arrays.asList(""), 1);
		myInventory.setItem(8, back);

		ItemStack newGroup = Tools.button(Material.PAPER, "&6New track", Arrays.asList("&eCreate new track"), 1);
		myInventory.setItem(0, newGroup);
		
		});
		p.openInventory(myInventory);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms tracks")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						String group = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (group.equals("Back")) {
							Main.openGUI(p);
						} else if (group.equals("New track")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - Track name");
							newGroup.add(p);
							p.closeInventory();
						} else EditTrack.open(p, LuckPerms.getApi().getTrack(group));
					}
			}
	}
}
