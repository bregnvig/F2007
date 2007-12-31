package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class PhoneNumberRule extends AbstractRule {

	public PhoneNumberRule(TextBoxBase field, Label labelProperty) {
		super(field, labelProperty);
	}

	public PhoneNumberRule(TextBoxBase field, String property) {
		super(field, property);
	}

	protected String getErrorMessage() {
		return "{0} skal være danske mobil nummer på 8 numeriske karaktere";
	}

	public List validate() {
		
		if (getField().getText().length() == 0) {
			return Collections.EMPTY_LIST;
		}
		
		if (getField().getText().length() != 8) {
			return getIssue();
		}
		try {
			Integer.parseInt(getField().getText());
		} catch (NumberFormatException e) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}

}
