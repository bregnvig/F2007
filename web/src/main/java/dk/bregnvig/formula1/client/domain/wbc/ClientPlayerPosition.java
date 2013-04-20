package dk.bregnvig.formula1.client.domain.wbc;

import java.io.Serializable;

import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class ClientPlayerPosition implements Serializable {

	public ClientPlayer player;
	public int points;
	public int position;
}
