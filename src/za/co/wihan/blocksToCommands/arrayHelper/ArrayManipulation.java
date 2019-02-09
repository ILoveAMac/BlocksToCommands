package za.co.wihan.blocksToCommands.arrayHelper;

public class ArrayManipulation {

	public String combineArgs(String[] args, int start) {
		StringBuilder buffer = new StringBuilder();
		// Combine all values in array >= start
		for (int i = start; i < args.length; i++) {
			buffer.append(' ').append(args[i]);
		}
		return buffer.toString().trim();
	}
}
