package dk.bregnvig.formula1.service;

import dk.bregnvig.formula1.Race;

public interface EventService {
	
	/**
	 * Races calls this method when they are completed
	 * @param race
	 */
	void raceCompleted(Race race);

}
