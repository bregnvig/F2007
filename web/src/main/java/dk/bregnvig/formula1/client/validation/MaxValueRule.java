package dk.bregnvig.formula1.client.validation;

import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public class MaxValueRule extends AbstractTextRule {
	
	private int maxValue;
	private IntegerRule dependsOn;

	public MaxValueRule(TextBoxBase field, String property, int maxValue) {
		super(field, property);
		this.maxValue = maxValue;
		dependsOn = new IntegerRule(field, property);
	}

	public MaxValueRule(TextBoxBase field, Label labelProperty, int maxValue) {
		super(field, labelProperty);
		this.maxValue = maxValue;
		dependsOn = new IntegerRule(field, labelProperty);
	}

	protected String getErrorMessage() {
		return "{0} må ikke være større end " + maxValue;
	}

	public List validate() {
		List issues = dependsOn.validate();
		if (issues.size() == 0) {
			int value = Integer.parseInt(getTextField().getText());
			if (value > maxValue) {
				issues = getIssue();
			}
		}
		return issues;
	}

}
