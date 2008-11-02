package dk.bregnvig.formula1.account;

import java.math.BigDecimal;

public class NotEnoughMoneyException extends AccountException {

	private static final long serialVersionUID = -5600846604510804687L;

	private BigDecimal balance;
	private BigDecimal amount;
	private String message;
	
	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public NotEnoughMoneyException(BigDecimal balance, BigDecimal amount) {
		this.balance = balance;
		this.amount = amount;
	}

	public NotEnoughMoneyException(String message, BigDecimal balance, BigDecimal amount) {
		this(balance, amount);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Could not withdraw amount: " + amount + ". Balance: " + balance + (message == null ? "" : " Additional message: " + message) ;
	}
	
	
}
