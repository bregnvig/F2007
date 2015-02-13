package dk.bregnvig.formula1.client.domain.bid;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import dk.bregnvig.formula1.client.domain.ClientDriver;

@JsonIgnoreProperties
public class ClientResult extends ClientBid {

	private ClientDriver[] firstCrashes;

	public ClientDriver[] getFirstCrashes() {
		return firstCrashes;
	}

	public void setFirstCrashes(ClientDriver[] firstCrashes) {
		this.firstCrashes = firstCrashes;
	}
	
	
	
}
