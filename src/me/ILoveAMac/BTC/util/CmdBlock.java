package me.ILoveAMac.BTC.util;

import java.util.List;

import org.bukkit.Location;

public class CmdBlock {
	
	private String name;
	private Location location;
	private List<PlayerCommand> commands;
	
	public CmdBlock(String name, Location location, List<PlayerCommand> list) {
		this.name = name;
		this.location = location;
		this.commands = list;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public List<PlayerCommand> getCommands() {
		return commands;
	}
	
}
