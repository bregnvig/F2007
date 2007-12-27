package dk.bregnvig.formula1.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientPlayer implements IsSerializable {

	
	private static final long serialVersionUID = 1L;

	private String playername;
	private String firstName;
	private String lastName;
	private String emailAddress;

	public String getPlayername() {
		return playername;
	}
	public void setPlayername(String playername) {
		this.playername = playername;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}	
}
