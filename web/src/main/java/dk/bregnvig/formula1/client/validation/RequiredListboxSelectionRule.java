package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class RequiredListboxSelectionRule extends AbstractRule {
	
	private boolean zeroASelection;

	public RequiredListboxSelectionRule(ListBox field, Label labelProperty, boolean zeroASelection) {
		super(field, labelProperty);
		this.zeroASelection = zeroASelection;
	}

	protected String getErrorMessage() {
		return "{0} skal vælges";
	}
	
	public List validate() {
		ListBox listBox = (ListBox) getField();
		if ((listBox.getSelectedIndex() == 0 && zeroASelection == false) || listBox.getSelectedIndex() == -1) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}
}
