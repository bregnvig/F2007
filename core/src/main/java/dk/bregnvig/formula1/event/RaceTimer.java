package dk.bregnvig.formula1.event;

import dk.bregnvig.formula1.Race;


public interface RaceTimer {

	/**
	 * Returns the amount of milliseconds before this race the timer should executed. This is a 
	 * combination of dyas, hours and minutes
	 * @return
	 */
	int getBeforeInMillis();

	/**
	 * Sets the race this timer is all about
	 * @param race
	 */
	void setRace(Race race);
	
	/**
	 * Sets the amount of hours before race start this timer should be executed. 
	 * Might be combined with the minutes and the days
	 * @param hours
	 */
	void setHours(int hours);

	/**
	 * Sets the amount of minutes before race start this timer should be executed
	 * Might be combined with the hours and the days
	 * @param minutes
	 */
	void setMinutes(int minutes);

	/**
	 * Sets the amount of days before race start this timer should be executed
	 * Might be combined with the hours and the minutes
	 * @param days
	 */
	void setDays(int days);
	
	/**
	 * Being called when the timer has timed out
	 */
	void invoke();
}
