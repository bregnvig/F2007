package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class MaxLengthRule extends AbstractRule {

	private int maxLength;
		
	public MaxLengthRule(TextBoxBase field, Label labelProperty, int maxLength) {
		super(field, labelProperty);
		this.maxLength = maxLength;
	}

	public MaxLengthRule(TextBoxBase field, String property, int maxLength) {
		super(field, property);
		this.maxLength = maxLength;
	}

	protected String getErrorMessage() {
		return "{0} kan maximum være på {1} tegn/karakterer".replaceAll("\\{1\\}", Integer.toString(maxLength));
	}

	public List validate() {
		
		if (getField().getText().length() == 0) {
			return Collections.EMPTY_LIST;
		}
		
		if (getField().getText().length() > maxLength) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}

}
