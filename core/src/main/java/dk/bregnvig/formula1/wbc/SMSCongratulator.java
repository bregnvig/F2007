package dk.bregnvig.formula1.wbc;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.event.RaceListener;
import dk.bregnvig.formula1.sms.SMSGateway;
import dk.bregnvig.formula1.wbc.WBC.Entry;

public class SMSCongratulator implements RaceListener {
	
	private SMSGateway gateway;
	private VelocityEngine velocityEngine;

	@Required
	public void setSMSGateway(SMSGateway gateway) {
		this.gateway = gateway;
	}

	@Required
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	public void raceClosed(Race race) {
	}

	@Override
	public void raceCompleted(Race race) {
		WBC wbc = race.getSeason().getWBC();
		
		for (Entry entry : wbc.getPreviousRaceEntries()) {
			if (entry.getPlayer().getSms() != null && entry.getPlayer().getSms().length() != 0) {
				Map<String, Object> model = new HashMap<String, Object>(3);
				model.put("entry", entry);
				gateway.sendSMS(entry.getPlayer().getSms(), VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/smsResult.vm", model));
			}
		}
	}

	@Override
	public void raceOpened(Race race) {
	}

	@Override
	public void raceRolledBack(Race race) {
	}
}
