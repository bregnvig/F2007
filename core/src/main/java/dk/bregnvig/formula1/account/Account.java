package dk.bregnvig.formula1.account;

import java.math.BigDecimal;
import java.util.Calendar;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.dao.GameDao;

@Entity
@Configurable
public class Account {

	private static BigDecimal bettingAmount = new BigDecimal(20);
	private static BigDecimal wbcAmount = new BigDecimal(100);

	private Log log = LogFactory.getLog(this.getClass()); 

	private Long id;
	private BigDecimal balance = BigDecimal.ZERO;
	private List<Entry> entries = new LinkedList<Entry>();
	private GameDao dao;

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
		Entry entry = new DepositEntry();
		addEntry(entry, message, amount);
	}

	public void withdraw(String message, BigDecimal amount) {
		if (verifyWithdraw(amount) == false) {
			throw new NotEnoughMoneyException(balance, amount);
		}
		balance = balance.subtract(amount.abs());
		Entry entry = new WithdrawEntry();
		addEntry(entry, message, amount.abs().negate());
	}
	
	public boolean verifyWithdraw(BigDecimal amount) {
		return balance.subtract(amount.abs()).intValue() >= getMinBalance();
	}
	
	public void transfer(String message, BigDecimal amount, Account toAccount) {
		if (verifyWithdraw(amount) == false) {
			throw new NotEnoughMoneyException("Transfer to: " + toAccount.id, balance, amount);
		}
		
		log.info("** Transfering from id: " + getId() + " to id: " + toAccount.id);
		
		balance = balance.subtract(amount.abs());
		TransferEntry entry = new TransferEntry();
		entry.setToAccount(toAccount);
		entry.setFromAccount(this);
		addEntry(entry, message, amount.abs());
		toAccount.balance = toAccount.balance.add(amount);
		toAccount.addEntry(entry, message, amount);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void participate(Race race) {
		Player bookie = dao.findPlayerByName("bookie");
		transfer("Deltagelse " + race.getName(), bettingAmount, bookie.getAccount());
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void joinWBC() {
		Player bookie = dao.findPlayerByName("bookie");
		transfer("Deltagelse i WBC", wbcAmount, bookie.getAccount());
	}
	
	public void winnings(Race race, int winners) {
		Player bookie = dao.findPlayerByName("bookie");
		bookie.getAccount().transfer("Gevinst " + race.getName(), new BigDecimal(getWinningsAmount(race, winners)), this);
	}

	public void rollback(Race race, int winners) {
		Player bookie = dao.findPlayerByName("bookie");
		transfer("Fejl i afgørelse " + race.getName(), new BigDecimal(getWinningsAmount(race, winners)), bookie.getAccount());
	}

	private int getWinningsAmount(Race race, int winners) {
		return race.getBids().size() * bettingAmount.intValue() / winners;
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

	@ManyToMany(cascade= {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinTable(name="account_entry_link")
	public List<Entry> getEntries() {
//		Collections.sort(this.entries, Collections.reverseOrder(new EntryDateComparator()));
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	@Transient
	public int getMinBalance() {
		return -100;
	}
	
	@Transient
	public int getBettingAmount() {
		return bettingAmount.intValue();
	}
	
	@Transient
	public boolean isBettingMoneyAvailable() {
		return getBalance().intValue() - bettingAmount.intValue() >= getMinBalance(); 
	}

	public void setDao(GameDao dao) {
		this.dao = dao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
