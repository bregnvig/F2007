package dk.bregnvig.formula1.client.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientSeason implements IsSerializable{

	private String name;
	
	private List<ClientPlayer> players;
	private List<ClientRace> races;
	private Set<ClientDriver> drivers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getPlayers() {
		return players;
	}

	public void setPlayers(List players) {
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

	public List getRaces() {
		return races;
	}

	public void setRaces(List races) {
		this.races = races;
	}

	public Set<ClientDriver> getDrivers() {
		return drivers;
	}
	
	public List<ClientDriver> getActiveDriver() {
		List activeDrivers = new ArrayList();
		Iterator i = getDrivers().iterator();
		while (i.hasNext()) {
			ClientDriver driver = (ClientDriver) i.next();
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
}
