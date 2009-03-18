package dk.bregnvig.formula1.wbc;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.event.RaceListener;

public class WBCListener implements RaceListener {

	public void raceClosed(Race race) {
	}

	public void raceCompleted(Race race) {
		WBC wbc = race.getSeason().getWBC();
		wbc.raceCompleted(race);
	}

	public void raceOpened(Race race) {
	}
}
