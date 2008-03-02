package dk.bregnvig.formula1.client.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.bid.ClientBid;

public class ClientRace implements IsSerializable{

	private Long id;

	private String name;
	private boolean opened;
	private boolean completed;
	private Date openDate;
	private boolean participant;
	private ClientDriver selectedDriver;
	
	public ClientRace() {
		
	}
	
	public ClientRace(boolean participant) {
		this.participant = participant;
	}

	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.bid.ClientBid>
	 */
	private List bids = new ArrayList();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date opens) {
		this.openDate = opens;
	}
	
	public static class OpensComparator implements Comparator, IsSerializable {

		public int compare(Object arg0, Object arg1) {
			ClientRace race0 = (ClientRace) arg0;
			ClientRace race1 = (ClientRace) arg1;
			
			return race0.getOpenDate().compareTo(race1.getOpenDate());
		}
	}
	
	/**
	 * Returns true if this player currently logged in  is already a participant
	 * @return
	 */
	public boolean isParticipant() {
		return participant;
	}
	
	/**
	 * Change the participant state for the race to true
	 */
	public void participant() {
		this.participant = true;
	}

	public List getBids() {
		return bids;
	}
	
	public void addBid(ClientBid bid) {
		bids.add(bid);
	}

	public ClientDriver getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(ClientDriver selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

	public boolean isOpened() {
		return opened;
	}
	
	public boolean isWaiting() {
		return new Date().before(openDate);
	}

	public void setOpened(boolean open) {
		this.opened = open;
	}
	
	public boolean isClosed() {
		return isOpened() == false && isCompleted() == false;
	}
	
}
