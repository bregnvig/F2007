package dk.bregnvig.formula1.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dk.bregnvig.formula1.sms.SMSGateway;

public class DummySMSGatewayImpl implements SMSGateway {

	private List<String> numbers = new ArrayList<String>();
	private List<String> messages = new ArrayList<String>();
	
	public void sendSMS(String phoneNumber, String message) {
		numbers.add(phoneNumber);
		messages.add(message);
		System.out.println("Sending to " + phoneNumber + " Message: " + message);
	}

	public List<String> getNumbers() {
		return numbers;
	}
	public List<String> getMessages() {
		return messages;
	}
	
	public boolean containsTheseValues(String userId, String points) {
		for (String message : messages) {
			if (message.contains(userId) && message.contains(points)) {
				return true;
			}
		}
		return false;
	}

	public void sendSMS(Collection<String> numbers, String message) {
		for (String number : numbers) {
			sendSMS(number, message);
		}
	}

	public void setFrom(String from) {
		// TODO Auto-generated method stub
		
	}

	public void setStatusURL(String url) {
		// TODO Auto-generated method stub
		
	}
}
