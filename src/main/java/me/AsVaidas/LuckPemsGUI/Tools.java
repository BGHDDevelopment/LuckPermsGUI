/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.lucko.luckperms.api.context.ContextSet;

public class Tools {

	public static ItemStack button(Material material, String pavadinimas, List<String> lore, int amount) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pavadinimas));
		meta.setLore(ctranslate(lore));
		item.setItemMeta(meta);
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack head(String owner, String pavadinimas, List<String> lore, int amount) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, amount);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pavadinimas));
		meta.setLore(ctranslate(lore));
		item.setItemMeta(meta);
		return item;
	}
	
	private static List<String> ctranslate(List<String> lore) {
		List<String> lore2 = new ArrayList<String>();
		for (String eilute : lore)
			lore2.add(ChatColor.translateAlternateColorCodes('&', eilute));
		return lore2;
	}

	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static void sendMessage(Player p, String message) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void debug(String key) {
		Bukkit.getPlayer("Noodles_YT").sendMessage(key);
	}
	
	public static void onAsync(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), runnable);
	}
	
	public static void sendCommand(Player p, String command) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			Bukkit.dispatchCommand(p, command);
		});
	}
	
	public static String contextConverter(ContextSet contextSet) {
		String eilute = "";
		
		Iterator<Entry<String, String>> set = contextSet.toMultimap().entries().iterator();
		while (set.hasNext()) {
			Entry<String, String> next = set.next();
			
			if (eilute.equals(""))
				eilute = next.getKey()+"="+next.getValue();
			else
				eilute += " "+next.getKey()+"="+next.getValue();
		}
		return eilute;
	}
	
	public static String getTime(long time) {
		time = time - System.currentTimeMillis();
		
	    long tempSec = time/(1000);
	    long min = (tempSec /60) % 60;
	    long hour = (tempSec /(60*60)) % 24;
	    long temphour = (tempSec /(60*60));
	    int days = 0;
	    while (temphour >= 24) {
	    	days++;
	    	temphour -= 24;
	    }
	    
	    return days+"d. "+hour+"h. "+min+"min.";
	}

	public static boolean isOnline(String name) {
		for (Player p : Bukkit.getOnlinePlayers())
			if (p.getName().equals(name))
				return true;
		return false;
	}

	public static void sendConsole(String string) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
		});
	}
}
