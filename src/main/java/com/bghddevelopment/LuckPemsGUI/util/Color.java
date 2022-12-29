package com.bghddevelopment.LuckPemsGUI.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Color {

    public static String translate(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
