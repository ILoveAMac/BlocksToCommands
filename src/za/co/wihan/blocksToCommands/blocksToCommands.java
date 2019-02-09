package za.co.wihan.blocksToCommands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import za.co.wihan.Listeners.PlayerInteract;
import za.co.wihan.blocksToCommands.Config.ConfigManager;
import za.co.wihan.blocksToCommands.Config.MainConfigManager;
import za.co.wihan.blocksToCommands.commands.BlockToCommand;

import java.util.logging.Logger;


public class blocksToCommands extends JavaPlugin {

    private static ConfigManager blocks;
    public static Economy econ = null;

    @Override
    public void onEnable() {
        // Get information from plugin.yml
        PluginDescriptionFile pdfFile = getDescription();
        // Create logger variable
        Logger logger = getLogger();

        // Register commands
        getCommand("btc").setExecutor(new BlockToCommand());

        // Setup config.yml & blocks.yml
        blocks = new ConfigManager(this, "blocks");
        saveDefaultConfig();
        MainConfigManager.getInstance().setup(this);

        // Register Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);

        // Display message to indicate that the plugin has been enabled.
        logger.info(pdfFile.getName() + " by " + pdfFile.getAuthors() + " has been enabled (V." + pdfFile.getVersion() + ")");



        // Check if Vault is  installed, if not quit plugin
        try {
            Class.forName("net.milkbowl.vault.Vault");
        } catch(ClassNotFoundException e) {
            logger.severe(String.format(ChatColor.RED + "[BTC] [%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Get information from plugin.yml
        // Create logger variable
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();

        // Display message to indicate that the plugin has been disabled.
        logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
    }

    public static ConfigManager getBlocks() {
        return blocks;
    }

}
