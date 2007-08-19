package dk.bregnvig.formula1.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.bregnvig.formula1.util.SMSService;

/**
 * This class handles the sending of the SMS for real. Will only work if the service is available
 * The sending of SMS is not deemed very important so failures to send should not cause the problem to fail. 
 * It should be logged though 
 * $Revision: 1.2 $
 * $Date: 2007-08-19 12:25:37 $
 * @author 4u603ctpsz9
 */
public class SMSServiceImpl implements SMSService {

	private Log log = LogFactory.getLog(SMSService.class);
	private net.codeweasels.service.SMSService service;
	
	public void sendSMS(String phoneNumber, String message) {
		try {
			if (service.isServiceOnline() == false) {
				log.warn("SMS Service in running but phone not online.");
				return;
			}
			service.sendMessage(phoneNumber, message);
		} catch (Exception e) {
			log.warn("Unable to send message to " + phoneNumber);
		}
	}

	public void setService(net.codeweasels.service.SMSService service) {
		this.service = service;
	}

}
