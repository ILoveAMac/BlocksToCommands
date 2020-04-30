package me.ILoveAMac.BTC.util;

public class PlayerCommand {
	
	private String commandString;
	private boolean runAsConsole;
	
	
	public PlayerCommand(String commandString, boolean runAsConsole) {
		this.commandString = commandString;
		this.runAsConsole = runAsConsole;
	}
	
	public String getCommandString() {
		return commandString;
	}

	public boolean isRunAsConsole() {
		return runAsConsole;
	}
}
