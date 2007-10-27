package dk.bregnvig.formula1.server.context.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.server.context.WebContext;
import dk.bregnvig.formula1.server.security.User;
import dk.bregnvig.formula1.service.SeasonService;

public class WebContextImpl implements ApplicationContextAware, WebContext {

	private ApplicationContext context;
	private ThreadLocal<User> user = new ThreadLocal<User>();
	private String seasonName;

	private static Season season;

	public Season getSeason() {
		return season;
	}

	public void init() {
		SeasonService service = (SeasonService) context.getBean("seasonService");

		season = service.findByName(seasonName);

		if (season == null) {
			throw new IllegalStateException("The season: " + seasonName + " could not be found");
		}
	}

	public User getUser() {
		return user.get();
	}

	public void setUser(User user) {
		this.user.set(user);
	}
	
	public void remove() {
		user.remove();
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
		init();
	}
}
