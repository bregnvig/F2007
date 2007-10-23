package dk.bregnvig.formula1.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import dk.bregnvig.formula1.Season;

public class AbstractService extends RemoteServiceServlet {
	
	private static final long serialVersionUID = 6954680002967911646L;

	public static final String SEASON = "seasonAttribute";
	
	Season getSeason() {
		return (Season) getServletContext().getAttribute(SEASON);
	}

}
