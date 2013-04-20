package dk.bregnvig.formula1.service;

import java.util.List;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.event.RaceListener;
import dk.bregnvig.formula1.event.RaceTimer;

public interface EventService {
	
	public void initialize();
	
	/**
	 * Races calls this method when they are completed
	 * @param race
	 */
	void raceCompleted(Race race);

	/**
	 * Races calls this method if they are rolled back
	 * @param race
	 */
	void raceRolledBack(Race race);
	
	List<RaceListener> getListeners();
	void setListeners(List<RaceListener> listeners);
	void setTimers(List<RaceTimer> timers);
	List<RaceTimer> getTimers();

}
