package dk.bregnvig.formula1.client.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractRule implements Rule {

	private Widget field;
	private Label labelProperty;
	private String property; 

	private AbstractRule(Widget field) {
		if (field == null) {
			throw new IllegalArgumentException("the field must be set");
		}
		this.field = field;
	}
	
	public AbstractRule(Widget field, Label labelProperty) {
		this(field);
		if (labelProperty == null) {
			throw new IllegalArgumentException("The property label must be set");
		}
		this.labelProperty = labelProperty;
		this.property = labelProperty.getText();
	}
	
	public AbstractRule(Widget field, String property) {
		this(field);
		if (property == null) {
			throw new IllegalArgumentException("The property must be set");
		}
		this.property = property;
	}

	/*
	 * Default validation continues.
	 * @see dk.bregnvig.formula1.client.validation.Rule#supendValidation()
	 */
	public boolean supendValidation() {
		return false;
	}

	protected Widget getField() {
		return field;
	}

	protected Label getLabelProperty() {
		return labelProperty;
	}

	protected String getProperty() {
		return property;
	}
	
	protected List<Issue> getIssue() {
		String issueText = getErrorMessage().replaceAll("\\{0\\}", getProperty());
		Issue issue = new Issue(field, issueText, labelProperty, property);
		List<Issue> issues = new ArrayList<Issue>(1);
		issues.add(issue);
		return issues;
	}
	
	public Set<Widget> getFields() {
		Set<Widget> fields = new HashSet<Widget>(2);
		fields.add(field);
		return fields;
	}

	protected abstract String getErrorMessage();
}
