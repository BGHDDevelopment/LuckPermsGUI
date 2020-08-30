/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.util.updatechecker;

import me.AsVaidas.LuckPemsGUI.Main;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker
{
    private Main plugin;
    private int resourceId;

    public UpdateChecker(Main plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

   public void getLatestVersion(Consumer<String> consumer) {
       Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
           try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
               if (scanner.hasNext()) {
                   consumer.accept(scanner.next());
               }
           } catch (IOException exception) {
               this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
           }
       });
   }
}

