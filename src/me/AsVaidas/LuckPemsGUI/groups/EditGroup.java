package me.AsVaidas.LuckPemsGUI.groups;

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
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;

public class EditGroup implements Listener {

	public static Map<Player, Group> setWeight = new HashMap<Player, Group>();
	public static Map<Player, Group> setName = new HashMap<Player, Group>();
	public static Map<Player, Group> rename = new HashMap<Player, Group>();
	public static Map<Player, Group> clone = new HashMap<Player, Group>();
	static LuckPermsApi l = LuckPerms.getApi();
	
	@EventHandler
	public void onsetWeight(AsyncPlayerChatEvent e) {
		if (!setWeight.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Group g = setWeight.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp group "+g.getName()+" setweight "+message);
		setWeight.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onsetName(AsyncPlayerChatEvent e) {
		if (!setName.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Group g = setName.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp group "+g.getName()+" setdisplayname "+message);
		setName.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onrename(AsyncPlayerChatEvent e) {
		if (!rename.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Group g = rename.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp group "+g.getName()+" rename "+message);
		rename.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onclone(AsyncPlayerChatEvent e) {
		if (!clone.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		Group g = clone.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp group "+g.getName()+" clone "+message);
		clone.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), l.getGroup(message));
		}, 3);
		e.setCancelled(true);
	}
	

	public static void open(Player p, Group group) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA+"LuckPerms group");
		Tools.onAsync(() -> {
			
			// ----------------------- INFO ------------------------------
			int weight = 0;
			try {
				weight = group.getWeight().getAsInt();
			} catch (Exception e) {
				weight = 0;
			}
			ItemStack info = Tools.button(Material.ARMOR_STAND,
					"&6Info",
					Arrays.asList(
							"&cGroup info: &e"+group.getName(),
							"&cDisplay name: &e"+group.getFriendlyName(),
							"&cWeight: &e"+weight,
							"&cCounts:",
							"   &cNodes: &e"+group.getNodes().size(),
							"   &cPermissions: &e"+group.getPermissions().size(),
							"   &cPrefixes: &e"+group.getCachedData().getMetaData(Contexts.global()).getPrefixes().size(),
							"   &cSuffixes: &e"+group.getCachedData().getMetaData(Contexts.global()).getSuffixes().size()),
					1);
			myInventory.setItem(4, info);
			// ----------------------- INFO ------------------------------
	
			// ----------------------- PERMISSION ------------------------------
			ItemStack perm = Tools.button(Material.SIGN, "&6Permissions", Arrays.asList("&e----------->",
					"&aDescription:",
					"&2   Permission nodes are a method of defining",
					"&2   the access each player has on a server,",
					"&2   in the form of a name and a true/false state."), 1);
			ItemStack pall = Tools.button(Material.CHEST, "&6All permissions", Arrays.asList("&eClick here to see all permissions"), 1);
			ItemStack pset = Tools.button(Material.ARROW, "&6Add permission", Arrays.asList("&eClick to add new permission"), 1);
			ItemStack psettemp = Tools.button(Material.ARROW, "&6Add temp permission", Arrays.asList("&eClick to add new temporary permission"), 1);
			ItemStack pcheck = Tools.button(Material.TORCH, "&6Check if group has permission", Arrays.asList("&eClick to check if group has permission"), 1);
			myInventory.setItem(9, perm);
			myInventory.setItem(10, pall);
			myInventory.setItem(11, pset);
			myInventory.setItem(12, psettemp);
			myInventory.setItem(13, pcheck);
			// ----------------------- PERMISSION ------------------------------
	
			// ----------------------- PARENT ------------------------------
			ItemStack parent = Tools.button(Material.SIGN, "&6Parent", Arrays.asList("&e----------->",
					"&aDescription:",
					"&2   Parent is a group. Groups has all",
					"&2   permissions of its parent. For example:",
					"&2   Group with parent 'default' will have all",
					"&2   default group permissions."), 1);
			ItemStack parentall = Tools.button(Material.CHEST, "&6All parents", Arrays.asList("&eClick here to see all parents"), 1);
			ItemStack parentadd = Tools.button(Material.ARROW, "&6Add parent", Arrays.asList("&eClick here to add parent"), 1);
			ItemStack parenttempadd = Tools.button(Material.ARROW, "&6Add temp parent", Arrays.asList("&eClick here to add temporary parent"), 1);
			myInventory.setItem(18, parent);
			myInventory.setItem(19, parentall);
			myInventory.setItem(20, parentadd);
			myInventory.setItem(21, parenttempadd);
			// ----------------------- PARENT ------------------------------
	
			// ----------------------- PREFIX/SUFFIX ------------------------------
			ItemStack meta = Tools.button(Material.SIGN, "&6Prefix/Suffix", Arrays.asList("&e----------->",
					"&aPrefix Description:",
					"&2   Prefix is text that goes",
					"&2   before the nick.",
					"&aSuffix Description:",
					"&2   Suffix is text that goes",
					"&2   after the nick."), 1);
			ItemStack metaallprefix = Tools.button(Material.CHEST, "&6All prefixes", Arrays.asList("&eClick here to see all prefixes"), 1);
			ItemStack metaallsuffix = Tools.button(Material.CHEST, "&6All suffixes", Arrays.asList("&eClick here to see all suffixes"), 1);
			ItemStack metaaddprefix = Tools.button(Material.ARROW, "&6Add prefix", Arrays.asList("&eClick here to add prefix"), 1);
			ItemStack metaaddsuffix = Tools.button(Material.ARROW, "&6Add suffix", Arrays.asList("&eClick here to add suffix"), 1);
			ItemStack metaaddtempprefix = Tools.button(Material.ARROW, "&6Add temp prefix", Arrays.asList("&eClick here to add temporary prefix"), 1);
			ItemStack metaaddtempsuffix = Tools.button(Material.ARROW, "&6Add temp suffix", Arrays.asList("&eClick here to add temporary suffix"), 1);
			myInventory.setItem(27, meta);
			myInventory.setItem(28, metaallprefix);
			myInventory.setItem(29, metaallsuffix);
			myInventory.setItem(30, metaaddprefix);
			myInventory.setItem(31, metaaddsuffix);
			myInventory.setItem(32, metaaddtempprefix);
			myInventory.setItem(33, metaaddtempsuffix);
			// ----------------------- PREFIX/SUFFIX ------------------------------
	
			// ----------------------- PREFIX/SUFFIX ------------------------------
			ItemStack meta2 = Tools.button(Material.SIGN, "&6Meta", Arrays.asList("&e----------->",
					"&aMeta Description:",
					"&2  You can set custom keys",
					"&2  and values"), 1);
			ItemStack metaKeys = Tools.button(Material.CHEST, "&6All keys", Arrays.asList("&eClick here to see all meta keys"), 1);
			ItemStack addKey = Tools.button(Material.ARROW, "&6Add key", Arrays.asList("&eClick here to add meta key"), 1);
			ItemStack addTempKey = Tools.button(Material.ARROW, "&6Add temp key", Arrays.asList("&eClick here to add temp meta key"), 1);
			myInventory.setItem(36, meta2);
			myInventory.setItem(37, metaKeys);
			myInventory.setItem(38, addKey);
			myInventory.setItem(39, addTempKey);
			// ----------------------- PREFIX/SUFFIX ------------------------------
	
			// ----------------------- GENERAL ------------------------------
			ItemStack general = Tools.button(Material.SIGN, "&6General", Arrays.asList("&e----------->"), 1);
			ItemStack weight1 = Tools.button(Material.DIAMOND_BLOCK, "&6Set weight", Arrays.asList("&eClick to set group weight", "&cCurrent: &e"+weight), 1);
			ItemStack displayname = Tools.button(Material.NAME_TAG, "&6Set displayname", Arrays.asList("&eClick to set display name", "&cCurrent: &e"+group.getFriendlyName()), 1);
			ItemStack rename = Tools.button(Material.NAME_TAG, "&6Rename", Arrays.asList("&eClick to rename", "&cCurrent: &e"+group.getName()), 1);
			ItemStack clone = Tools.button(Material.PAPER, "&6Clone", Arrays.asList("&eClick to clone"), 1);
			ItemStack remove = Tools.button(Material.REDSTONE, "&6Remove group", Arrays.asList("&eRemove the group"), 1);
			myInventory.setItem(45, general);
			myInventory.setItem(46, weight1);
			myInventory.setItem(47, displayname);
			myInventory.setItem(48, rename);
			myInventory.setItem(49, clone);
			myInventory.setItem(50, remove);
			// ----------------------- GENERAL ------------------------------
	
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
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms group")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						
						String group = ChatColor.stripColor(inv.getItem(4).getItemMeta().getLore().get(0).split(" ")[2]);
						Group g = LuckPerms.getApi().getGroup(group);
						
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Back")) {
							GroupsGUI.open(p);
						} else if (name.equals("All permissions")) {
							Permissions.open(p, g, 0);
						} else if (name.equals("Add permission")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Permission&8> [&7Value&8] [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPermission - Your permission");
							Tools.sendMessage(p, "&aValue - true/false");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Permissions.addPermission.put(p, g);
							p.closeInventory();
						} else if (name.equals("Add temp permission")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Permission&8> [&7Value&8] <&7Duration&8> [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPermission - Your permission");
							Tools.sendMessage(p, "&aValue - true/false");
							Tools.sendMessage(p, "&aDuration - time <s, m, h, d, mon, y>");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Permissions.addTempPermission.put(p, g);
							p.closeInventory();
						} else if (name.equals("Check if group has permission")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Permission&8> [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPermission - Your permission");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Permissions.checkIfHas.put(p, g);
							p.closeInventory();
						}
						
						else if (name.equals("All parents")) {
							Parents.open(p, g, 0);
						} else if (name.equals("Add parent")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Parent&8> [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aParent - parent group");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Parents.addParent.put(p, g);
							p.closeInventory();
						} else if (name.equals("Add temp parent")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Parent&8> <&7Duration&8> [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aParent - parent group");
							Tools.sendMessage(p, "&aDuration - time <s, m, h, d, mon, y>");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Parents.addTempParent.put(p, g);
							p.closeInventory();
						}
						
						else if (name.equals("All prefixes")) {
							Prefix.open(p, g, 0);
						} else if (name.equals("Add prefix")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Priority&8> \"<&7Prefix&8>\" [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPriority - prefix priority, if user has two groups, the higher priority prefix will be set");
							Tools.sendMessage(p, "&aPrefix - own prefix");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Prefix.addPrefix.put(p, g);
							p.closeInventory();
						} else if (name.equals("Add temp prefix")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Priority&8> \"<&7Prefix&8>\" <&7Duration&8> [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPriority - prefix priority, if user has two groups, the higher priority prefix will be set");
							Tools.sendMessage(p, "&aPrefix - own prefix");
							Tools.sendMessage(p, "&aDuration - time <s, m, h, d, mon, y>");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Prefix.addTempPrefix.put(p, g);
							p.closeInventory();
						}
						
						else if (name.equals("All suffixes")) {
							Suffix.open(p, g, 0);
						} else if (name.equals("Add suffix")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Priority&8> \"<&7Suffix&8>\" [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPriority - suffix priority, if user has two groups, the higher priority suffix will be set");
							Tools.sendMessage(p, "&aSuffix - own suffix");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Suffix.addPrefix.put(p, g);
							p.closeInventory();
						} else if (name.equals("Add temp suffix")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Priority&8> \"<&7Suffix&8>\" <&7Duration&8> [&7Server&8] [&7World&8]");
							Tools.sendMessage(p, "&aPriority - suffix priority, if user has two groups, the higher priority suffix will be set");
							Tools.sendMessage(p, "&aSuffix - own suffix");
							Tools.sendMessage(p, "&aDuration - time <s, m, h, d, mon, y>");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Suffix.addTempPrefix.put(p, g);
							p.closeInventory();
						}
						

						else if (name.equals("Set weight")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Weight&8>");
							Tools.sendMessage(p, "&aWeight - group weight");
							setWeight.put(p, g);
							p.closeInventory();
						} else if (name.equals("Set displayname")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - group display name");
							setName.put(p, g);
							p.closeInventory();
						} else if (name.equals("Rename")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - group new name");
							rename.put(p, g);
							p.closeInventory();
						} else if (name.equals("Clone")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - new group new name");
							clone.put(p, g);
							p.closeInventory();
						} else if (name.equals("Remove group")) {
							Tools.sendCommand(p, "lp deletegroup "+g.getName());
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
								GroupsGUI.open(p);
							}, 3);
						}
					}
			}
	}
}
