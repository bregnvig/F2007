package dk.bregnvig.formula1.autoresult;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;

public interface AutomaticResult {
	
	void setURL(String url);
	
	RaceResult getRaceResult(Race race);

}
