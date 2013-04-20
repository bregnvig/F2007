package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class PhoneNumberRule extends AbstractTextRule {

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
		
		if (getTextField().getText().length() == 0) {
			return Collections.EMPTY_LIST;
		}
		
		if (getTextField().getText().length() != 8) {
			return getIssue();
		}
		try {
			Integer.parseInt(getTextField().getText());
		} catch (NumberFormatException e) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}

}
