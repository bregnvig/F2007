package dk.bregnvig.formula1.client.domain.bid;

import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class ClientBid implements IsSerializable {

	private ClientPlayer player;

	public ClientPlayer getPlayer() {
		return player;
	}

	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}
	
}
