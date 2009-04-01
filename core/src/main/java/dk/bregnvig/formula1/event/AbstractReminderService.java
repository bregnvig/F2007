package dk.bregnvig.formula1.event;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;

public abstract class AbstractReminderService implements RaceTimer {

	private Log log = LogFactory.getLog(AbstractReminderService.class);
	private VelocityEngine velocityEngine;
	private int hours;
	private int days;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void invoke(Race race) {
		log.info(this.getClass().getName() + " started");
		Collection<Player> players = getNonplayingPlayers(race);
		for (Player player : players) {
			sendReminder(race, player);
		}
	}
	
	abstract void sendReminder(Race race, Player player);
	
	Collection<Player> getNonplayingPlayers(Race race) {
		Collection<Player> result = new HashSet<Player>();
		result.addAll(race.getSeason().getPlayers());

		for (Bid bid : race.getBids()) {
			result.remove(bid.getPlayer());
		}
		return result;
	}

	@Required
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	long getInternalDelay() {
		return RaceTimer.DAY * days + RaceTimer.HOUR * hours;
	}

}

