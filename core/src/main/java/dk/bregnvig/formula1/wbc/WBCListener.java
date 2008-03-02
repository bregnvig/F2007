package dk.bregnvig.formula1.wbc;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.event.AbstractRaceListener;

public class WBCListener extends AbstractRaceListener{
	
	
	
	@Override
	public void raceClosed() {
	}

	@Override
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void raceCompleted() {
		
		WBC wbc = getRace().getSeason().getWBC();
		wbc.raceCompleted(getRace());
	}

	@Override
	public void raceOpened() {
	}
}
