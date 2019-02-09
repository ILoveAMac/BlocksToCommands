package za.co.wihan.blocksToCommands.Config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;


/**
 * This class was created on 1/12/2017 in the project Bukkit Tutorials by DeveloperB.
 */

public class ConfigManager {

    private File file;
    private File blocksYML;
    private FileConfiguration config;

    private ConfigManager() {

    }

    private static ConfigManager instance = new ConfigManager();

    public static ConfigManager getInstance() {
        return instance;
    }

    public ConfigManager(Plugin plugin, String fileName) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), fileName + ".yml");

        blocksYML = new File(plugin.getDataFolder(), "blocks.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }


    public void set(String path, Object value) {
        config.set(path, value);
    }

    public void reloadConfigs(){config = YamlConfiguration.loadConfiguration(blocksYML);}

    public Object get(String path) {
        return config.get(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public ConfigurationSection createSection(String path) {
        ConfigurationSection section = config.createSection(path);
        save();
        return section;
    }

    public Set<String> getMainKeys() {
        return config.getKeys(false);
    }

    public Set<String> getKeys(String path) {
        return config.getConfigurationSection(path).getKeys(false);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}