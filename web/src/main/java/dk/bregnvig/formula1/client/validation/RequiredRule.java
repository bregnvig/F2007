package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class RequiredRule extends AbstractTextRule {
	
	private boolean suspendValidation = false; 

	public RequiredRule(TextBoxBase field, Label labelProperty) {
		super(field, labelProperty);
	}

	public RequiredRule(TextBoxBase field, String property) {
		super(field, property);
	}

	protected String getErrorMessage() {
		return "{0} skal udfyldes";
	}
	
	public List validate() {
		
		if (getTextField().getText().length() == 0) {
			suspendValidation = true;
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}

	public boolean supendValidation() {
		return suspendValidation;
	}

	
}
