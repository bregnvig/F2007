package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class EmailRule extends AbstractRule {

	public final static String EMAIL_PATTERN = "^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z])?[a-zA-Z]*)*\\s+<("
		+ "\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})>$|^(\\w[-._\\w]*\\w"
		+ "@\\w[-._\\w]*\\w\\.\\w{2,4})$";
	
	public EmailRule(TextBoxBase field, Label labelProperty) {
		super(field, labelProperty);
	}

	public EmailRule(TextBoxBase field, String property) {
		super(field, property);
	}

	protected String getErrorMessage() {
		return "{0} er ikke en gyldig e-mail-adresse";
	}

	public List validate() {
		
		if (getField().getText().length() == 0) {
			return Collections.EMPTY_LIST;
		}
		
		if (getField().getText().replaceFirst(EMAIL_PATTERN, "").length() > 0) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}

}
