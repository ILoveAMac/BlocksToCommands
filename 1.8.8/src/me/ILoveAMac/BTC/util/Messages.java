package me.ILoveAMac.BTC.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Messages {

	private Player player;

	private List<String> helpMessages;

	public Messages(Player player) {
		this.player = player;

		// Load help messages
		helpMessages = new ArrayList<String>();

		helpMessages.add("&6/btc reload");
		helpMessages.add("&6/btc help &r&c<page>");
		helpMessages.add("&6/btc list &r&c<page>");
		helpMessages.add("&6/btc create &r&c<name>");
		helpMessages.add("&6/btc delete &r&c<name>");
		helpMessages.add("&6/btc clone &r&c<nameOfBlockToClone> <newName>");
		helpMessages.add("&6/btc tp &r&c<name>");
		helpMessages.add("&6/btc add cmd &r&c<name> <true/false : runFromConsole> <command>");
		helpMessages.add("&6/btc remove cmd &r&c<name> <index number / all>");
		helpMessages.add("&6/btc add msg &r&c<name> <msg>");
		helpMessages.add("&6/btc remove msg &r&c<name> <index number / all>");
		helpMessages.add("&6/btc set cost &r&c<name> <cost>");
		helpMessages.add("&6/btc remove cost &r&c<name>");
		helpMessages.add("&6/btc set perm &r&c<name> <cost>");
		helpMessages.add("&6/btc remove perm &r&c<name>");
//		helpMessages.add("&6/btc set cooldown &r&c<name> <cooldown in seconds>");
//		helpMessages.add("&6/btc remove cooldown &r&c<name>");
		helpMessages.add("&6/btc info cost &r&c<name>");
		helpMessages.add("&6/btc info perm &r&c<name>");
//		helpMessages.add("&6/btc info cooldown &r&c<name>");
		helpMessages.add("&6/btc info cmd &r&c<name> <page>");
		helpMessages.add("&6/btc info msg &r&c<name> <page>");
		helpMessages.add("&6/btc info location/loc &r&c<name> <page>");
	}

	// Admin Player Messages
	public void invalidArgumentsGeneral() {

		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &cInvalid Arguments."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Type &b/btc help &7for command usage."));
	}

	public void helpMessage(int page) {
		int listPages = (int) Math.ceil((helpMessages.size() / 8f));

		if (listPages == 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cNothing to display."));
			return;
		}

		if (page > listPages || page <= 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&6&l[BTC] &r&cPage requested &a(" + page + ") &cdoes not exist."));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Only pages &a1 to " + listPages + " &7are available."));
			return;
		}

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&l-------&rHelp: page " + page + "/" + listPages + "&e&l-------"));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&7Type &b/btc help <page> &7for more information."));

		// Calculate start position
		int startPos = 0 + 8 * (page - 1); // Tn = a + d(n - 1)

		// Display
		int i = 0;
		while (i < 8 && i + startPos < helpMessages.size()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessages.get(startPos + i)));
			i++;
		}
	}

	public void listBlocks(int page) {
		// Get list to work with
		BlockManager manager = new BlockManager();
		List<String> blockNames = manager.getBlockList();

		// Sort List
		Collections.sort(blockNames);

		int listPages = (int) Math.ceil((blockNames.size() / 8f));

		if (listPages == 0) {
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cYou have no BTC blocks. &aAdd some!"));
			return;
		}

		if (page > listPages || page <= 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&6&l[BTC] &r&cPage requested &a(" + page + ") &cdoes not exist."));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Only pages &a1 to " + listPages + " &7are available."));
			return;
		}

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&l-------&rList: page " + page + "/" + listPages + "&e&l-------"));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&7Type &b/btc list <page> &7for more information."));

		// Calculate start position
		int startPos = 0 + 8 * (page - 1); // Tn = a + d(n - 1)

		// Display
		int i = 0;
		int startPosCount = startPos;
		while (i < 8 && i + startPos < blockNames.size()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7" + (startPosCount + 1) + ": &r" + blockNames.get(startPos + i)));
			i++;
			startPosCount++;
		}
	}

	public void listMessages(String blockName, int page) {
		// Get list to work with
		BlockManager manager = new BlockManager();
		List<String> messages = manager.getMessages(blockName);

		if (messages == null) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cNo messages to display."));
			return;
		}

		int listPages = (int) Math.ceil((messages.size() / 8f));

		if (listPages == 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cNo messages to display."));
			return;
		}

		if (page > listPages || page <= 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&6&l[BTC] &r&cPage requested &a(" + page + ") &cdoes not exist."));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Only pages &a1 to " + listPages + " &7are available."));
			return;
		}

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&l-------&rList: page " + page + "/" + listPages + "&e&l-------"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&7Type &b/btc info msg &a" + blockName + " &b<page> &7for more information."));
		// /btc info msg <name> <page>

		// Calculate start position
		int startPos = 0 + 8 * (page - 1); // Tn = a + d(n - 1)

		// Display
		int i = 0;
		int startPosCount = startPos;
		while (i < 8 && i + startPos < messages.size()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7" + (startPosCount + 1) + ": &r" + messages.get(startPos + i)));
			i++;
			startPosCount++;
		}
	}

	public void listCommands(String blockName, int page) {
		// Get list to work with
		BlockManager manager = new BlockManager();
		List<List<String>> commandData = manager.getCommands(blockName);

		if (commandData == null) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cNo commands to display."));
			return;
		}

		// Get Command strings List
		List<String> commandStrings = commandData.get(0);
		List<String> commandStates = commandData.get(1);

		int listPages = (int) Math.ceil((commandStrings.size() / 8f));

		if (listPages == 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cNothing to display."));
			return;
		}

		if (page > listPages || page <= 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&6&l[BTC] &r&cPage requested &a(" + page + ") &cdoes not exist."));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Only pages &a1 to " + listPages + " &7are available."));
			return;
		}

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&l-------&rList: page " + page + "/" + listPages + "&e&l-------"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&7Type &b/btc info cmd &a" + blockName + " &b<page> &7for more information."));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&bThe colour reflects the runFromConsole state."));
		// /btc info cmd <name> <page>

		// Calculate start position
		int startPos = 0 + 8 * (page - 1); // Tn = a + d(n - 1)

		// Display
		int i = 0;
		int startPostCount = startPos;
		while (i < 8 && i + startPos < commandStrings.size()) {

			if (commandStates.get(startPos + i).equalsIgnoreCase("true")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&7" + (startPostCount + 1) + ": &r&a" + commandStrings.get(startPos + i)));
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&7" + (startPostCount + 1) + ": &r&c" + commandStrings.get(startPos + i)));
			}
			i++;
			startPostCount++;
		}
	}

	public void notLockingAtBlock() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &cYou are not looking at a block."));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&7Look at a block while running the &bcreate command&7."));
	}

	public void nameInUse() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &cThis name is already in use."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Pick an new name and try again."));
	}

	public void nameAlreadyAssigned(String blockName) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &cThis block is already assigned as: &b" + blockName));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Try looking at a different block."));
	}

	public void unknownError() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&6An &cunknown error &6occurred."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&7Try deleting all the files in the blocks folder and starting again"));
	}

	public void creationConfirmation(String blockName, Location blockLocation) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aSucsessfully created:&b " + blockName + " &awith properties:"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ax:&b " + blockLocation.getBlockX()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ay:&b " + blockLocation.getBlockY()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&az:&b " + blockLocation.getBlockZ()));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&aworld:&b " + blockLocation.getWorld().getName()));
	}

	public void cloneComplete(String originalName, String cloneName, Location blockLocation) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aSucsessfully cloned:&b " + originalName + " &ato " + cloneName + " &awith properties:"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ax:&b " + blockLocation.getBlockX()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ay:&b " + blockLocation.getBlockY()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&az:&b " + blockLocation.getBlockZ()));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&aworld:&b " + blockLocation.getWorld().getName()));
	}

	public void locationInfo(String blockName) {
		BlockManager manager = new BlockManager();
		Location blockLocation = manager.getBlockLocation(blockName);

		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aLocation info of:&b " + blockName));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ax:&b " + blockLocation.getBlockX()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ay:&b " + blockLocation.getBlockY()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&az:&b " + blockLocation.getBlockZ()));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&aworld:&b " + blockLocation.getWorld().getName()));

	}

	public void blockDoesNotExist(String blockName) {
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cThere is no block called: &a" + blockName));
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&7Do &b/btc list&7 to see a list of all the block names"));
	}

	public void blockDeleted(String blockName) {
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aBlock:&b " + blockName + " &awas deleted."));
	}

	public void costSet(float cost) {
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe cost has been set to: &b" + cost));
	}

	public void cooldownRemoved() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe cooldown has been removed."));
	}

	public void cooldowwnSet(int cooldown) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aThe cooldown has been set to: &b" + cooldown + " &aseconds"));
	}

	public void costRemoved() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe cost has been removed."));
	}

	public void costValue(String blockName) {
		BlockManager manager = new BlockManager();

		float costValue = manager.getCost(blockName);

		if (costValue == 0) {
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThis block has no set cost."));
			return;
		}

		String cost = costValue + "";

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aThe cost set to &b" + blockName + " &ais &b" + cost + "&a units"));

	}

	public void commandAdded(String commandString, boolean doesRunFromConsole) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe command has been added."));

		if (doesRunFromConsole) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bRun from console: &atrue"));
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bRun from console: &cfalse"));
		}

		player.sendMessage(ChatColor.GRAY + "Command: " + ChatColor.RESET + commandString);
	}

	public void commandRemoved() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe command has been removed."));
	}
	
	public void allCommandsRemoved() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aAll commands been removed."));
	}

	public void messageAdded(String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe message has been added."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Message:&r " + message));
	}

	public void messageRemoved() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe message has been removed."));
	}

	public void allMessagesRemoved() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aAll messages have been removed."));
	}
	
	public void permSet(String perm, String blockName) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe permission:&d " + perm
				+ "&a will now be needed to interact with block: &b" + blockName));
	}

	public void permValue(String blockName) {
		BlockManager manager = new BlockManager();

		String perm = manager.getPerm(blockName);

		if (perm == null) {
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThis block has no permission set."));
			return;
		}

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aThe premission set to &b" + blockName + " &ais &b" + perm));

	}

	public void cooldownValue(String blockName) {
		BlockManager manager = new BlockManager();

		int cooldown = manager.getCooldown(blockName);

		if (cooldown == 0) {
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThis block has no permission set."));
			return;
		}

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aThe cooldown set to &b" + blockName + " &ais &b" + cooldown + " &aseconds."));

	}

	public void removePerm() {
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aThe permission has been removed."));
	}

	public void invalidIntegerInput(String incorrectInput) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&cThe value: &a" + incorrectInput + "&c is not a valid integer!"));
	}

	public void invalidFloatInput(String incorrectInput) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&cThe value: &a" + incorrectInput + "&c is not a valid floating point value!"));
	}

	public void negativeValueInput(String incorrectInput) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&cThe value: &a" + incorrectInput
				+ "&c is not valid. Only numbers greater than zero are allowed!"));
	}

	public void invalidTrueFalseInput(String incorrectInput) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&cThe value: &a" + incorrectInput + "&c is not a valid true/fales value!"));
	}

	public void succesfullTp(String blockName) {
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aYou were teleported to:&b " + blockName));
	}

	public void notAdmin() {
		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&c&lYou do not have permission to preform that command."));
	}

	public void cannotBreakBlockAdmin(String blockName) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[BTC] &r&aThis is a BTC block. To get rid of it remove it with: &b/btc remove " + blockName));

		ConfigManager configManager = ConfigManager.getInstance();
		if ((boolean) configManager.get("options.adminShiftRemove")) {

			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&6&l[BTC] &r&aAlternatively hold shift and break the block."));
		}
	}

	public void reloadComplete() {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[BTC] &r&aconfig.yml was reloaded!"));
	}

	public void listBlocks() {
		listBlocks(1);
	}

	public void helpMessage() {
		helpMessage(1);
	}

	// NORMAL PLAYER MESSAGE -> Get msg from config.yml
	public void cannotBreakBlock() {
		ConfigManager configManager = ConfigManager.getInstance();

		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', configManager.get("messages.mayNotBreakBlock").toString()));
	}

	public void cannotUseBlock() {
		ConfigManager configManager = ConfigManager.getInstance();

		player.sendMessage(
				ChatColor.translateAlternateColorCodes('&', configManager.get("messages.mayNotUseBlock").toString()));
	}

	public void notEnoughFunds(float cost) {
		ConfigManager configManager = ConfigManager.getInstance();

		String message = VariableReplacer.replaceConfigVars(configManager.get("messages.notEnoughFunds").toString(),
				cost);

		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public void moneyDeducted(float cost) {
		ConfigManager configManager = ConfigManager.getInstance();

		String message = VariableReplacer.replaceConfigVars(configManager.get("messages.fundsDeducted").toString(),
				cost);

		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
