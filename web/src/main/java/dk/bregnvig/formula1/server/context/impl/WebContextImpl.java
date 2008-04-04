package dk.bregnvig.formula1.server.context.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.server.context.WebContext;
import dk.bregnvig.formula1.service.GameService;

public class WebContextImpl implements ApplicationContextAware, WebContext {

	private ApplicationContext context;
	private ThreadLocal<Player> player = new ThreadLocal<Player>();
	private GameService service;
	private String seasonName;

	
	@Transactional(propagation = Propagation.SUPPORTS)
	public Season getSeason() {
		Season season = service.findByName(seasonName);
		if (season == null) {
			throw new IllegalStateException("The season: " + seasonName + " could not be found");
		}
		return season;
	}

	public void init() {
		service = (GameService) context.getBean("gameService");
		// Starts the event and timers
		service.findByName(seasonName).getRaces();
	}

	public Player getPlayer() {
		return player.get();
	}

	public void setPlayer(Player player) {
		this.player.set(player);
	}
	
	public void remove() {
		player.remove();
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
		init();
	}
}
