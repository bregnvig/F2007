package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.validation.UniqueDriverRule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.BigLabel;

public class SelectFirstCrashDriverPanel extends AbstractWizardPage {

	private DriverSelection[] drivers;
	private boolean bidPage;
	
	public SelectFirstCrashDriverPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		bidPage = getMediator().getSelectedRace().isOpened();
		drivers = new DriverSelection[isBidPage() ? 1 : 3];
		Grid grid = new Grid(isBidPage() ? 1 : 3, 1);
		grid.setCellPadding(10);
		grid.setWidget(0, 0, drivers[0] = new DriverSelection(mediator, "Først udgående kører"));
		if (isResultPage()) {
			grid.setWidget(1, 0, drivers[1] = new DriverSelection(mediator, "Anden udgående kører"));
			grid.setWidget(2, 0, drivers[2] = new DriverSelection(mediator, "Tredie udgående kører"));
		}

		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
		
		if (mediator.isTesting() && isResultPage()) {
			drivers[0].setCurrentDriverIndex(0);
			drivers[1].setCurrentDriverIndex(1);
			drivers[2].setCurrentDriverIndex(2);
		}
	}
	
	protected Widget getScreenTitle() {
		return new  BigLabel(isBidPage() ? "Gæt først udgået køre" : "Udgående kørere");
	}

	protected Rule[] getRules() {
		if (isBidPage()) {
			return new Rule[0];
		} else {
			Rule[] rules = new Rule[3];
			rules[0] = new UniqueDriverRule(drivers[0], drivers);
			rules[1] = new UniqueDriverRule(drivers[1], drivers);
			rules[2] = new UniqueDriverRule(drivers[2], drivers);
			return rules;
		}
	}
	
	public ClientDriver getDriver() {
		return drivers[0].getDriver();
	}
	
	public ClientDriver[] getDrivers() {
		ClientDriver[] drivers = new ClientDriver[this.drivers.length];
		for (int i = 0; i < this.drivers.length; i++) {
			drivers[i] = this.drivers[i].getDriver();
		}
		return drivers;
	}

	
	private boolean isResultPage() {
		return !bidPage;
	}
	
	private boolean isBidPage() {
		return bidPage;
	}
}
