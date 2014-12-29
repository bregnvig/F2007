package dk.bregnvig.formula1.wbc;

public class WBCException extends RuntimeException {

	public WBCException() {
	}

	public WBCException(String message) {
		super(message);
	}

	public WBCException(Throwable cause) {
		super(cause);
	}

	public WBCException(String message, Throwable cause) {
		super(message, cause);
	}
}
