package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.BigLabel;

public class SelectFastestDriverPanel extends AbstractWizardPage {

	private DriverSelection driver;
	
	public SelectFastestDriverPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		
		Grid grid = new Grid(1, 1);
		grid.setCellPadding(10);
		grid.setWidget(0, 0, driver = new DriverSelection(mediator, "Hurtigste kører"));

		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
	}
	
	protected Widget getScreenTitle() {
		return new  BigLabel(getMediator().getSelectedRace().isOpened() ? "Vælg hurtigste kører" : "Hurtigste kører");
	}

	protected Rule[] getRules() {
		return new Rule[0];
	}
	
	public ClientDriver getDriver() {
		return driver.getDriver();
	}

}
