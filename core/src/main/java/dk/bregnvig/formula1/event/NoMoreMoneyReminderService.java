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
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Player;

public class NoMoreMoneyReminderService extends AbstractRaceListener {
	
	private Log log = LogFactory.getLog(OneWeekReminderService.class);
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private String fromAddress;

	@Override
	public void raceClosed() {
	}

	@Override
	public void raceCompleted() {
		Collection<Bid> bids = getRace().getBids();
		for (Bid bid : bids) {
			Player player = bid.getPlayer();
			if (player.getAccount().isBettingMoneyAvailable() == false && player.isEmailAddressAvailable()) {
				sendWarningEmail(player);
			}
		}
	}

	@Override
	public void raceOpened() {
	}

	private void sendWarningEmail(final Player player) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(player.getEmailAddress());
				message.setFrom(fromAddress);
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("player", player);
				message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/noMoreMoneyReminderSubject.vm", model));
				message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/noMoreMoneyReminderBody.vm", model), true);
			}
		};
		try {
			log.info("Sending 'no money reminder' to " + player.getEmailAddress() + " for race " + getRace().getName());
			this.mailSender.send(preparator);
		} catch (MailException e) {
			log.error("Sending 'no money reminder' failed to " + player.getEmailAddress() + " for race " + getRace().getName(), e);
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
}
