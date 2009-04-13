package dk.bregnvig.formula1.event;

import dk.bregnvig.formula1.Race;

public interface RaceListener {

	/**
	 * Is being called when the race is being opened for bids
	 * @param race
	 */
	void raceOpened(Race race);
	
	/**
	 * Is being called when a race is being closed for anymore bids
	 * @param race
	 */
	void raceClosed(Race race);
	
	/**
	 * Is being called when a race is completed. Meaning the result has been send in
	 * @param race
	 */
	void raceCompleted(Race race);

	/**
	 * Is being called if a race is being rolled back.
	 * @param race
	 */
	void raceRolledBack(Race race);
}
