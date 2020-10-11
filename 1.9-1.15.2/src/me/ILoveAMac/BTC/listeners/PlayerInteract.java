package me.ILoveAMac.BTC.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ILoveAMac.BTC.Main;
import me.ILoveAMac.BTC.util.BlockManager;
import me.ILoveAMac.BTC.util.Messages;
import me.ILoveAMac.BTC.util.VariableReplacer;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.inventory.EquipmentSlot;


public class PlayerInteract implements Listener {


	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) {
			// No block clicked
			return;
		}

		EquipmentSlot e = event.getHand(); //Get the hand of the event and set it to 'e'.
		if (e != null && !e.equals(EquipmentSlot.HAND)) { //If the event is fired by HAND (main hand)
			return;
		}

		// Setup blockManager
		BlockManager blockManager = new BlockManager();

		// Get click location
		Location clickLocation = event.getClickedBlock().getLocation();

		// Check if block is a BTC block
		if (!blockManager.isBlockAssigned(clickLocation)) {
			// Not a BTC Block
			return;
		}

		// Get Player
		Player player = event.getPlayer();

		// Setup Messages
		Messages sendMsg = new Messages(player);

		// Get block data
		String blockName = blockManager.getBlockName(clickLocation);
		Location blockLocation = blockManager.getBlockLoaction(blockName);

		String customPerm = blockManager.getPerm(blockName);
		float cost = blockManager.getCost(blockName);
		List<String> messages = blockManager.getMessages(blockName);
		List<List<String>> commandData = blockManager.getCommands(blockName);

		// May player use block?
		if (!doesHavePermission(customPerm, player)) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				sendMsg.cannotUseBlock();
			} else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				sendMsg.cannotBreakBlock();
			}
			event.setCancelled(true);
			return;
		}

		// Admin specific msg
		if (player.hasPermission("btc.admin") && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			return;
		}

		// TODO Cooldown implementation

		if (cost > 0) {
			if (applyCostSuccess(cost, player)) {
				sendMsg.moneyDeducted(cost);
			} else {
				sendMsg.notEnoughFunds(cost);
				event.setCancelled(true);
				return;
			}
		}

		sendMessages(messages, player, blockLocation);

		runCommands(commandData, player, blockLocation);

		event.setCancelled(true);
	}

	public void sendMessages(List<String> messages, Player player, Location blockLocation) {
		if (messages == null) {
			return;
		}

		for (String message : messages) {
			String formattedMessage = VariableReplacer.replaceVars(message, player, blockLocation);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
		}
	}

	public void runCommands(List<List<String>> commandData, Player player, Location blockLocation) {
		if (commandData == null) {
			return;
		}

		// Separate data
		List<String> commandStrings = commandData.get(0);
		List<String> commandStates = commandData.get(1);

		for (int i = 0; i < commandStrings.size(); i++) {
			boolean runFromConsole = Boolean.parseBoolean(commandStates.get(i));
			// Replace vars
			String commandString = VariableReplacer.replaceVars(commandStrings.get(i), player, blockLocation);
			if (runFromConsole) {
				ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
				Bukkit.dispatchCommand(console, commandString);
			} else {
				player.performCommand(commandString);
			}
		}
	}

	public boolean applyCostSuccess(float cost, Player player) {
		Economy economy = Main.getPlugin().getEconomy();

		EconomyResponse economyResponse = economy.withdrawPlayer(player, cost);

		return economyResponse.transactionSuccess();
	}

	public boolean doesHavePermission(String customPerm, Player player) {
		if (customPerm == null) {
			return player.hasPermission("btc.use");
		} else {
			return player.hasPermission("btc.use") && player.hasPermission(customPerm);
		}
	}
}
