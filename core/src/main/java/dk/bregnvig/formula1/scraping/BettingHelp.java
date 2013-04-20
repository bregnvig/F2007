package dk.bregnvig.formula1.scraping;

import java.util.List;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;

public interface BettingHelp {
	
	int getLastYearsPole(Race race);

	List<Integer> getPrevious3StartingPositions(Season season);
	
	List<Integer> getPrevious3EndPositions(Season season);
}
