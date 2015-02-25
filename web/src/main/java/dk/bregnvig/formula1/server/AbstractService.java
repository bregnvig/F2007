package dk.bregnvig.formula1.server;

import java.util.List;

import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;
import dk.bregnvig.formula1.server.context.WebContext;

public class AbstractService {
	
	private static final long serialVersionUID = 6954680002967911646L;
	
	private WebContext context;

	protected WebContext getContext() {
		return context;
	}

	public void setContext(WebContext context) {
		this.context = context;
	}
}
