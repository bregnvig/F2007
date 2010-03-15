package dk.bregnvig.formula1.client.domain.wbc;

import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class ClientPlayerPosition implements IsSerializable {

	public ClientPlayer player;
	public int points;
	public int position;
}
