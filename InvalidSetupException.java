public class InvalidSetupException extends Exception {
	public InvalidSetupException(String[] line) {
		super("Invalid parameters at line " + line[0] + "\n" +
			  String.join(" ", line));
	}
}