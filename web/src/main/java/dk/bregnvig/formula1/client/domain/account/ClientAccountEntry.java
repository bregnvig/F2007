package dk.bregnvig.formula1.client.domain.account;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientAccountEntry implements IsSerializable {

	private int amount;
	private Date date;
	private String message;
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
