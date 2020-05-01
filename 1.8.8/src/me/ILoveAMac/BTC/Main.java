package me.ILoveAMac.BTC;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ILoveAMac.BTC.Comands.BTC;
import me.ILoveAMac.BTC.listeners.BlockBreak;
import me.ILoveAMac.BTC.listeners.PlayerInteract;
import me.ILoveAMac.BTC.util.ConfigManager;
import net.milkbowl.vault.economy.Economy;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

	private static Main plugin;

	private Economy econ;
	
	@Override
	public void onEnable() {
		// Setup main instance
		plugin = this;
		
		// Setup plugin functions
		setupBlocksFolder();
		commandRegister();
		eventRegister();
		configSetup();
		setupEconomy();
		
		// TODO Validate the blocks folder
	}

	@Override
	public void onDisable() {

	}

	public void commandRegister() {
		getCommand("btc").setExecutor(new BTC());
	}
	
	public void eventRegister() {
		getServer().getPluginManager().registerEvents(new BlockBreak(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
	}
	
	public void configSetup() {
		this.saveDefaultConfig();
		ConfigManager.getInstance().setup(this);
	}
	
	private void setupEconomy() {
        if (!setupEconomySuccess()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
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

    private void setupBlocksFolder(){
		File file = new File(this.getDataFolder() + File.separator + "blocks");
		if (!(file.exists() && file.isDirectory())) {
			boolean created = file.mkdir();
			if (created) {
				this.getLogger().info("The blocks data folder has been created!");
			} else {
				this.getLogger().severe("Could not create blocks data folder! Does the plugin have permission?");
				this.getLogger().info("Disabling plugin...");
				Bukkit.getPluginManager().disablePlugin(this);
			}
		} else {
			this.getLogger().info("blocks data folder is ready.");
		}
	}

	public static Main getPlugin() {
		return plugin;
	}
	
	public Economy getEconomy() {
		return econ;
	}
}
