package za.co.wihan.blocksToCommands.commandHelper;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ReplaceVariables {

    public String getVariables(String command, Player player, Location location) {


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

        valuesMap.put("blockX", location.getBlockX() + "");
        valuesMap.put("blockY", location.getBlockY() + "");
        valuesMap.put("blockZ", location.getBlockZ() + "");

        valuesMap.put("world", location.getWorld().getName() + "");

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        return sub.replace(command);
    }
}
