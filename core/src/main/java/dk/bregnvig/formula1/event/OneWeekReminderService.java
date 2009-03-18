package dk.bregnvig.formula1.event;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.util.PlayerMessagePreparator;

public class OneWeekReminderService implements RaceTimer {

	private static Locale DA_DK = new Locale("da", "DK");
	private static SimpleDateFormat weekday = new SimpleDateFormat("EEEEE", DA_DK);
	private static SimpleDateFormat hours = new SimpleDateFormat("HH:mm", DA_DK);
	
	private Log log = LogFactory.getLog(OneWeekReminderService.class);
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private String fromAddress;

	public void invoke(Race race) {
		log.info("OneWeekReminderService started");
		Collection<Player> players = getNonplayingPlayers(race);
		for (Player player : players) {
			sendConfirmationEmail(race, player);
		}
	}
	
	public int getDelay(Race race) {
		return (int) ((race.getOpen().getTime().getTime() - RaceTimer.DAY * 7) - new Date().getTime()); 
	}

	@Required
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Required
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	private Collection<Player> getNonplayingPlayers(Race race) {
		Collection<Player> result = new HashSet<Player>();
		result.addAll(race.getSeason().getPlayers());

		for (Bid bid : race.getBids()) {
			result.remove(bid.getPlayer());
		}
		return result;
	}

	private void sendConfirmationEmail(final Race race, final Player player) {
		try {
			log.info("Sending reminder to " + player.getEmailAddress() + " for race " + race.getName());
			this.mailSender.send(new OneWeekReminderPreparator(race, player));
		} catch (MailException e) {
			log.error("Sending failed to " + player.getEmailAddress() + " for race " + race.getName(), e);
		}
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	private class OneWeekReminderPreparator implements PlayerMessagePreparator {
		
		private Player player;
		private Race race;
		
		public OneWeekReminderPreparator(Race race, Player player) {
			this.race = race;
			this.player = player;
		}
		
		public void prepare(MimeMessage mimeMessage) throws Exception {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setTo(player.getEmailAddress());
			message.setFrom(fromAddress);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("player", player);
			model.put("race", race);
			model.put("weekday", weekday.format(race.getClose().getTime()));
			model.put("time", hours.format(race.getClose().getTime()));
			message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/oneWeekReminderSubject.vm", model));
			message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/oneWeekReminderBody.vm", model), true);
		}

		public Player getPlayer() {
			return player;
		}
	}

}
