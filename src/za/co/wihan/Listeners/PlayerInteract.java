package za.co.wihan.Listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import za.co.wihan.blocksToCommands.Config.MainConfigManager;
import za.co.wihan.blocksToCommands.Config.RemoveEntry;
import za.co.wihan.blocksToCommands.blocksToCommands;
import za.co.wihan.blocksToCommands.commandHelper.ReplaceVariables;
import za.co.wihan.blocksToCommands.economy.Cost;

import java.util.Set;

import static za.co.wihan.blocksToCommands.blocksToCommands.getBlocks;


public class PlayerInteract implements Listener {

    public static blocksToCommands plugin;

    private RemoveEntry remove = new RemoveEntry();
    private ReplaceVariables replaceVars = new ReplaceVariables();
    private Cost cost = new Cost();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.ACACIA_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.BIRCH_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.DARK_OAK_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.JUNGLE_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.OAK_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.SPRUCE_PRESSURE_PLATE
                    || event.getClickedBlock().getType() == Material.TRIPWIRE) {
                return;
            }
        }


        // Prevent event from off-hand
        EquipmentSlot e = event.getHand();

        if (e.equals(EquipmentSlot.HAND) && event.getClickedBlock() != null) {
            try {
                int x = event.getClickedBlock().getLocation().getBlockX();
                int y = event.getClickedBlock().getLocation().getBlockY();
                int z = event.getClickedBlock().getLocation().getBlockZ();
                Location location = event.getClickedBlock().getLocation();


                String world = location.getWorld().getName();

                Player player = event.getPlayer();
                Set<String> arg = getBlocks().getMainKeys();
                String[] nodes = arg.toArray(new String[0]);

                if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.use"))) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        for (int i = 0; i < nodes.length; i++) {

                            if (x == Integer.parseInt(getBlocks().get(nodes[i] + ".X") + "") && y == Integer.parseInt(getBlocks().get(nodes[i] + ".Y") + "") && z == Integer.parseInt(getBlocks().get(nodes[i] + ".Z") + "") && world.equalsIgnoreCase(getBlocks().get(nodes[i] + ".World") + "")) {
                                event.setCancelled(true);
                                if (cost.applyCost(player, getBlocks().get(nodes[i] + ".Name") + "")) {
                                	if (MainConfigManager.getInstance().get("options.soundOnClick").equals(true)) {
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                                	}                               
                                    String runFromConsole = getBlocks().get(nodes[i] + ".runFromConsole") + "";
                                    if (runFromConsole.equalsIgnoreCase("false")) {
                                        String command = replaceVars.getVariables(getBlocks().get(nodes[i] + ".Command") + "", player, location);
                                        player.performCommand(command);
                                    } else {
                                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                        String command = replaceVars.getVariables(getBlocks().get(nodes[i] + ".Command") + "", player, location);
                                        Bukkit.dispatchCommand(console, command);
                                    }
                                }
                            }
                        }
                    } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        if (event.getClickedBlock().getType() != Material.AIR)
                            if (!player.isSneaking()) {
                                for (int i = 0; i < nodes.length; i++) {
                                    if (x == Integer.parseInt(getBlocks().get(nodes[i] + ".X") + "") && y == Integer.parseInt(getBlocks().get(nodes[i] + ".Y") + "") && z == Integer.parseInt(getBlocks().get(nodes[i] + ".Z") + "") && world.equalsIgnoreCase(getBlocks().get(nodes[i] + ".World") + "")) {
                                        if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove"))) {
                                            event.setCancelled(true);
                                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                                            player.sendMessage(ChatColor.RED + "[BTC] Please delete the block before breaking it");
                                        } else {
                                            event.setCancelled(true);
                                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                                            String message = (String) MainConfigManager.getInstance().get("messages.noBreak");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                }
                            } else if (player.isSneaking() && player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove")) && MainConfigManager.getInstance().get("options.shiftRemove").toString().equalsIgnoreCase("true")) {

                                for (int i = 0; i < nodes.length; i++) {
                                    if (x == Integer.parseInt(getBlocks().get(nodes[i] + ".X") + "") && y == Integer.parseInt(getBlocks().get(nodes[i] + ".Y") + "") && z == Integer.parseInt(getBlocks().get(nodes[i] + ".Z") + "") && world.equalsIgnoreCase(getBlocks().get(nodes[i] + ".World") + "")) {
                                        String name = getBlocks().get(nodes[i] + ".Name") + "";
                                        remove.removeEntryShift(player, name);
                                    }
                                }
                            } else if (player.isSneaking() && !player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove"))) {
                                for (int i = 0; i < nodes.length; i++) {
                                    if (x == Integer.parseInt(getBlocks().get(nodes[i] + ".X") + "") && y == Integer.parseInt(getBlocks().get(nodes[i] + ".Y") + "") && z == Integer.parseInt(getBlocks().get(nodes[i] + ".Z") + "") && world.equalsIgnoreCase(getBlocks().get(nodes[i] + ".World") + "")) {
                                        String message = (String) MainConfigManager.getInstance().get("messages.noBreak");
                                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                                        //player.sendMessage(x + (getBlocks().get(nodes[i] + ".X") + ""));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        event.setCancelled(true);
                                    }
                                }
                            } /*else {
                            event.setCancelled(true);
                            if (player.hasPermission("permissions.remove")) {
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                                player.sendMessage(ChatColor.RED + "[BTC] This block can't be broken by hand, change this in config.yml");
                            } else {
                                String message = (String) MainConfigManager.getInstance().get("messages.noBreak");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            }
                        }*/
                    }
                } else {
                    for (int i = 0; i < nodes.length; i++) {
                        if (x == Integer.parseInt(getBlocks().get(nodes[i] + ".X") + "") && y == Integer.parseInt(getBlocks().get(nodes[i] + ".Y") + "") && z == Integer.parseInt(getBlocks().get(nodes[i] + ".Z") + "") && world.equalsIgnoreCase(getBlocks().get(nodes[i] + ".World") + "") && !player.hasPermission((String) MainConfigManager.getInstance().get("permissions.use"))) {
                            String message = (String) MainConfigManager.getInstance().get("messages.noPermsUse");
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            //player.sendMessage(x + (getBlocks().get(nodes[i] + ".X") + ""));
                            event.setCancelled(true);
                        }
                    }
                }
            } catch (Exception ex) {
                Player player = event.getPlayer();
                if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.use")) && !player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove"))) {
                    String message = (String) MainConfigManager.getInstance().get("messages.clickException");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                } else if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove"))) {
                    String message = ChatColor.RED + "[BTC] There is a problem in the blocks.yml file! To remove this block delete blocks.yml / fix the problem in blocks.yml";
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message + " " + ex));
                }
                event.setCancelled(true);
            }
        }
    }
}
