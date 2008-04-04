package dk.bregnvig.formula1.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientWBCEntry implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	private ClientPlayer player;
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

}
