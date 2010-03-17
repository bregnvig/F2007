package dk.bregnvig.formula1.client.domain;

import java.util.Comparator;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientPlayer extends Object implements IsSerializable {

	
	private static final long serialVersionUID = 1L;

	private String playername;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String smsNumber;
	private int balance;
	private boolean gameAdministrator;
	private boolean bankAdministrator;
	private boolean wbcParticipant;
	private boolean partOfSeason;
	private boolean bettingMoneyAvailable;
	private boolean reminderWanted;
	private int lastYearWBC;

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
	public String getName() {
		return firstName+" "+lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getSms() {
		return smsNumber;
	}
	public void setSms(String smsNumber) {
		this.smsNumber = smsNumber;
	}
	public boolean isGameAdministrator() {
		return gameAdministrator;
	}
	public void setGameAdministrator(boolean gameAdministrator) {
		this.gameAdministrator = gameAdministrator;
	}
	public boolean isBankAdministrator() {
		return bankAdministrator;
	}
	public void setBankAdministrator(boolean bankAdministrator) {
		this.bankAdministrator = bankAdministrator;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playername == null) ? 0 : playername.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass().getName().equals(obj.getClass().getName()) == false)
			return false;
		final ClientPlayer other = (ClientPlayer) obj;
		if (playername == null) {
			if (other.playername != null)
				return false;
		} else if (!playername.equals(other.playername))
			return false;
		return true;
	}
	
	public static class NameComparator implements Comparator<ClientPlayer>, IsSerializable {

		public int compare(ClientPlayer player0, ClientPlayer player1) {
			return (player0.getFirstName()+player0.getLastName()).compareTo(player1.getFirstName()+player1.getLastName());
		}
	}

	public int getBalance() {
		return balance;
	}
	public void setBalance(int amount) {
		this.balance = amount;
	}
	public boolean isPartOfSeason() {
		return partOfSeason;
	}
	public boolean isWbcParticipant() {
		return wbcParticipant;
	}
	public boolean isBettingMoneyAvailable() {
		return bettingMoneyAvailable;
	}
	public void setWbcParticipant(boolean wbcParticipant) {
		this.wbcParticipant = wbcParticipant;
	}
	public void setPartOfSeason(boolean partOfSeason) {
		this.partOfSeason = partOfSeason;
	}
	public void setBettingMoneyAvailable(boolean bidMoneyAvailable) {
		this.bettingMoneyAvailable = bidMoneyAvailable;
	}
	public boolean isReminderWanted() {
		return reminderWanted;
	}
	public void setReminderWanted(boolean reminderWanted) {
		this.reminderWanted = reminderWanted;
	}
	public int getLastYearWBC() {
		return lastYearWBC >= 1 && lastYearWBC  <= 3 ? lastYearWBC : 0;
	}
	public void setLastYearWBC(int lastYearWBC) {
		this.lastYearWBC = lastYearWBC;
	}
}
