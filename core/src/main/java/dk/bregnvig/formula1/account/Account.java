package dk.bregnvig.formula1.account;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Account {
	
	private Long id;
	private BigDecimal balance = BigDecimal.ZERO;
	private List<Entry> entries = new LinkedList<Entry>();

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable=false)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public void deposit(String message, BigDecimal amount) {
		balance = balance.add(amount);
		addEntry(new DepositEntry(), message, amount);
	}

	public void withdraw(String message, BigDecimal amount) {
		if (verifyWithdraw(amount) == false) {
			throw new NotEnoughMoney(balance, amount);
		}
		balance = balance.subtract(amount.abs());
		addEntry(new WithdrawEntry(), message, amount.abs().negate());
	}
	
	public boolean verifyWithdraw(BigDecimal amount) {
		return balance.subtract(amount.abs()).intValue() >= getMinBalance();
	}
	
	public void transfer(String message, BigDecimal amount, Account toAccount) {
		if (verifyWithdraw(amount) == false) {
			throw new NotEnoughMoney("Transfer to: " + toAccount.id, balance, amount);
		}
		
		balance = balance.subtract(amount.abs());
		TransferEntry entry = new TransferEntry();
		entry.setToAccount(toAccount);
		entry.setFromAccount(this);
		addEntry(entry, message, amount.abs());
		toAccount.balance = toAccount.balance.add(amount);
		toAccount.addEntry(entry, message, amount);
	}
	

	private void addEntry(Entry entry, String message, BigDecimal amount) {
		entry.setMessage(message);
		entry.setAmount(amount);
		entries.add(0, entry);
	}
	
	@Entity
	@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING)
	@Table(name="account_entry")
	public static abstract class Entry {
		
		private Long id;
		private Calendar date;
		private String message;
		private BigDecimal amount;
		
		public Entry() {
			this.date = Calendar.getInstance();
		}

		@Id
		@GeneratedValue
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@Column(nullable=false)
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		
		@Column(nullable=false)
		@Temporal(value=TemporalType.TIMESTAMP)
		public Calendar getDate() {
			return date;
		}
		public void setDate(Calendar date) {
			this.date = date;
		}
		
		@Column(length=50)
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	@Entity
	@DiscriminatorValue(value="deposit")
	public static class DepositEntry extends Entry {
		
	}

	@Entity
	@DiscriminatorValue(value="withdraw")
	public static class WithdrawEntry extends Entry {
	}
	
	@Entity
	@DiscriminatorValue(value="transfer")
	public static class TransferEntry extends Entry {
		
		private Account toAccount;
		private Account fromAccount;

		@ManyToOne
		@JoinColumn(name="to_account_id")
		public Account getToAccount() {
			return toAccount;
		}

		public void setToAccount(Account toAccount) {
			this.toAccount = toAccount;
		}

		@ManyToOne
		@JoinColumn(name="from_account_id")
		public Account getFromAccount() {
			return fromAccount;
		}

		public void setFromAccount(Account fromAccount) {
			this.fromAccount = fromAccount;
		}
	}
	
	private class EntryDateComparator implements Comparator<Entry> {

		public int compare(Entry entry0, Entry entry1) {
			return entry0.getDate().compareTo(entry1.getDate());
		}
		
	}

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="account_entry_link")
	public List<Entry> getEntries() {
		Collections.sort(this.entries, Collections.reverseOrder(new EntryDateComparator()));
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	@Transient
	public int getMinBalance() {
		return -100;
	}
}
