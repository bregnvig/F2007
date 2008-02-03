package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.validation.UniqueDriverRule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;

public class SelectGridPanel extends AbstractWizardPage {

	private DriverSelection[] drivers = new DriverSelection[6];
	
	public SelectGridPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		
		HorizontalPanel driver2 = new HorizontalPanel();
		driver2.add(getSpacer());
		drivers[1] = new DriverSelection(mediator, "Position 2");
		driver2.add(drivers[1]);
		HorizontalPanel driver4 = new HorizontalPanel();
		driver4.add(getSpacer());
		drivers[3] = new DriverSelection(mediator, "Position 4");
		driver4.add(drivers[3]);
		HorizontalPanel driver6 = new HorizontalPanel();
		driver6.add(getSpacer());
		drivers[5] = new DriverSelection(mediator, "Position 6");
		driver6.add(drivers[5]);
		
		Grid grid = new Grid(2, 3);
		grid.setCellPadding(10);
		grid.setWidget(0, 0, drivers[0] = new DriverSelection(mediator, "Position 1"));
		grid.setWidget(1, 0, driver2);
		grid.setWidget(0, 1, drivers[2] = new DriverSelection(mediator, "Position 3"));
		grid.setWidget(1, 1, driver4);
		grid.setWidget(0, 2, drivers[4] = new DriverSelection(mediator, "Position 5"));
		grid.setWidget(1, 2, driver6);
		
		if (mediator.isTesting()) {
			drivers[0].setCurrentDriverIndex(0);
			drivers[1].setCurrentDriverIndex(1);
			drivers[2].setCurrentDriverIndex(2);
			drivers[3].setCurrentDriverIndex(3);
			drivers[4].setCurrentDriverIndex(4);
			drivers[5].setCurrentDriverIndex(5);
		}

		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
	}
	
	private Image getSpacer() {
		Image image = new Image("clear.cache.gif");
		image.setWidth("25px");
		return image;
	}

	protected String getScreenTitle() {
		return "Vælg start grid";
	}

	protected Rule[] getRules() {
		Rule[] rules = new Rule[6];
		rules[0] = new UniqueDriverRule(drivers[0], drivers);
		rules[1] = new UniqueDriverRule(drivers[1], drivers);
		rules[2] = new UniqueDriverRule(drivers[2], drivers);
		rules[3] = new UniqueDriverRule(drivers[3], drivers);
		rules[4] = new UniqueDriverRule(drivers[4], drivers);
		rules[5] = new UniqueDriverRule(drivers[5], drivers);
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
