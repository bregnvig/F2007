package dk.bregnvig.formula1.client.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;

public class ClientRace implements Serializable{

	private Long id;

	private String name;
	private String circuitId;;
	private boolean opened;
	private boolean completed;
	private Date openDate;
	private Date closeDate;
	private boolean participant;
	private ClientDriver selectedDriver;
	private boolean fullyLoaded;
	private List<ClientBid> bids = new ArrayList<ClientBid>();
	
	private ClientResult result;
		
	public ClientRace() {
		
	}
	
	public ClientRace(boolean participant) {
		this.participant = participant;
	}

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
	public String getCircuitId() {
		return circuitId;
	}
	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
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
	
	public static class OpensComparator implements Comparator<ClientRace>, IsSerializable {

		public int compare(ClientRace race0, ClientRace race1) {
			return race0.getOpenDate().compareTo(race1.getOpenDate());
		}
	}
	
	/**
	 * Returns true if this player currently logged in  is already a participant
	 * @return
	 */
	public void setParticipant(boolean participant) {
		//Ignore it. Only created for flex
	}
	
	public boolean isParticipant() {
		return participant;
	}
	
	/**
	 * Change the participant state for the race to true
	 */
	public void participant() {
		this.participant = true;
	}

	public List<ClientBid> getBids() {
		return bids;
	}
	
	public void setBids(List<ClientBid> bids) {
		this.bids = bids;
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
		if (openDate == null) return false; // For the super light weight
		return new Date().before(openDate);
	}

	public void setOpened(boolean open) {
		this.opened = open;
	}
	
	public boolean isClosed() {
		return isWaiting() == false && isOpened() == false && isCompleted() == false;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public boolean isFullyLoaded() {
		return fullyLoaded;
	}

	public void setFullyLoaded(boolean fullyLoaded) {
		this.fullyLoaded = fullyLoaded;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass().equals(obj.getClass()) == false)
			return false;
		final ClientRace other = (ClientRace) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String toString() {
		return name + " Open: " + openDate + " Close: " + closeDate;
	}

	public ClientResult getRaceResult() {
		return result;
	}

	public void setRaceResult(ClientResult result) {
		this.result = result;
	}
	
}
