package dk.bregnvig.formula1.wbc;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.bregnvig.formula1.event.AbstractRaceListener;
import dk.bregnvig.formula1.service.SMSService;
import dk.bregnvig.formula1.wbc.WBC.Entry;

public class SMSCongratulator extends AbstractRaceListener{
	
	private SMSService service;
	private VelocityEngine velocityEngine;

	@Override
	public void raceClosed() {
	}

	@Override
	public void raceCompleted() {
		WBC wbc = getRace().getSeason().getWBC();
		
		for (Entry entry : wbc.getPreviousRaceEntries()) {
			if (entry.getPlayer().getSms() != null && entry.getPlayer().getSms().length() != 0) {
				Map<String, Object> model = new HashMap<String, Object>(3);
				model.put("entry", entry);
				service.sendSMS(entry.getPlayer().getSms(), VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/smsResult.vm", model));
			}
		}
	}

	@Override
	public void raceOpened() {
	}

	public void setSMSService(SMSService service) {
		this.service = service;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}
