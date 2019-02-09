package za.co.wihan.blocksToCommands.commandHelper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckIfValid {

	public boolean validateCommand(CommandSender sender) {
		// Check if sender is a player
		if (!(sender instanceof Player)) {
			sender.sendMessage("[BTC] This command cannot be run from console!");
			return false;
		}
		return true;
	}
}
