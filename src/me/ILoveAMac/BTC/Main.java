package me.ILoveAMac.BTC;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ILoveAMac.BTC.Comands.BTC;
import me.ILoveAMac.BTC.listeners.BlockBreak;
import me.ILoveAMac.BTC.listeners.PlayerInteract;
import me.ILoveAMac.BTC.util.ConfigManager;
import net.milkbowl.vault.economy.Economy;

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
        if (!setupEconomySucsess()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
	}
	
    private boolean setupEconomySucsess() {
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

	public static Main getPlugin() {
		return plugin;
	}
	
	public Economy getEconomy() {
		return econ;
	}
}
