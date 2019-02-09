package za.co.wihan.blocksToCommands.economy;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import za.co.wihan.blocksToCommands.Config.MainConfigManager;

import static za.co.wihan.blocksToCommands.blocksToCommands.econ;
import static za.co.wihan.blocksToCommands.blocksToCommands.getBlocks;

/**
 * Created by ILoveAMac on 2017/06/28.
 */

public class Cost {

    public void addCost(double amount, Player player, String name) {
        if (getBlocks().contains(name)) {
            getBlocks().set(name + ".Cost", amount);
            getBlocks().save();
            player.sendMessage(ChatColor.GREEN + "[BTC] You have successfully added a cost of " + amount + " to this block!");
        } else {
            player.sendMessage(ChatColor.RED + "[BTC] Could not add a cost because the block does not exist!");
        }
    }

    public void removeCost(Player player, String name) {
        try {
            if (getBlocks().contains(name + ".Cost") && Double.parseDouble(getBlocks().get(name + ".Cost") + "") > 0) {
                getBlocks().set(name + ".Cost", "0");
                getBlocks().save();
                player.sendMessage(ChatColor.GREEN + "[BTC] You have successfully removed the cost from this block!");
            } else {
                player.sendMessage(ChatColor.RED + "[BTC] Could not remove the cost because the block does not exist or the cost is 0");
            }
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + "[BTC] No cost is defined in blocks.yml, cost could not be removed");
        } catch (Exception ex) {
            player.sendMessage(ChatColor.RED + "[BTC] Could not remove the cost - UNKNOWN ERROR");
            player.sendMessage(ChatColor.RED + ex.toString());
        }
    }

    public boolean applyCost(Player player, String blockName) {
        if ((getBlocks().get(blockName + ".Cost") + "") != "") {
            if (Double.parseDouble(getBlocks().get(blockName + ".Cost") + "") > 0) {
                try {
                    EconomyResponse r = econ.withdrawPlayer(player, Double.parseDouble(getBlocks().get(blockName + ".Cost") + ""));
                    if (r.transactionSuccess()) {
                        String currency = (String) MainConfigManager.getInstance().get("messages.currency");
                        player.sendMessage(ChatColor.GREEN + "[BTC] " + Double.parseDouble(getBlocks().get(blockName + ".Cost") + "") + " " + currency + " were deducted from your account");
                        return true;
                    } else {
                        String message = (String) MainConfigManager.getInstance().get("messages.noFunds");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    return true;
                } catch (Exception ex) {
                    player.sendMessage(String.format("Err"));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                    return false;
                }
            }
        }
        return true;
    }

}
