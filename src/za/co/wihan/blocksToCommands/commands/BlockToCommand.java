package za.co.wihan.blocksToCommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import za.co.wihan.blocksToCommands.Config.CreateEntry;
import za.co.wihan.blocksToCommands.Config.MainConfigManager;
import za.co.wihan.blocksToCommands.Config.RemoveEntry;
import za.co.wihan.blocksToCommands.commandHelper.CheckIfValid;
import za.co.wihan.blocksToCommands.economy.Cost;

import java.util.Set;

import static za.co.wihan.blocksToCommands.blocksToCommands.getBlocks;


public class BlockToCommand implements CommandExecutor {

    private CheckIfValid check = new CheckIfValid();
    private CreateEntry make = new CreateEntry();
    private RemoveEntry remove = new RemoveEntry();
    private Cost cost = new Cost();

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (commandLabel.equalsIgnoreCase("btc") && args.length >= 4) {
            if (check.validateCommand(sender)) {
                Player player = (Player) sender;

                if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.create")) && args[1].equalsIgnoreCase("create")) {
                    make.createEntry(player, args);
                } else {
                    if (!player.hasPermission((String) MainConfigManager.getInstance().get("permissions.create")) || !player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove"))) {
                        String message = (String) MainConfigManager.getInstance().get("messages.noPermsGeneric");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

                    } else if (commandLabel.equalsIgnoreCase("btc") && args.length == 4) {
                        if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.cost")) && args[1].equalsIgnoreCase("cost")) {
                            if (args[2].equalsIgnoreCase("add")) {
                                try {
                                    cost.addCost(Double.parseDouble(args[3]), player, args[0]);
                                } catch (NumberFormatException ex) {
                                    player.sendMessage(ChatColor.RED + "[BTC] \"" + args[3] + "\" is not a valid number!");
                                }
                            }
                        } else if (!player.hasPermission((String) MainConfigManager.getInstance().get("permissions.cost"))) {
                            String message = (String) MainConfigManager.getInstance().get("messages.noPermsGeneric");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        } else {
                            player.sendMessage(ChatColor.RED + "[BTC] Invalid arguments!");
                            invalidArgs(player);
                        }

                    } else {
                        player.sendMessage(ChatColor.RED + "[BTC] Invalid arguments!");
                        invalidArgs(player);
                    }
                }
                return true;
            }
        } else if (commandLabel.equalsIgnoreCase("btc") && args.length == 3 && check.validateCommand(sender)) {
            Player player = (Player) sender;
            if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.cost")) && args[1].equalsIgnoreCase("cost")) {
                if (args[2].equalsIgnoreCase("remove")) {
                    cost.removeCost(player, args[0]);
                } else if (!player.hasPermission((String) MainConfigManager.getInstance().get("permissions.cost"))) {
                    String message = (String) MainConfigManager.getInstance().get("messages.noPermsGeneric");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.cost"))) {
                invalidArgs(player);
            } else {
                String message = (String) MainConfigManager.getInstance().get("messages.noPermsGeneric");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }

        } else if (commandLabel.equalsIgnoreCase("btc") && args.length == 1) {
            if (check.validateCommand(sender)) {
                Player player = (Player) sender;
                try {
                    if (args[0].equalsIgnoreCase("list") && player.hasPermission((String) MainConfigManager.getInstance().get("permissions.list"))) {
                        Set<String> arg = getBlocks().getMainKeys();
                        String[] nodes = arg.toArray(new String[0]);
                        if (nodes.length != 0) {
                            player.sendMessage(ChatColor.GREEN + "[BTC] List of blocks:");
                            for (String node : nodes) {
                                player.sendMessage(ChatColor.AQUA + (getBlocks().get(node + ".Name") + ""));
                            }
                        } else {
                            player.sendMessage(ChatColor.AQUA + "[BTC] There are no blocks in blocks.yml");
                        }
                    } else if (args[0].equalsIgnoreCase("reload") && player.hasPermission((String) MainConfigManager.getInstance().get("permissions.reload"))) {
                        try {
                            getBlocks().reloadConfigs();
                            MainConfigManager.getInstance().reloadMainConfig();
                            sender.sendMessage(ChatColor.GREEN + "[BTC] The config files have been reloaded!");
                        } catch (Exception ex) {
                            sender.sendMessage(ChatColor.RED + "[BTC] An error occurred while trying to reload the config files...");
                            sender.sendMessage(ex.toString());
                        }

                    } else if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.list")) || player.hasPermission((String) MainConfigManager.getInstance().get("permissions.reload"))) {
                        player.sendMessage(ChatColor.RED + "[BTC] Invalid arguments!");
                        invalidArgs(player);
                    } else {
                        String message = (String) MainConfigManager.getInstance().get("messages.noPermsGeneric");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                } catch (Exception ex) {
                    player.sendMessage(ex.toString());
                }
            } else {
                try {
                    if (args[0].equalsIgnoreCase("reload")) {
                        getBlocks().reloadConfigs();
                        MainConfigManager.getInstance().reloadMainConfig();
                        sender.sendMessage(ChatColor.GREEN + "[BTC] The config files have been reloaded!");
                    }
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "[BTC] An error occurred while trying to reload the config files...");
                    sender.sendMessage(ex.toString());
                }
            }

        } else if ((commandLabel.equalsIgnoreCase("btc") && args.length == 2 && check.validateCommand(sender))) {
            Player player = (Player) sender;
            if (player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove")) && args[1].equalsIgnoreCase("remove")) {
                remove.removeEntry(player, args);
            } else {
                if (!player.hasPermission((String) MainConfigManager.getInstance().get("permissions.remove"))) {
                    String message = (String) MainConfigManager.getInstance().get("messages.noPermsRemove");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                } else {
                    player.sendMessage(ChatColor.RED + "[BTC] Invalid arguments!");
                    player.sendMessage(ChatColor.GREEN + "List of blocks:");
                    invalidArgs(player);
                }
                return true;
            }
        } else if (commandLabel.equalsIgnoreCase("btc") && !(args.length >= 4) && check.validateCommand(sender)) {
            Player player = (Player) sender;
            if (!player.hasPermission((String) MainConfigManager.getInstance().get("permissions.create"))) {
                String message = (String) MainConfigManager.getInstance().get("messages.noPermsCreate");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            } else {
                invalidArgs(player);
            }
            return true;

        }
        return false;
    }

    private void invalidArgs(Player player) {
        player.sendMessage(ChatColor.GREEN + "/btc " + ChatColor.RED + "{name} {create} {runFromConsole} {command}");
        player.sendMessage(ChatColor.GREEN + "/btc " + ChatColor.RED + "{name} {remove}");
        player.sendMessage(ChatColor.GREEN + "/btc " + ChatColor.RED + "{name} cost add {amount}");
        player.sendMessage(ChatColor.GREEN + "/btc " + ChatColor.RED + "{name} cost remove");
        player.sendMessage(ChatColor.GREEN + "/btc " + ChatColor.RED + "list");
        player.sendMessage(ChatColor.GREEN + "/btc " + ChatColor.RED + "reload");
    }
}
