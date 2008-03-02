package dk.bregnvig.formula1.client.domain.bid;

import dk.bregnvig.formula1.client.domain.ClientDriver;

public class ClientResult extends ClientBid {

	private ClientDriver[] firstCrashes;

	public ClientDriver[] getFirstCrashes() {
		return firstCrashes;
	}

	public void setFirstCrashes(ClientDriver[] firstCrashes) {
		this.firstCrashes = firstCrashes;
	}
	
	
	
}
