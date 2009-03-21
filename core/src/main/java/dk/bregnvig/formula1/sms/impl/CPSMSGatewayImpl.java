package dk.bregnvig.formula1.sms.impl;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import dk.bregnvig.formula1.sms.SMSGateway;

public class CPSMSGatewayImpl implements SMSGateway {
	
	private Log log = LogFactory.getLog(CPSMSGatewayImpl.class);
	private String username;
	private String password;
	private String from;
	private String statusURL;
	
	public CPSMSGatewayImpl() {
	}

	public void sendSMS(String number, String message) {
		List<String> numbers = new ArrayList<String>(1);
		if (number != null) {
			numbers.add(number);
		}
		sendSMS(numbers, message);
	}

	public void sendSMS(Collection<String> numbers, String message) {
		
		validateState();
		
		StringBuilder uriString = new StringBuilder(200); 
		uriString.append("http://www.cpsms.dk/sms/");
		uriString.append("?username=").append(username);
		uriString.append("&password=").append(password);
		uriString.append(getNumbers(numbers));

		try {
			uriString.append("&from=").append(URLEncoder.encode(from, "ISO-8859-1"));
			uriString.append("&message=").append(URLEncoder.encode(message, "ISO-8859-1"));
			if (statusURL != null) {
				uriString.append("&url=").append(statusURL);
			}
			
			log.info("Send SMS - " + uriString);
			URL uri = new URL(uriString.toString());
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(uri.openStream());
			Element element = document.getRootElement();
			if ("succes".equals(element.getName())) {
				if (element.getValue().indexOf("0 recipient") != -1) {
					String warning = "Unable to send exception: " + element.getValue();
					log.warn(warning);
				}
				return;
			}
			String warning = "Unable to send exception: " + element.getValue();
			log.warn(warning);
		} catch (Exception e) {
			String warning = "Unable to send sms";
			log.warn(warning + " - " + e.getMessage());
		}
	}
	
	private void validateState() {
		if (username == null) {
			throw new IllegalStateException("Username must be set");
		}
		if (password == null) {
			throw new IllegalStateException("Passwiord must be set");
		}
		if (from == null) {
			throw new IllegalStateException("From must be set");
		}
	}
	
	private String getNumbers(Collection<String> numbers) {
		if (numbers == null || numbers.size() == 0) {
			throw new IllegalArgumentException("Must send to at least number");
		}
		StringBuilder result = new StringBuilder();
		for (String number : numbers) {
			result.append("&recipient[]=").append(number);
		}
		return result.toString();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setStatusURL(String statusURL) {
		if (statusURL.length() > 100) {
			throw new IllegalArgumentException("Status URL can not be longer than 100 characters");
		}
		this.statusURL = statusURL;
	}

	public void setUsername(String userId) {
		this.username = userId;
	}

	public void setFrom(String from) {
		if (from.length() > 11) {
			throw new IllegalArgumentException("from cannot be longer than 11 characters. Was '" + from + "'");
		}
		this.from = from;
	}
}
