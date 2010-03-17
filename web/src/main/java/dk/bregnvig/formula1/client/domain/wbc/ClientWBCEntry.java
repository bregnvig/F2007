package dk.bregnvig.formula1.client.domain.wbc;

import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;

public class ClientWBCEntry implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	private ClientPlayer player;
	private ClientRace race;
	private int points;
	
	public ClientPlayer getPlayer() {
		return player;
	}
	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}
	public int getPoints() {
		return points;
		
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public ClientRace getRace() {
		return race;
	}
	public void setRace(ClientRace race) {
		this.race = race;
	}

}
