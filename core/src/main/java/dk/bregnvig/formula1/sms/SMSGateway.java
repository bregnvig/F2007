package dk.bregnvig.formula1.sms;

import java.util.Collection;

/**
 * Methods to support an SMS gatemway 
 * @author bregnvig
 *
 */
public interface SMSGateway {
	
	void sendSMS(String number, String message);
	
	void sendSMS(Collection<String> numbers, String message);
	
	/**
	 * The gateway sends who this SMS is from
	 * @param from
	 */
	void setFrom(String from);
	
	/**
	 * Status is sent to this URL. Optional method
	 * @param url
	 */
	void setStatusURL(String url);
}
