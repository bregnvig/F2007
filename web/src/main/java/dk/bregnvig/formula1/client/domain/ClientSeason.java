package dk.bregnvig.formula1.client.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientSeason implements Serializable {

	private String name;
	
	private List<ClientPlayer> players;
	private List<ClientRace> races;
	private Set<ClientDriver> drivers;
	private ClientRace currentRace;
	private Date lastestJoinDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ClientPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<ClientPlayer> players) {
		this.players = players;
	}
	
	public ClientPlayer findPlayerByPlayerName(String playerName) {
		for (int i = 0; i < players.size(); i++) {
			ClientPlayer player = (ClientPlayer) players.get(i);
			if (player.getPlayername().equals(playerName)) {
				return player;
			}
		}
		return null;
	}

	public List<ClientRace> getRaces() {
		return races;
	}

	public void setRaces(List<ClientRace> races) {
		this.races = races;
	}

	public Set<ClientDriver> getDrivers() {
		return drivers;
	}
	
	public List<ClientDriver> getActiveDriver() {
		List<ClientDriver> activeDrivers = new ArrayList<ClientDriver>();
		Iterator<ClientDriver> i = getDrivers().iterator();
		while (i.hasNext()) {
			ClientDriver driver = i.next();
			if (driver.isActive()) {
				activeDrivers.add(driver);
			}
		}
		Collections.sort(activeDrivers, new ClientDriver.NumberComparator());
		return activeDrivers;
	}

	public void setDrivers(Set<ClientDriver> drivers) {
		this.drivers = drivers;
	}

	public ClientRace getCurrentRace() {
		return currentRace;
	}

	public void setCurrentRace(ClientRace currentRace) {
		this.currentRace = currentRace;
	}

	public Date getLastestJoinDate() {
		return lastestJoinDate;
	}

	public void setLastestJoinDate(Date lastestJoinDate) {
		this.lastestJoinDate = lastestJoinDate;
	}
	

}
