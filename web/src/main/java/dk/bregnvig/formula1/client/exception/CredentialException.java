package dk.bregnvig.formula1.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CredentialException extends Exception implements IsSerializable {
	
	private static final long serialVersionUID = 4544564940923666651L;

	public CredentialException() {
		
	}
	
	public CredentialException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CredentialException(String arg0) {
		super(arg0);
	}

	public CredentialException(Throwable arg0) {
		super(arg0);
	}

}
