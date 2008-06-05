package dk.bregnvig.formula1.client.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientSeason implements IsSerializable{

	private String name;
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientPlayer>
	 */
	private List players;

	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientRace>
	 */
	private List races;

	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientDriver>
	 */
	private List drivers;

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

	public List getDrivers() {
		return drivers;
	}
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientDriver>
	 */
	public List getActiveDriver() {
		List activeDrivers = new ArrayList();
		Iterator i = getDrivers().iterator();
		while (i.hasNext()) {
			ClientDriver driver = (ClientDriver) i.next();
			if (driver.isActive()) {
				activeDrivers.add(driver);
			}
		}
		return activeDrivers;
	}

	public void setDrivers(List drivers) {
		this.drivers = drivers;
	}
}
