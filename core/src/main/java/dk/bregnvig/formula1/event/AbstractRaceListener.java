package dk.bregnvig.formula1.event;

import java.util.Timer;
import java.util.TimerTask;

import dk.bregnvig.formula1.Race;

public abstract class AbstractRaceListener {
	
	private Race race;
	private Timer timer;
	
	public void setRace(Race race) {
		timer = new Timer(true);
		this.race = race;
		TimerTask openTask = new TimerTask() {
			@Override
			public void run() {
				raceOpened();
			}
		};
		timer.schedule(openTask, race.getOpen().getTime());
		TimerTask closeTask = new TimerTask() {
			@Override
			public void run() {
				raceClosed();
			}
		};
		timer.schedule(closeTask, race.getClose().getTime());		
	}
	
	public void cancel() {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	protected Race getRace() {
		return race;
	}
	
	public abstract void raceOpened();

	public abstract void raceClosed();
	
	public abstract void raceCompleted();
}
