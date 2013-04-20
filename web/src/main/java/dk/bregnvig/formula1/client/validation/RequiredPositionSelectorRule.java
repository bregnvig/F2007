package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import dk.bregnvig.formula1.client.widget.bid.PositionSelector;

public class RequiredPositionSelectorRule extends AbstractRule {

	public RequiredPositionSelectorRule(PositionSelector field, String property) {
		super(field, property);
	}

	protected String getErrorMessage() {
		return "{0} skal vælges";
	}
	
	public List validate() {
		
		PositionSelector selector = (PositionSelector) getField();
		
		if (selector.getSelectedPostion() == 0) {
			return getIssue();
		}
		return Collections.EMPTY_LIST;
	}
}
