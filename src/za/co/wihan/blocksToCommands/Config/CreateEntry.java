package za.co.wihan.blocksToCommands.Config;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import za.co.wihan.blocksToCommands.arrayHelper.ArrayManipulation;

import java.util.Set;

import static za.co.wihan.blocksToCommands.blocksToCommands.getBlocks;

public class CreateEntry {

    // Create new instance of ArrayManipulation class
    ArrayManipulation manipulate = new ArrayManipulation();

    public void createEntry(Player player, String[] args) {

        // Prevent duplicate
        if (getBlocks().contains(args[0])) {
            String message = (String) MainConfigManager.getInstance().get("messages.nameAlreadySet");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            // Check if true / false value is given
            if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                // Get Material and location of the block the player is looking
                // at
                Block block = player.getTargetBlock(null, 4);
                Location location = block.getLocation();

                // Check if block is not air
                if (block.getType() != Material.AIR) {
                    // Get values from command
                    String name = args[0];
                    String command = manipulate.combineArgs(args, 3);
                    boolean runFromConsole = args[2].equalsIgnoreCase("true");
                    String world = location.getWorld().getName();

                    // Get block location
                    int x = location.getBlockX();
                    int y = location.getBlockY();
                    int z = location.getBlockZ();

                    // Write to config
                    getBlocks().set(name + ".Name", args[0]);
                    getBlocks().set(name + ".Command", command);
                    getBlocks().set(name + ".runFromConsole", runFromConsole);
                    getBlocks().set(name + ".World", world);
                    getBlocks().set(name + ".X", x);
                    getBlocks().set(name + ".Y", y);
                    getBlocks().set(name + ".Z", z);
                    getBlocks().set(name + ".Cost", "0");

                    // Save config
                    getBlocks().save();

                    // Success!
                    player.sendMessage(ChatColor.GREEN + "[BTC] Added new block command @" + x + "," + y + "," + z + " Command to run: " + ChatColor.AQUA + command + ChatColor.GREEN + " Run from console: " + ChatColor.AQUA + runFromConsole + ChatColor.GREEN + ", name: " + ChatColor.AQUA + name + ChatColor.GREEN + ", in world: " + ChatColor.AQUA + world);

                    // If block is air, do nothing and prompt
                } else {
                    player.sendMessage(ChatColor.RED + "[BTC] You can't set a command on a" + ChatColor.BOLD + " AIR " + ChatColor.RESET + ChatColor.RED + "block, look at the block you want to select!");
                }

            } else {
                // No true / false value for runFromConsole
                String message = (String) MainConfigManager.getInstance().get("messages.noRunFromConsole");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }

    }

}
