package raymarching.exceptions;

public class InvalidSetupException extends Exception {
	public InvalidSetupException(String[] line) {
		super("Invalid parameters at line " + line[0] + "\n" +
			  String.join(" ", line));
	}
	
	public static void assertLength(String[] line, int length) throws InvalidSetupException {
		if (line.length != length) throw new InvalidSetupException(line);
	}
}
