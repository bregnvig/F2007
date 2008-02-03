package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.validation.UniqueDriverRule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;

public class SelectPodiumPanel extends AbstractWizardPage {

	private DriverSelection[] drivers = new DriverSelection[3];
	
	public SelectPodiumPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		
		VerticalPanel driver2 = new VerticalPanel();
		driver2.add(getSpacer());
		drivers[1] = new DriverSelection(mediator, "2. plads");
		driver2.add(drivers[1]);
		
		VerticalPanel driver3 = new VerticalPanel();
		driver3.add(getSpacer());
		drivers[2] = new DriverSelection(mediator, "3. plads");
		driver3.add(drivers[2]);
		
		Grid grid = new Grid(1, 3);
		grid.setCellPadding(10);
		grid.setWidget(0, 0, driver2);
		grid.setWidget(0, 1, drivers[0] = new DriverSelection(mediator, "1. plads"));
		grid.setWidget(0, 2, driver3);

		if (mediator.isTesting()) {
			drivers[0].setCurrentDriverIndex(0);
			drivers[1].setCurrentDriverIndex(1);
			drivers[2].setCurrentDriverIndex(2);
		}
		
		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
	}
	
	private Image getSpacer() {
		Image image = new Image("clear.cache.gif");
		image.setHeight("40px");
		return image;
	}

	protected String getScreenTitle() {
		return "Vælg podium placeringer";
	}

	protected Rule[] getRules() {
		Rule[] rules = new Rule[3];
		rules[0] = new UniqueDriverRule(drivers[0], drivers);
		rules[1] = new UniqueDriverRule(drivers[1], drivers);
		rules[2] = new UniqueDriverRule(drivers[2], drivers);
		return rules;
	}
	
	public ClientDriver[] getDrivers() {
		ClientDriver[] drivers = new ClientDriver[6];
		for (int i = 0; i < this.drivers.length; i++) {
			drivers[i] = this.drivers[i].getDriver();
		}
		return drivers;
	}

}
