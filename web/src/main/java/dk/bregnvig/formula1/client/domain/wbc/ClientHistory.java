package dk.bregnvig.formula1.client.domain.wbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.bregnvig.formula1.client.domain.ClientRace;

public class ClientHistory implements Serializable {

	public ClientRace race;
	public List<ClientPlayerPosition> positions = new ArrayList<ClientPlayerPosition>();
}
