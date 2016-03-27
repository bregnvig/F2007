package dk.bregnvig.formula1.server.restful.model;

import java.io.Serializable;

public class AccountOperation implements Serializable {
	
	public static enum Operation {
		DEPOSIT, WITHDRAW, TRANSFER
	}
	
	private Operation operation;
	private String message;
	private int amount;
	private String receiver;

	public AccountOperation() {
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
