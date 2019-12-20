/*
 * Copyright (c) BGHDDevelopment.
 * Please refer to the plugin page or GitHub page for our open-source license.
 * If you have any questions please email ceo@bghddevelopment or reach us on Discord
 */

package me.AsVaidas.LuckPemsGUI.updatechecker;

import me.AsVaidas.LuckPemsGUI.Main;
import me.AsVaidas.LuckPemsGUI.util.Settings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker
{
    public Main plugin;
    public String version;
    
    public UpdateChecker(Main plugin) {
        this.plugin = plugin;
        this.version = this.getLatestVersion();
    }
    
    @SuppressWarnings("unused")
	public String getLatestVersion() {
        try {
            final int resource = 53460;
            final HttpURLConnection con = (HttpURLConnection)new URL("https://api.spigotmc.org/legacy/update.php?resource=53460").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=53460".getBytes("UTF-8"));
            final String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 7) {
                return version;
            }
        }
        catch (Exception ex) {
            System.out.println("---------------------------------");
            this.plugin.getLogger().info("Failed to check for a update!");
            System.out.println("---------------------------------");
        }
        return null;
    }
    
    public boolean isConnected() {
        return this.version != null;
    }
    
    public boolean hasUpdate() {
        return !this.version.equals(Settings.VERSION);
    }
}
