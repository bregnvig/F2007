package dk.bregnvig.formula1.service;

public interface SMSService {
	
	/**
	 * Sends a sms to the specified phone number
	 * @param phoneNumber
	 * @param message
	 */
	void sendSMS(String phoneNumber, String message);
}
