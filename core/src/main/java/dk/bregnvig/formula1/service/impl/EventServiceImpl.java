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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Transactional(readOnly = true, propagation=Propagation.REQUIRED)
	public void initialize() {
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
		}
		for (Race race : races) {
			for (RaceTimer timer : timers) {
				long delay = timer.getDelay(race);
				if (delay > 100) {
					log.info("Timer added for " + race.getName() + " for timer " + timer.getClass().getSimpleName() + " Delay: " + (delay/1000) + " seconds");
					this.timer.schedule(new InternalTimer(race, timer), delay);
				} else {
					log.info("Timer NOT added for " + race.getName() + " for timer " + timer.getClass().getSimpleName() + " Delay: " + (delay/1000) + " seconds");
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

	@Override
	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void raceCompleted(Race race) {
		for (RaceListener listener : listeners) {
			listener.raceCompleted(race);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void raceRolledBack(Race race) {
		for (RaceListener listener : listeners) {
			listener.raceRolledBack(race);
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

		long getDelay() {
			return race.getOpen().getTime().getTime() - new Date().getTime();
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
		
		long getDelay() {
			return race.getClose().getTime().getTime() - new Date().getTime();
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
}