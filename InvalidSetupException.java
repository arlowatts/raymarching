public class InvalidSetupException extends Exception {
	public InvalidSetupException(int lineNumber, String className, String line, String required) {
		super("Invalid " + className + " parameters on line " + lineNumber + ":\n " + "\t" + line + "\n" + className + " requires: " + required);
	}
}