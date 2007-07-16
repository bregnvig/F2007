package dk.bregnvig.formula1.wbc;

import dk.bregnvig.formula1.event.AbstractRaceListener;

public class WBCListener extends AbstractRaceListener{

	@Override
	public void raceClosed() {
	}

	@Override
	public void raceCompleted() {
		WBC wbc = getRace().getSeason().getWBC();
		wbc.raceCompleted(getRace());
	}

	@Override
	public void raceOpened() {
	}
}
