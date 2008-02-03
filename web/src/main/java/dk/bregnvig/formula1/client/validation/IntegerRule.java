package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class IntegerRule extends AbstractTextRule {

	public IntegerRule(TextBoxBase field, Label labelProperty) {
		super(field, labelProperty);
	}

	public IntegerRule(TextBoxBase field, String property) {
		super(field, property);
	}

	protected String getErrorMessage() {
		return "{0} skal skrives som tal";
	}

	public List validate() {
		try {
			Integer.parseInt(getTextField().getText());
		} catch (NumberFormatException e) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}

}
