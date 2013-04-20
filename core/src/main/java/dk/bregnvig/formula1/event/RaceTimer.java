package dk.bregnvig.formula1.event;

import dk.bregnvig.formula1.Race;


public interface RaceTimer {
	
	static final int SECOND = 1000;
	static final int MINUTE = SECOND*60;
	static final int HOUR = MINUTE*60;
	static final int DAY = HOUR*24;

	/**
	 * Being called when the timer has timed out
	 */
	void invoke(Race race);
	
	/**
	 * Returns the delay from now until the timer should be invoked. The delay is specified in milliseconds.
	 * A negative number indicates the event should already have occurred.
	 * @param race
	 * @return
	 */
	long getDelay(Race race);
}
