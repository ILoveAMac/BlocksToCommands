package me.ILoveAMac.BTC.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BlockManager {

	private final String blocksDataFolderPath = "plugins" + File.separator + "Blocks_To_Commands" + File.separator + "blocks";

	public void CreateBlock(CmdBlock cmdBlock) {
		FileConfiguration config = null;
		File file = new File(blocksDataFolderPath + File.separator + cmdBlock.getName() + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			config = YamlConfiguration.loadConfiguration(file);
			// Add Values
			config.set("Block.name", cmdBlock.getName());

			// Location
			config.set("Block.location.world", cmdBlock.getLocation().getWorld().getName());
			config.set("Block.location.x", cmdBlock.getLocation().getBlockX());
			config.set("Block.location.y", cmdBlock.getLocation().getBlockY());
			config.set("Block.location.z", cmdBlock.getLocation().getBlockZ());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void cloneBlock(String nameOfBlockToClone, String newBlockName, Location locationOfNewBlock)
			throws IOException, IllegalArgumentException {

		// Check if block to clone exists
		if (!doesBlockExist(nameOfBlockToClone)) {
			// Fail
			throw new IllegalArgumentException();
		}

		// Check if new name is already taken
		if (doesBlockExist(newBlockName)) {
			// Fail
			throw new IllegalArgumentException();
		}

		File sourceFile = new File(blocksDataFolderPath + File.separator + nameOfBlockToClone + ".yml");
		File dest = new File(blocksDataFolderPath + File.separator + newBlockName + ".yml");

		try (FileInputStream fis = new FileInputStream(sourceFile); FileOutputStream fos = new FileOutputStream(dest)) {

			byte[] buffer = new byte[1024];
			int length;

			while ((length = fis.read(buffer)) > 0) {

				fos.write(buffer, 0, length);
			}
		}

		// Setup config for new file
		FileConfiguration config = YamlConfiguration.loadConfiguration(dest);

		// Setup cloned file
		config.set("Block.name", newBlockName);
		config.set("Block.location.world", locationOfNewBlock.getWorld().getName());
		config.set("Block.location.x", locationOfNewBlock.getBlockX());
		config.set("Block.location.y", locationOfNewBlock.getBlockY());
		config.set("Block.location.z", locationOfNewBlock.getBlockZ());

		// Save changes
		try {
			config.save(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void deleteBlock(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");

		if (!file.exists()) {
			return;
		}

		try {
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public List<PlayerCommand> getBlockCmds(Location location) {

		File dataFolder = new File(blocksDataFolderPath);
		File[] files = dataFolder.listFiles();

		if (files == null) {
			return null;
		}

		for (File file : files) {

			if (!file.getName().contains(".yml")) {
				continue;
			}

			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			int x = config.getInt("Block.location.x");
			int y = config.getInt("Block.location.y");
			int z = config.getInt("Block.location.z");
			String world = config.getString("Block.location.world");

			if (location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z
					&& location.getWorld().getName().equals(world)) {

				if (config.getConfigurationSection("Block.commands").getKeys(false).size() == 0) {
					return null;
				}

				List<PlayerCommand> cmds = new ArrayList<PlayerCommand>();

				String[] commandIndexes = config.getConfigurationSection("Block.commands").getKeys(false)
						.toArray(new String[0]);

				for (String commandIndex : commandIndexes) {
					String commandString = config.getString("Block.commands." + commandIndex + ".command");
					boolean runAsConsole = config.getBoolean("Block.commands." + commandIndex + ".runFromConsole");
					PlayerCommand pcmd = new PlayerCommand(commandString, runAsConsole);
					cmds.add(pcmd);
				}

				return cmds;

			}

		}
		return null;
	}

	public List<String> getBlockList() {
		File dataFolder = new File(blocksDataFolderPath);
		File[] files = dataFolder.listFiles();

		// Check if there are block files
		if (files == null) {
			return null;
		}

		List<String> blockNames = new ArrayList<String>();

		for (File file : files) {

			// Make sure file is a YAML file
			if (!file.getName().contains(".yml")) {
				continue;
			}

			// Load file
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			// Add block to list
			String blockName = config.getString("Block.name");
			blockNames.add(blockName);
		}
		return blockNames;
	}

	public List<String> getMessages(String blockName) {

		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		String[] keys;

		// Get message keys
		try {
			keys = config.getConfigurationSection("Block.messages").getKeys(false).toArray(new String[0]);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		// Create list to store messages
		List<String> messages = new ArrayList<String>();

		// Add messages
		for (String key : keys) {
			messages.add(config.getString("Block.messages." + key + ".message"));
		}

		return messages;
	}

	public List<List<String>> getCommands(String blockName) {

		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		String[] keys;

		// Get message keys
		try {
			keys = config.getConfigurationSection("Block.commands").getKeys(false).toArray(new String[0]);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

		// Create list to store command strings
		List<String> commands = new ArrayList<String>();

		// Create list to store command states
		List<String> commandStates = new ArrayList<String>();

		// Add commands and command states
		for (String key : keys) {
			commands.add(config.getString("Block.commands." + key + ".command"));
			commandStates.add(config.getString("Block.commands." + key + ".runFromConsole"));
		}

		// Create combined list
		List<List<String>> commandData = new ArrayList<List<String>>();
		commandData.add(commands);
		commandData.add(commandStates);

		return commandData;
	}

	public void addCommand(String blockName, boolean doesRunFromConsole, String command) {

		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		int cmdCount = 0;
		if (!(config.getConfigurationSection("Block.commands") == null)) {
			cmdCount = config.getConfigurationSection("Block.commands").getKeys(false).toArray(new String[0]).length;
		}

		config.set("Block.commands." + "cmd_" + (cmdCount + 1) + ".command", command);
		config.set("Block.commands." + "cmd_" + (cmdCount + 1) + ".runFromConsole", doesRunFromConsole);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearCommands(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.commands", null);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeCommand(String blockName, int index) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.commands.cmd_" + index, null);

		// Rewrite current index numbers
		if (config.getConfigurationSection("Block.commands") == null) {
			try {
				config.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		String[] keys = config.getConfigurationSection("Block.commands").getKeys(false).toArray(new String[0]);

		int newIndex = 1;
		for (String key : keys) {

			// Get key data
			String commandString = config.getString("Block.commands." + key + ".command");
			boolean runFromConsole = config.getBoolean("Block.commands." + key + ".runFromCnsole");

			// Delete key
			config.set("Block.commands." + key, null);

			// Rewrite key
			config.set("Block.commands." + "cmd_" + newIndex + ".command", commandString);
			config.set("Block.commands." + "cmd_" + newIndex + ".runFromCnsole", runFromConsole);

			newIndex++;
		}

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reomoveMessage(String blockName, int index) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.messages.msg_" + index, null);

		// Rewrite current index numbers
		if (config.getConfigurationSection("Block.messages") == null) {
			try {
				config.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		String[] keys = config.getConfigurationSection("Block.messages").getKeys(false).toArray(new String[0]);

		int newIndex = 1;
		for (String key : keys) {

			// Get key data
			String commandString = config.getString("Block.messages." + key + ".message");

			// Delete key
			config.set("Block.messages." + key, null);

			// Rewrite key
			config.set("Block.messages." + "msg_" + newIndex + ".message", commandString);

			newIndex++;
		}

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearMessages(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.messages", null);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeCost(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.cost", null);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCost(String blockName, float cost) {

		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.cost", cost);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float getCost(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		float cost = (float) config.getDouble("Block.cost");
		
		return cost;
	}
	
	public void setCooldown(String blockName, int cooldownInSec) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.cooldown", cooldownInSec);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeCooldown(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.cooldown", null);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getCooldown(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		int cooldown = config.getInt("Block.cooldown");
		
		return cooldown;
	}
	
	public void setPerm(String blockName, String perm) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		config.set("Block.permission", perm);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removePerm(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Block.permission", null);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getPerm(String blockName) {
		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		String perm = config.getString("Block.permission");
		
		return perm;
	}

	public void addMsg(String blockName, String message) {

		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		int msgCount = 0;
		if (!(config.getConfigurationSection("Block.messages") == null)) {
			msgCount = config.getConfigurationSection("Block.messages").getKeys(false).toArray(new String[0]).length;
		}

		config.set("Block.messages." + "msg_" + (msgCount + 1) + ".message", message);

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean doesBlockExist(String blockName) {

		File dataFolder = new File(blocksDataFolderPath);
		File[] files = dataFolder.listFiles();

		for (File file : files) {

			if (!file.getName().contains(".yml")) {
				continue;
			}

			String fileName = file.getName();

			String testName = blockName + ".yml";

			if (testName.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	public boolean isBlockAssigned(Location location) {

		File dataFolder = new File(blocksDataFolderPath);
		File[] files = dataFolder.listFiles();

		for (File file : files) {

			if (!file.getName().contains(".yml")) {
				continue;
			}

			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			int x = config.getInt("Block.location.x");
			int y = config.getInt("Block.location.y");
			int z = config.getInt("Block.location.z");
			String world = config.getString("Block.location.world");

			if (location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z
					&& location.getWorld().getName().equals(world)) {
				return true;
			}
		}
		return false;
	}

	public String getBlockName(Location location) {
		File dataFolder = new File(blocksDataFolderPath);
		File[] files = dataFolder.listFiles();

		for (File file : files) {

			if (!file.getName().contains(".yml")) {
				continue;
			}

			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			int x = config.getInt("Block.location.x");
			int y = config.getInt("Block.location.y");
			int z = config.getInt("Block.location.z");
			String world = config.getString("Block.location.world");

			if (location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z
					&& location.getWorld().getName().equals(world)) {
				return config.getString("Block.name");
			}
		}
		return null;
	}

	public Location getBlockLocation(String blockName) {

		File file = new File(blocksDataFolderPath + File.separator + blockName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		// Get data
		String worldName = config.getString("Block.location.world");
		int x = config.getInt("Block.location.x");
		int y = config.getInt("Block.location.y") + 1;
		int z = config.getInt("Block.location.z");

		// Get world from data
		World world = Bukkit.getWorld(worldName);

		if (world == null) {
			return null;
		}

		// Create location

		return new Location(world, x, y, z);
	}
}
