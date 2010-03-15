package dk.bregnvig.formula1.client.domain.wbc;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.ClientRace;

public class ClientHistory implements IsSerializable {

	public ClientRace race;
	public List<ClientPlayerPosition> positions = new ArrayList<ClientPlayerPosition>();
}
