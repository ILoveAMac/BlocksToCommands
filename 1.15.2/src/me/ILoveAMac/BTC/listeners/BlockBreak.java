package me.ILoveAMac.BTC.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.ILoveAMac.BTC.util.BlockManager;
import me.ILoveAMac.BTC.util.ConfigManager;
import me.ILoveAMac.BTC.util.Messages;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// Check if block broken is a btc block

		Location blockLocation = event.getBlock().getLocation();
		BlockManager manager = new BlockManager();

		if (!manager.isBlockAssigned(blockLocation)) {
			// Normal block break
			return;
		}

		Player player = event.getPlayer();
		Messages sendMsg = new Messages(player);

		String blockName = manager.getBlockName(blockLocation);

		// Shift Remove
		ConfigManager configManager = ConfigManager.getInstance();
		
		if (((boolean) configManager.get("options.adminShiftRemove")) && player.hasPermission("btc.admin") && player.isSneaking()) {
			// Delete block
			manager.deleteBlock(blockName);
			sendMsg.blockDeleted(blockName);
			return;
		}
		
		if (player.hasPermission("btc.admin")) {
			// Block must be deleted first msg
			sendMsg.cannotBreakBlockAdmin(blockName);
			// Cancel event
			event.setCancelled(true);
		} else {
			// Not admin msg
			sendMsg.cannotBreakBlock();
			// Cancel event
			event.setCancelled(true);
		}
	}
}
