package dk.bregnvig.formula1.client.domain.account;

import java.io.Serializable;
import java.util.List;

public class ClientAccount implements Serializable {

	private int balance;
	
	private List<ClientAccountEntry> entries;

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public List<ClientAccountEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ClientAccountEntry> entries) {
		this.entries = entries;
	}
}
