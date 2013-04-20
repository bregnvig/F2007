package dk.bregnvig.formula1.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.sms.SMSGateway;

public class SmsReminderService extends AbstractReminderService {

	private static Locale DA_DK = new Locale("da", "DK");
	private static SimpleDateFormat hours = new SimpleDateFormat("HH:mm", DA_DK);
	
	private Log log = LogFactory.getLog(SmsReminderService.class);
	private SMSGateway gateway;

	public long getDelay(Race race) {
		return (race.getClose().getTime().getTime() - getInternalDelay()) - new Date().getTime(); 
	}

	@Required
	public void setGateway(SMSGateway gateway) {
		this.gateway = gateway;;
	}

	@Override
	void sendReminder(final Race race, final Player player) {
			if (player.isSmsAvailable()) {
				log.info("Sending reminder to " + player.getSms() + " for race " + race.getName());
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("player", player);
				model.put("race", race);
				model.put("time", hours.format(race.getClose().getTime()));
				String message = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(), "templates/smsReminderMessage.vm", model);
				this.gateway.sendSMS(player.getSms(), message);
			}
	}
}