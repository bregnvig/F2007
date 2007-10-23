package dk.bregnvig.formula1.server.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.server.AbstractService;
import dk.bregnvig.formula1.service.SeasonService;

public class Initialize implements ServletContextListener {
	
	private ServletContext context;

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
		SeasonService service = (SeasonService) applicationContext.getBean("seasonService");
		
		Season season = service.findByName(context.getInitParameter("seasonName"));
		
		if (season == null) {
			throw new IllegalStateException("The season: " + context.getInitParameter("seasonName") + " could not be found");
		}
		
		context.setAttribute(AbstractService.SEASON, season);
	}
}
