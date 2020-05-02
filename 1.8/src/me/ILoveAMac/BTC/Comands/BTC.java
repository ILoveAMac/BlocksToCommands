package me.ILoveAMac.BTC.Comands;

import java.util.Set;

import me.ILoveAMac.BTC.util.BlockManager;
import me.ILoveAMac.BTC.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ILoveAMac.BTC.Main;
import me.ILoveAMac.BTC.util.CmdBlock;
import me.ILoveAMac.BTC.util.Messages;

public class BTC implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLable, String[] args) {

		// Player
		if (commandLable.equalsIgnoreCase("btc") && isCommandSenderPlayer(sender)) {
			// Player
			Player player = (Player) sender;
			Messages sendMsg = new Messages(player);

			// Does not have btc.admin
			if (!player.hasPermission("btc.admin")) {
				sendMsg.notAdmin();
				return true;
			}

			// No arguments
			if (args.length == 0) {
				sendMsg.invalidArgumentsGeneral();
				return true;

				// One Argument
			} else if (args.length == 1) {

				if (args[0].equalsIgnoreCase("help")) {
					sendMsg.helpMessage();
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					ConfigManager.getInstance().reloadConfig();
					sendMsg.reloadComplete();
					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					sendMsg.listBlocks();
					return true;
				} else {
					sendMsg.invalidArgumentsGeneral();
					return true;
				}

				// Two arguments
			} else if (args.length == 2) {

				if (args[0].equalsIgnoreCase("create")) {

					if (player.getTargetBlock((Set<Material>) null, 5).getType() == Material.AIR) {
						sendMsg.notLockingAtBlock();
						return true;
					}

					String blockName = args[1];
					Location target = player.getTargetBlock((Set<Material>) null, 5).getLocation();
					BlockManager manager = new BlockManager();

					// Duplicate prevention
					if (manager.doesBlockExist(blockName)) {
						sendMsg.nameInUse();
						return true;
					} else if (manager.isBlockAssigned(target)) {
						String name = manager.getBlockName(target);

						if (name == null) {
							sendMsg.unknownError();
						}

						sendMsg.nameAlreadyAssigned(name);
						return true;
					}

					CmdBlock cmdBlock = new CmdBlock(blockName, target, null);
					manager.CreateBlock(cmdBlock);

					sendMsg.creationConfirmation(blockName, target);

					return true;
				} else if (args[0].equalsIgnoreCase("delete")) {
					String blockName = args[1];

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(blockName)) {
						sendMsg.blockDoesNotExist(blockName);
						return true;
					}

					manager.deleteBlock(blockName);
					sendMsg.blockDeleted(blockName);
					return true;

				} else if (args[0].equalsIgnoreCase("help")) {

					int page;
					try {
						page = Integer.parseInt(args[1]);
					} catch (Exception e) {
						sendMsg.invalidArgumentsGeneral();
						return true;
					}

					sendMsg.helpMessage(page);
					return true;

				} else if (args[0].equalsIgnoreCase("list")) {
					int page;
					try {
						page = Integer.parseInt(args[1]);
					} catch (Exception e) {
						sendMsg.invalidIntegerInput(args[1]);
						return true;
					}

					sendMsg.listBlocks(page);
					return true;
				} else if (args[0].equalsIgnoreCase("tp")) {
					String blockName = args[1];

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(blockName)) {
						sendMsg.blockDoesNotExist(blockName);
						return true;
					}

					Location tpLocation = manager.getBlockLocation(blockName);

					tpLocation.add(0.500, 0, 0.500);
					tpLocation.setYaw(-90);
					tpLocation.setPitch(90);

					player.teleport(tpLocation);
					sendMsg.succesfullTp(blockName);
					return true;

				}

				sendMsg.invalidArgumentsGeneral();
				return true;

			} else if (args.length == 3) {
				// 3 Arguments
				if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("cost")) {

					BlockManager manager = new BlockManager();
					String blockName = args[2];
					if (!manager.doesBlockExist(blockName)) {
						sendMsg.blockDoesNotExist(blockName);
						return true;
					}

					manager.removeCost(blockName);
					sendMsg.costRemoved();
					return true;

				} else if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("cooldown")) {
					
					BlockManager manager = new BlockManager();
					String blockName = args[2];
					if (!manager.doesBlockExist(blockName)) {
						sendMsg.blockDoesNotExist(blockName);
						return true;
					}

					manager.removeCooldown(blockName);
					sendMsg.cooldownRemoved();
					return true;
					
					
				} else if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("perm")) {

					BlockManager manager = new BlockManager();
					String blockName = args[2];
					if (!manager.doesBlockExist(blockName)) {
						sendMsg.blockDoesNotExist(blockName);
						return true;
					}

					manager.removePerm(blockName);
					sendMsg.removePerm();
					return true;

				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("msg")) {
					// btc info msg <name>

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];

					sendMsg.listMessages(blockName, 1);
					return true;

				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("cmd")) {
					// btc info cmd <name>

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];

					sendMsg.listCommands(blockName, 1);
					return true;

				} else if (args[0].equalsIgnoreCase("info")
						&& (args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc"))) {
					BlockManager manager = new BlockManager();

					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];

					sendMsg.locationInfo(blockName);
					return true;
				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("perm")) {
					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];
					sendMsg.permValue(blockName);
					return true;

				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("cost")) {

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];
					sendMsg.costValue(blockName);
					return true;

				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("cooldown")) {
					
					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];
					sendMsg.cooldownValue(blockName);
					return true;
					
				} else if (args[0].equalsIgnoreCase("clone")) {
					// btc clone nameToClone newName

					String nameToClone = args[1];
					String newName = args[2];

					BlockManager manager = new BlockManager();

					// Make sure original exists
					if (!manager.doesBlockExist(nameToClone)) {
						sendMsg.blockDoesNotExist(nameToClone);
						return true;
					}

					// Make sure new name is not taken
					if (manager.doesBlockExist(newName)) {
						sendMsg.nameInUse();
						return true;
					}

					// Make sure not to clone to existing location
					if (player.getTargetBlock((Set<Material>) null, 5).getType() == Material.AIR) {
						sendMsg.notLockingAtBlock();
						return true;
					}

					Location newLocation = player.getTargetBlock((Set<Material>) null, 5).getLocation();

					if (manager.isBlockAssigned(newLocation)) {
						String name = manager.getBlockName(newLocation);
						sendMsg.nameAlreadyAssigned(name);
						return true;
					}
					//

					// Clone
					try {
						manager.cloneBlock(nameToClone, newName, newLocation);
						sendMsg.cloneComplete(nameToClone, newName, newLocation);
					} catch (Exception e) {
						e.printStackTrace();
					}

					return true;
				}

				sendMsg.invalidArgumentsGeneral();
				return true;

			} else {

				// btc add cmd {name} {true / false} {command}
				// 4 or more args

				if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("cmd")) {

					if (args.length == 4) {
						sendMsg.invalidArgumentsGeneral();
						return true;
					}

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					// Concatenate rest of args into a single string
					String commandString = combineArgs(args, 4);

					if (!(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))) {
						sendMsg.invalidTrueFalseInput(args[3]);
						return true;
					}

					boolean doesRunFromConsole;
					try {
						doesRunFromConsole = Boolean.parseBoolean(args[3]);
					} catch (Exception e) {
						sendMsg.invalidTrueFalseInput(args[3]);
						return true;
					}

					manager.addCommand(args[2], doesRunFromConsole, commandString);
					sendMsg.commandAdded(commandString, doesRunFromConsole);
					return true;

				} else if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("msg")) {

					// btc add msg {name} hello world!
					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					// Concatenate rest of args into a single string
					String message = combineArgs(args, 3);

					manager.addMsg(args[2], message);
					sendMsg.messageAdded(message);
					return true;

				} else if (args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("cooldown")) {

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					int cooldown;

					try {
						cooldown = Integer.parseInt(args[3]);
					} catch (Exception e) {
						sendMsg.invalidIntegerInput(args[3]);
						return true;
					}

					if (cooldown <= 0) {
						// Can't have negative / zero cooldown
						sendMsg.negativeValueInput(cooldown + "");
						return true;
					}

					String blockName = args[2];
					manager.setCooldown(blockName, cooldown);
					sendMsg.cooldowwnSet(cooldown);
					return true;
				} else if (args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("cost")) {
					// btc add cost {name} 100

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					float cost;

					try {
						cost = Float.parseFloat(args[3]);
					} catch (Exception e) {
						sendMsg.invalidFloatInput(args[3]);
						return true;
					}

					if (cost <= 0f) {
						// Can't have negative / zero cost
						sendMsg.negativeValueInput(cost + "");
						return true;
					}

					manager.addCost(args[2], cost);
					sendMsg.costSet(cost);
					return true;

				} else if (args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("perm")) {

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];
					String perm = args[3];

					manager.setPerm(blockName, perm);
					sendMsg.permSet(perm, blockName);
					return true;

				} else if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("cmd")) {
					// btc remove cmd {name} index

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					if (args[3].equalsIgnoreCase("all")) {
						manager.clearCommands(args[2]);
						sendMsg.allCommandsRemoved();
						return true;
					}

					int index;

					try {
						index = Integer.parseInt(args[3]);
					} catch (Exception e) {
						sendMsg.invalidIntegerInput(args[3]);
						return true;
					}

					manager.removeCommand(args[2], index);
					sendMsg.commandRemoved();
					return true;

				} else if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("msg")) {
					// btc remove cmd {name} index

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					if (args[3].equalsIgnoreCase("all")) {
						manager.clearMessages(args[2]);
						sendMsg.allMessagesRemoved();
						return true;
					}

					int index;

					try {
						index = Integer.parseInt(args[3]);
					} catch (Exception e) {
						sendMsg.invalidIntegerInput(args[3]);
						return true;
					}

					manager.reomoveMessage(args[2], index);
					sendMsg.messageRemoved();
					return true;

				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("msg")) {
					// btc info msg <name> <page>

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];

					int page;

					try {
						page = Integer.parseInt(args[3]);
					} catch (Exception e) {
						sendMsg.invalidIntegerInput(args[3]);
						return true;
					}

					sendMsg.listMessages(blockName, page);
					return true;

				} else if (args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("cmd")) {
					// btc info cmd <name> <page>

					BlockManager manager = new BlockManager();
					if (!manager.doesBlockExist(args[2])) {
						sendMsg.blockDoesNotExist(args[2]);
						return true;
					}

					String blockName = args[2];

					int page;

					try {
						page = Integer.parseInt(args[3]);
					} catch (Exception e) {
						sendMsg.invalidIntegerInput(args[3]);
						return true;
					}

					sendMsg.listCommands(blockName, page);
					return true;
				}

				sendMsg.invalidArgumentsGeneral();
				return true;
			}

			// Console
		} else if (commandLable.equalsIgnoreCase("btc") && !isCommandSenderPlayer(sender)) {
			// Console
			if (args.length == 0) {
				// TODO
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {
					// TODO
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					ConfigManager.getInstance().reloadConfig();
					Main.getPlugin().getLogger().info("Config reloaded");
					return true;
				}

				// TODO Invalid
				return true;
			}

			// TODO Invalid
			return true;
		}

		// No idea who/what sent this command
		return false;
	}

	// Helper Methods
	private String combineArgs(String[] args, int start) {
		StringBuilder buffer = new StringBuilder();
		// Combine all values in array >= start
		for (int i = start; i < args.length; i++) {
			buffer.append(' ').append(args[i]);
		}
		return buffer.toString().trim();
	}

	private boolean isCommandSenderPlayer(CommandSender sender) {
		return sender instanceof Player;
	}
}
