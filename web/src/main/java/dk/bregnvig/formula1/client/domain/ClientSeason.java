package dk.bregnvig.formula1.client.domain;

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

	public List getRaces() {
		return races;
	}

	public void setRaces(List races) {
		this.races = races;
	}
}
