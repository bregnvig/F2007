package dk.bregnvig.formula1.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.bregnvig.formula1.Race;

public abstract class AbstractRaceTimer implements RaceTimer {

	private static final int SECOND = 1000;
	private static final int MINUTE = SECOND*60;
	private static final int HOUR = MINUTE*60;
	private static final int DAY = HOUR*24;
	
	private Log log = LogFactory.getLog(AbstractRaceListener.class);

	private int seconds = -1;
	private int days = -1;
	private int hours = -1;
	private int minuttes = -1;
	private Race race;

	public int getBeforeInMillis() {
		int total = -1;
		if (days != -1) {
			total = days * DAY;
		}
		if (hours != -1) {
			total += hours*HOUR;
		}
		
		if (minuttes != -1) {
			total += minuttes*MINUTE;
		}
		if (seconds != -1) {
			total += seconds*SECOND;
		}		
		if (total == -1) {
			String message = "Timer not set for" + this.getClass().getName();
			log.info(message);
			throw new IllegalStateException(message);
		}
		return total;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public void setMinutes(int minuttes) {
		this.minuttes = minuttes;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public void setRace(Race race) {
		this.race = race;
	}
	
	protected Race getRace() {
		return race;
	}

}
