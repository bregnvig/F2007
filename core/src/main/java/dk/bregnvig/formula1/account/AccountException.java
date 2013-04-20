package dk.bregnvig.formula1.account;

public class AccountException extends RuntimeException {

	private static final long serialVersionUID = -973179118888943926L;

	public AccountException() {
		super();
	}

	public AccountException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AccountException(String arg0) {
		super(arg0);
	}

	public AccountException(Throwable arg0) {
		super(arg0);
	}

}
