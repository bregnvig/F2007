package dk.bregnvig.formula1.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.event.RaceListener;
import dk.bregnvig.formula1.event.RaceTimer;
import dk.bregnvig.formula1.service.EventService;
import dk.bregnvig.formula1.service.GameService;

public class EventServiceImpl implements EventService {
	
	private Log log = LogFactory.getLog(EventServiceImpl.class);
	
	private Timer timer = null;
	private List<RaceTimer> timers;
	private List<RaceListener> listeners;
	private GameService service;
	private String seasonName;
	private PropertyPlaceholderConfigurer bla;

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public void setService(GameService service) {
		this.service = service;
	}

	public void setTimers(List<RaceTimer> timers) {
		this.timers = timers;
	}
	
	public List<RaceTimer> getTimers() {
		return Collections.unmodifiableList(timers);
	}
	
	
	public void initialized() {
		Season season = service.findByName(seasonName);
		
		if (season == null) {
			//Must likely in test
			return;
		}
		createTimer();
		Collection<Race> races = season.getRaces();
		for (Race race : races) {
			OpenedTimer openedTimer = new OpenedTimer(race);
			if (openedTimer.isValid()) {
				log.info("Opened timer added for " + race.getName());
				timer.schedule(openedTimer, openedTimer.getDelay());
			}
			ClosedTimer closedTimer = new ClosedTimer(race);
			if (closedTimer.isValid()) {
				log.info("Closed timer added for " + race.getName());
				timer.schedule(closedTimer, closedTimer.getDelay());
			}
			for (RaceTimer timer : timers) {
				int delay = timer.getDelay(race);
				if (delay > 100) {
					log.info("Timer added for " + race.getName() + " for timer impl " + timer.getClass().getName());
					this.timer.schedule(new InternalTimer(race, timer), delay);
				}
			}
		}
	}

	@Required
	public void setListeners(List<RaceListener> listeners) {
		this.listeners = listeners;
	}

	public List<RaceListener> getListeners() {
		return Collections.unmodifiableList(listeners);
	}

	public void raceCompleted(Race race) {
		for (RaceListener listener : listeners) {
			listener.raceCompleted(race);
		}
	}
	
	private Timer createTimer() {
		if (timer != null) timer.cancel();
		return timer = new Timer(true);
	}
	
	private void raceOpened(Race race) {
		for (RaceListener listener : listeners) {
			listener.raceOpened(race);
		}
	}

	private void raceClosed(Race race) {
		for (RaceListener listener : listeners) {
			listener.raceClosed(race);
		}
	}

	private class OpenedTimer extends TimerTask {
		
		private Race race;
		
		public OpenedTimer(Race race) {
			this.race = race;
		}

		@Override
		public void run() {
			raceOpened(race);
		}

		int getDelay() {
			return (int) (race.getOpen().getTime().getTime() - new Date().getTime());
		}

		boolean isValid() {
			return new Date().before(race.getOpen().getTime());
		}
	}

	private class ClosedTimer extends TimerTask {
		
		private Race race;
		
		public ClosedTimer(Race race) {
			this.race = race;
		}

		@Override
		public void run() {
			raceClosed(race);
		}
		
		int getDelay() {
			return (int) (race.getClose().getTime().getTime() - new Date().getTime());
		}
		
		boolean isValid() {
			return new Date().before(race.getClose().getTime());
		}
	}
	
	private class InternalTimer extends TimerTask {

		private Race race;
		private RaceTimer timer;
		
		public InternalTimer(Race race, RaceTimer timer) {
			this.race = race;
			this.timer = timer;
		}
		
		@Override
		public void run() {
			timer.invoke(race);
		}
	}

	public PropertyPlaceholderConfigurer getBla() {
		return bla;
	}

	public void setBla(PropertyPlaceholderConfigurer bla) {
		this.bla = bla;
	}
}