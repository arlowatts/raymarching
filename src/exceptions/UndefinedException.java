package raymarching.exceptions;

public class UndefinedException extends Exception {
	public UndefinedException(String undef) {
		super(undef + " is not defined or does not exist.");
	}
}
