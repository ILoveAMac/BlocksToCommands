package za.co.wihan.blocksToCommands.Config;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import static za.co.wihan.blocksToCommands.blocksToCommands.getBlocks;


public class RemoveEntry {


    public void removeEntry(Player player, String[] args) {

        try {
            if (getBlocks().contains(args[0])) {
                // Get information from config
                int x = (int) getBlocks().get(args[0] + ".X");
                int y = (int) getBlocks().get(args[0] + ".Y");
                int z = (int) getBlocks().get(args[0] + ".Z");
                World world = Bukkit.getWorld(getBlocks().get(args[0] + ".World") + ""); /* + "" to convert to string */

                // Get information from block in config
                Location location = new Location(world, x, y, z);
                Block block = location.getBlock();
                // "delete" block
                block.setType(Material.AIR);

                // Write to config & save
                getBlocks().set(args[0], null);
                getBlocks().save();

                // Success
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                player.sendMessage(ChatColor.GREEN + "[BTC] The block was removed!");

            } else {

                String message = (String) MainConfigManager.getInstance().get("messages.notExist");

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }

        } catch (Exception ex) {
            String message = ChatColor.RED + "[BTC] There is a problem in the blocks.yml file! To remove this block delete blocks.yml / fix the problem in blocks.yml";
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

    }

    public void removeEntryShift(Player player, String name) {

        try {
            if (getBlocks().contains(name)) {
                // Get information from config
                int x = (int) getBlocks().get(name + ".X");
                int y = (int) getBlocks().get(name + ".Y");
                int z = (int) getBlocks().get(name + ".Z");
                World world = Bukkit.getWorld(getBlocks().get(name + ".World") + ""); /* + "" to convert to string */

                // Get information from block in config
                Location location = new Location(world, x, y, z);
                Block block = location.getBlock();
                // "delete" block
                block.setType(Material.AIR);

                // Write to config & save
                getBlocks().set(name, null);
                getBlocks().save();

                // Success
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                player.sendMessage(ChatColor.GREEN + "[BTC] The block was removed!");

            } else {

                String message = (String) MainConfigManager.getInstance().get("messages.notExist");

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }

        } catch (Exception ex) {
            String message = ChatColor.RED + "[BTC] There is a problem in the blocks.yml file! To remove this block delete blocks.yml / fix the problem in blocks.yml";
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

    }

}
