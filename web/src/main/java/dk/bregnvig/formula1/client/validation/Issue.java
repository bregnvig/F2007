package dk.bregnvig.formula1.client.validation;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class Issue implements IsSerializable {
	
	private Widget field;
	private Label labelProperty;
	private String property; 
	private String issue;

	private Issue(Widget field, String issue) {
		if (field == null) {
			throw new IllegalArgumentException("the field must be set");
		}
		if (issue == null) {
			throw new IllegalArgumentException("the issue must be specified");
		}
		this.field = field;
		this.issue = issue;
	}

	public Issue(Widget field, String issue, Label labelProperty, String property) {
		this(field, issue);
		if (labelProperty == null && property == null) {
			throw new IllegalArgumentException("Either labelProperty or property must be set");
		}
		this.labelProperty = labelProperty;
		this.property = property;
		
	}
	
	public Issue(TextBoxBase field, String issue, Label labelProperty) {
		this(field, issue);
		if (labelProperty == null) {
			throw new IllegalArgumentException("The property label must be set");
		}
		this.labelProperty = labelProperty;
		this.property = labelProperty.getText();
	}
	
	public Issue(TextBoxBase field, String issue, String property) {
		this(field, issue);
		if (property == null) {
			throw new IllegalArgumentException("The property must be set");
		}
		this.property = property;
	}

	public Widget getField() {
		return field;
	}

	public Label getLabelProperty() {
		return labelProperty;
	}

	public String getProperty() {
		return property;
	}

	public String getIssue() {
		return issue;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Issue:");
		buffer.append(" field: ");
		buffer.append(field);
		buffer.append(" labelProperty: ");
		buffer.append(labelProperty);
		buffer.append(" property: ");
		buffer.append(property);
		buffer.append(" issue: ");
		buffer.append(issue);
		buffer.append("]");
		return buffer.toString();
	}
}
