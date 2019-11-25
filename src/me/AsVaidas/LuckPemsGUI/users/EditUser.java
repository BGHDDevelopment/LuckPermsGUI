package me.AsVaidas.LuckPemsGUI.users;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
import me.AsVaidas.LuckPemsGUI.users.Permissions;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import me.lucko.luckperms.api.User;

public class EditUser implements Listener {

	public static Map<Player, User> primarygroup = new HashMap<Player, User>();
	public static Map<Player, User> clone = new HashMap<Player, User>();
	public static Map<Player, User> promote = new HashMap<Player, User>();
	public static Map<Player, User> demote = new HashMap<Player, User>();
	static LuckPermsApi l = LuckPerms.getApi();

	
	@EventHandler
	public void onswitchPrimaryGroup(AsyncPlayerChatEvent e) {
		if (!primarygroup.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		User g = primarygroup.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp user "+g.getName()+" parent set "+message);
		primarygroup.remove(e.getPlayer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}

	@EventHandler
	public void onClone(AsyncPlayerChatEvent e) {
		if (!clone.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		User g = clone.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp user "+g.getName()+" clone "+message);
		clone.remove(e.getPlayer());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), l.getUser(message));
		}, 3);
		e.setCancelled(true);
	}

	@EventHandler
	public void onPromote(AsyncPlayerChatEvent e) {
		if (!promote.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		User g = promote.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp user "+g.getName()+" promote "+message);
		promote.remove(e.getPlayer());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}


	@EventHandler
	public void onDemote(AsyncPlayerChatEvent e) {
		if (!demote.containsKey(e.getPlayer())) return;
		String message = e.getMessage();
		User g = demote.get(e.getPlayer());
		
		Tools.sendCommand(e.getPlayer(), "lp user "+g.getName()+" demote "+message);
		demote.remove(e.getPlayer());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			open(e.getPlayer(), g);
		}, 3);
		e.setCancelled(true);
	}

	public static void open(Player p, User user) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA +"LuckPerms user");
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
	
			
			// ----------------------- PERMISSION ------------------------------
			ItemStack perm = Tools.button(Material.SIGN, "&6Permissions", Arrays.asList("&e----------->",
					"&aDescription:",
					"&2   Permission nodes are a method of defining",
					"&2   the access each player has on a server,",
					"&2   in the form of a name and a true/false state."), 1);
			ItemStack pall = Tools.button(Material.CHEST, "&6All permissions", Arrays.asList("&eClick here to see all permissions"), 1);
			ItemStack pset = Tools.button(Material.ARROW, "&6Add permission", Arrays.asList("&eClick to add new permission"), 1);
			ItemStack psettemp = Tools.button(Material.ARROW, "&6Add temp permission", Arrays.asList("&eClick to add new temporary permission"), 1);
			ItemStack pcheck = Tools.button(Material.TORCH, "&6Check if user has permission", Arrays.asList("&eClick to check if user has permission"), 1);
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
			
	
			// ----------------------- META ------------------------------
			ItemStack meta = Tools.button(Material.SIGN, "&6Meta", Arrays.asList("&e----------->",
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
			// ----------------------- META ------------------------------
	
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
			ItemStack primarygroup = Tools.button(Material.DIAMOND_BLOCK, "&6Change primary group", Arrays.asList("&eClick to set primary group", "&cCurrent: &e"+user.getPrimaryGroup()), 1);
			ItemStack clone = Tools.button(Material.PAPER, "&6Clone", Arrays.asList("&eClick to clone"), 1);
			ItemStack promote = Tools.button(Material.ARROW, "&6Promote", Arrays.asList("&eClick to promote"), 1);
			ItemStack demote = Tools.button(Material.TRIPWIRE_HOOK, "&6Demote", Arrays.asList("&eClick to demote"), 1);
			myInventory.setItem(45, general);
			myInventory.setItem(46, primarygroup);
			myInventory.setItem(47, clone);
			myInventory.setItem(48, promote);
			myInventory.setItem(49, demote);
			// ----------------------- GENERAL ------------------------------
	
			ItemStack back = Tools.button(Material.BARRIER, "&6Back", Arrays.asList(""), 1);
			myInventory.setItem(8, back);
			
		});
		p.openInventory(myInventory);
	}
	

	public static void openTracks(Player p, User user, String type) {
		Inventory myInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA +"Select " + type); //+type+" "+user.getName()+" track");
		Tools.onAsync(() -> {
			ItemStack back = Tools.button(Material.BARRIER, "&6Back", Arrays.asList(""), 1);
			myInventory.setItem(8, back);
			
			int i = 9;
			for (Track track : l.getTracks()) {

				ItemStack item = Tools.button(Material.TNT,
						"&6"+track.getName(),
						Arrays.asList(
								"&cGroups: &e"+track.getSize()
								), 1);
				myInventory.setItem(i, item);
				
				i++;
			}
		});
		p.openInventory(myInventory);
	}
	
	@EventHandler
	public void onInventoryPromoteClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().contains(ChatColor.AQUA + "Select")) {
				e.setCancelled(true);
				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
					String group = ChatColor.stripColor(inv.getName().split(" ")[0]); //default 2
					User g = LuckPerms.getApi().getUser(group);
					String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
					if (name.equals("Back")) {
						//open(p, g);
						UsersGUI.open(p);
					} else {
							String type = inv.getName().split(" ")[1];
							if (type.equalsIgnoreCase("promote")) {
								Tools.sendCommand(p, "lp user "+g.getName()+" promote "+name);
							} else if (type.equalsIgnoreCase("demote")) {
								Tools.sendCommand(p, "lp user "+g.getName()+" demote "+name);
							}
						}
					}
			}
	}

	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv != null && item != null)
			if (e.getView().getTitle().equals(ChatColor.AQUA+"LuckPerms user")) {
				e.setCancelled(true);
				if (item.hasItemMeta())
					if (item.getItemMeta().hasDisplayName()) {
						
						String group = ChatColor.stripColor(inv.getItem(4).getItemMeta().getLore().get(0).split(" ")[1]);
						User g = LuckPerms.getApi().getUser(group);
						
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						if (name.equals("Back")) {
							UsersGUI.open(p);
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
						} else if (name.equals("Check if user has permission")) {
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
							Tools.sendMessage(p, "&aPriority - prefix priority");
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
							Tools.sendMessage(p, "&aPriority - prefix priority");
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
							Tools.sendMessage(p, "&aPriority - suffix priority");
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
							Tools.sendMessage(p, "&aPriority - suffix priority");
							Tools.sendMessage(p, "&aSuffix - own suffix");
							Tools.sendMessage(p, "&aDuration - time <s, m, h, d, mon, y>");
							Tools.sendMessage(p, "&aServer - server that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Tools.sendMessage(p, "&aWorld - world that permission will work");
							Tools.sendMessage(p, "    &2Leave blank or 'global' for global");
							Suffix.addTempPrefix.put(p, g);
							p.closeInventory();
						}
						

						else if (name.equals("Change primary group")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Group&8>");
							Tools.sendMessage(p, "&aGroup - the group to set as primary");
							primarygroup.put(p, g);
							p.closeInventory();
						} else if (name.equals("Clone")) {
							Tools.sendMessage(p, "&eWrite in chat:");
							Tools.sendMessage(p, "&8<&7Name&8>");
							Tools.sendMessage(p, "&aName - user name/UUID to clone onto");
							clone.put(p, g);
							p.closeInventory();
						} else if (name.equals("Promote")) {
							openTracks(p, g, "Promote");
						} else if (name.equals("Demote")) {
							openTracks(p, g, "Demote");
						}
					}
			}
	}

	@SuppressWarnings("deprecation")
	public static void open(Player player, String message) {
		if (!Tools.isOnline(message)) {
			UUID uuid = Bukkit.getOfflinePlayer(message).getUniqueId();
			try {
				open(player, l.getUserManager().loadUser(uuid).get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			open(player, l.getUser(message));
	}
	
}
