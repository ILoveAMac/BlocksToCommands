package me.ILoveAMac.BTC;

import me.ILoveAMac.BTC.Comands.BTC;
import me.ILoveAMac.BTC.Metrics.Metrics;
import me.ILoveAMac.BTC.listeners.BlockBreak;
import me.ILoveAMac.BTC.listeners.PlayerInteract;
import me.ILoveAMac.BTC.util.ConfigManager;
import me.ILoveAMac.BTC.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.io.File;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main plugin;

    private Economy econ;

    @Override
    public void onEnable() {
        // Setup main instance
        plugin = this;

        // Setup plugin functions
        commandRegister();
        eventRegister();

        setupPluginFolder();
        configSetup();
        setupBlocksFolder();

        setupEconomy();

        updateChecker();
        setupMetrics();

        // TODO Validate the blocks folder
    }


    @Override
    public void onDisable() {

    }

    private void updateChecker() {
        new UpdateChecker(this, 42090).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getServer().getConsoleSender().sendMessage(ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "[BTC]" + ChatColor.RESET.toString() + ChatColor.GREEN.toString() + " Plugin running latest version!");
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "===========");
                getServer().getConsoleSender().sendMessage(ChatColor.GOLD.toString() + "[BTC]" + ChatColor.RESET.toString() + ChatColor.AQUA.toString() + " There is a new update available on spigot!");
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "===========");
            }
        });
    }

    private void setupMetrics() {
        // Create opt-out in config.yml if it does not exist
        ConfigManager configManager = ConfigManager.getInstance();
        if (!configManager.isSet("options.metrics")) {
            configManager.set("options.metrics", true);
            configManager.save();
            configManager.reloadConfig();
        }

        // Check for opt-out
        if (!((boolean) configManager.get("options.metrics"))) {
            // user opt-out
            return;
        }

        this.getLogger().info("Metrics have been enabled, opt-out in config.yml");
        Metrics metrics = new Metrics(this, 7383);

    }


    private void commandRegister() {
        getCommand("btc").setExecutor(new BTC());
    }

    private void eventRegister() {
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    private void configSetup() {
        this.saveDefaultConfig();
        ConfigManager.getInstance().setup(this);
    }

    private void setupEconomy() {
        if (!setupEconomySuccess()) {
            this.getLogger().severe("Vault is not installed and or you do not have an economy plugin installed!");
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD.toString() + "[BTC]" + ChatColor.RESET.toString() + ChatColor.RED.toString() + " Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            this.getLogger().info("Successfully hooked into vault and your economy plugin.");
        }
    }

    private boolean setupEconomySuccess() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void setupBlocksFolder() {
        File file = new File(this.getDataFolder() + File.separator + "blocks");
        if (!(file.exists() && file.isDirectory())) {
            boolean created = file.mkdir();
            if (created) {
                this.getLogger().info("The blocks data folder has been created!");
            } else {
                this.getLogger().severe("Could not create blocks data folder! Does the plugin have permission?");
                getServer().getConsoleSender().sendMessage(ChatColor.GOLD.toString() + "[BTC]" + ChatColor.RESET.toString() + ChatColor.RED.toString() + " Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            this.getLogger().info("blocks data folder is ready.");
        }
    }

    private void setupPluginFolder() {
        if (!this.getDataFolder().exists()) {
            boolean mkdir = this.getDataFolder().mkdir();
            this.getLogger().info("Plugin main folder is missing. Making it now.");
            if (!mkdir) {
                this.getLogger().severe("Could not create main plugin folder! Does the plugin have permission?");
                getServer().getConsoleSender().sendMessage(ChatColor.GOLD.toString() + "[BTC]" + ChatColor.RESET.toString() + ChatColor.RED.toString() + " Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
            } else {
                this.getLogger().info("Plugin main folder has been created.");
            }
        }
    }

    public static Main getPlugin() {
        return plugin;
    }

    public Economy getEconomy() {
        return econ;
    }
}
