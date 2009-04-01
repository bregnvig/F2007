package dk.bregnvig.formula1.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.util.PlayerMessagePreparator;

public class NoMoreMoneyReminderService implements RaceListener {
	
	private Log log = LogFactory.getLog(EmailReminderService.class);
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private String fromAddress;

	private void sendWarningEmail(final Race race, final Player player) {
		try {
			log.info("Sending 'no money reminder' to " + player.getEmailAddress() + " for race " + race.getName());
			this.mailSender.send(new WarningMessagePreparator(player));
		} catch (MailException e) {
			log.error("Sending 'no money reminder' failed to " + player.getEmailAddress() + " for race " + race.getName(), e);
		}
	}
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public void raceClosed(Race race) {
	}

	public void raceCompleted(Race race) {
		Collection<Bid> bids = race.getBids();
		for (Bid bid : bids) {
			Player player = bid.getPlayer();
			if (player.getAccount().isBettingMoneyAvailable() == false && player.isEmailAddressAvailable()) {
				sendWarningEmail(race, player);
			}
		}
	}

	public void raceOpened(Race race) {
	}
	
	private class WarningMessagePreparator implements PlayerMessagePreparator {
		
		private Player player;

		public WarningMessagePreparator(Player player) {
			this.player = player;
		}

		public void prepare(MimeMessage mimeMessage) throws Exception {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setTo(player.getEmailAddress());
			message.setFrom(fromAddress);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("player", player);
			message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/noMoreMoneyReminderSubject.vm", model));
			message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/noMoreMoneyReminderBody.vm", model), true);
		}
		
		public Player getPlayer() {
			return player;
		}
	}
}
