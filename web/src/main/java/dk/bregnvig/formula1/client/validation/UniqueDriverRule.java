package dk.bregnvig.formula1.client.validation;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.widget.bid.DriverSelection;

public class UniqueDriverRule extends AbstractRule {

	private DriverSelection currentSelection;
	private DriverSelection[] drivers;
	
	public UniqueDriverRule(Widget field, DriverSelection[] drivers) {
		super(field, ((DriverSelection) field).getDriver().getName());
		currentSelection = (DriverSelection) field;
		this.drivers = drivers;
	}

	protected String getErrorMessage() {
		return "{0} er allerede benyttet et andet sted";
	}

	public List validate() {
		for (int i = 0; i < drivers.length; i++) {
			if (drivers[i].getTitle().equals(currentSelection.getTitle()) == false) {
				if (drivers[i].getDriver().equals(currentSelection.getDriver())) {
					return getIssue();
				}
			}
		}
		return Collections.EMPTY_LIST;
	}
}
