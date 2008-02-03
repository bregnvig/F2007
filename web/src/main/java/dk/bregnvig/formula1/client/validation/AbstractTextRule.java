package dk.bregnvig.formula1.client.validation;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractTextRule extends AbstractRule {

	public AbstractTextRule(Widget field, Label labelProperty) {
		super(field, labelProperty);
	}

	public AbstractTextRule(Widget field, String property) {
		super(field, property);
	}

	public TextBoxBase getTextField() {
		return (TextBoxBase) super.getField();
	}

}
