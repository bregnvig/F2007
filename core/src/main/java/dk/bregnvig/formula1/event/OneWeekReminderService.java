package dk.bregnvig.formula1.event;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
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

public class OneWeekReminderService extends AbstractRaceTimer {

	private static Locale DA_DK = new Locale("da", "DK");
	private static SimpleDateFormat weekday = new SimpleDateFormat("EEEEE", DA_DK);
	private static SimpleDateFormat hours = new SimpleDateFormat("HH:mm", DA_DK);
	
	private Log log = LogFactory.getLog(OneWeekReminderService.class);
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private String fromAddress;

	public void invoke() {
		log.info("OneWeekReminderService started");
		Collection<Player> players = getNonplayingPlayers();
		for (Player player : players) {
			sendConfirmationEmail(player);
		}
	}

	public void setJavaMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	private Collection<Player> getNonplayingPlayers() {
		Collection<Player> result = new HashSet<Player>();
		result.addAll(getRace().getSeason().getPlayers());

		for (Bid bid : getRace().getBids()) {
			result.remove(bid.getPlayer());
		}
		return result;
	}

	private void sendConfirmationEmail(final Player player) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(player.getEmailAddress());
				message.setFrom(fromAddress);
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("player", player);
				model.put("race", getRace());
				model.put("weekday", weekday.format(getRace().getClose().getTime()));
				model.put("time", hours.format(getRace().getClose().getTime()));
				message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/oneWeekReminderSubject.vm", model));
				message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/oneWeekReminderBody.vm", model), true);
			}
		};
		try {
			log.info("Sending reminder to " + player.getEmailAddress() + " for race " + getRace().getName());
			this.mailSender.send(preparator);
		} catch (MailException e) {
			log.error("Sending failed to " + player.getEmailAddress() + " for race " + getRace().getName());
		}
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

}
