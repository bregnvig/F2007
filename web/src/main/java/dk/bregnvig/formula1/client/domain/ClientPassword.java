package dk.bregnvig.formula1.client.domain;

import java.io.Serializable;

public class ClientPassword implements Serializable {

	private static final long serialVersionUID = 1L;

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
