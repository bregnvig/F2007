package dk.bregnvig.formula1.client.domain.account;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientAccount implements IsSerializable {

	private int balance;
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.account.ClientAccountEntry>
	 */
	private List entries;

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public List getEntries() {
		return entries;
	}

	public void setEntries(List entries) {
		this.entries = entries;
	}
}
