package dk.bregnvig.formula1.util;

import java.util.ArrayList;
import java.util.List;

public class SMSServiceDummyImpl implements SMSService {

	private List<String> numbers = new ArrayList<String>();
	private List<String> messages = new ArrayList<String>();
	
	public void sendSMS(String phoneNumber, String message) {
		numbers.add(phoneNumber);
		messages.add(message);
	}

	public List<String> getNumbers() {
		return numbers;
	}
	public List<String> getMessages() {
		return messages;
	}
}
