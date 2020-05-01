package me.ILoveAMac.BTC.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VariableReplacer {
	
	public static String replaceVars(String inputStr, Player player, Location blockLocation) {
        Map<String, String> valuesMap = new HashMap<String, String>();

        // Setup variables to be replaced
        valuesMap.put("name", player.getName());
        valuesMap.put("displayName", player.getDisplayName());
        valuesMap.put("uuid", player.getUniqueId() + "");
        String ip = player.getAddress() + "";
        valuesMap.put("ip", ip.replace("/", ""));

        valuesMap.put("near", "@p");

        Player randomPlayer = Bukkit.getOnlinePlayers().iterator().next();
        valuesMap.put("randomPlayer", randomPlayer.getName() + "");


        valuesMap.put("x", player.getLocation().getX() + "");
        valuesMap.put("y", player.getLocation().getY() + "");
        valuesMap.put("z", player.getLocation().getZ() + "");

        valuesMap.put("blockX", blockLocation.getBlockX() + "");
        valuesMap.put("blockY", blockLocation.getBlockY() + "");
        valuesMap.put("blockZ", blockLocation.getBlockZ() + "");
        valuesMap.put("world", blockLocation.getWorld().getName() + "");

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        return sub.replace(inputStr);
	}
	
	public static String replaceConfigVars(String message, float cost) {
        ConfigManager configManager = ConfigManager.getInstance();
		
        String currency = (String) configManager.get("currency");
        
		Map<String, String> valuesMap = new HashMap<String, String>();
		
        valuesMap.put("currency", currency);
        valuesMap.put("cost", cost + "");
        
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        
        return sub.replace(message);
	}
	
}
