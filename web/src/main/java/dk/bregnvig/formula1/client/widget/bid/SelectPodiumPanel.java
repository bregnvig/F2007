package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.validation.UniqueDriverRule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.BigLabel;

public class SelectPodiumPanel extends AbstractWizardPage {

	private DriverSelection[] drivers;
	private boolean bidPage;
	
	public SelectPodiumPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		bidPage = getMediator().getSelectedRace().isOpened();

		drivers = new DriverSelection[isBidPage() ? 3 : 4];
		VerticalPanel driver2 = new VerticalPanel();
		driver2.add(getSpacer());
		drivers[1] = new DriverSelection(mediator, "2. plads");
		driver2.add(drivers[1]);
		
		VerticalPanel driver3 = new VerticalPanel();
		driver3.add(getSpacer());
		drivers[2] = new DriverSelection(mediator, "3. plads");
		driver3.add(drivers[2]);

		VerticalPanel driver4 = new VerticalPanel();
		if (isResultPage()) {
			driver4.add(getSpacer());
			drivers[3] = new DriverSelection(mediator, "4. plads");
			driver4.add(drivers[3]);
		}
		
		Grid grid = new Grid(isBidPage() ? 1 : 2, 3);
		grid.setCellPadding(10);
		grid.setWidget(0, 0, driver2);
		grid.setWidget(0, 1, drivers[0] = new DriverSelection(mediator, "1. plads"));
		grid.setWidget(0, 2, driver3);
		if (isResultPage()) {
			grid.setWidget(1, 1, driver4);
		}

		if (mediator.isTesting()) {
			drivers[0].setCurrentDriverIndex(0);
			drivers[1].setCurrentDriverIndex(1);
			drivers[2].setCurrentDriverIndex(2);
			if (isResultPage()) {
				drivers[3].setCurrentDriverIndex(3);
			}
		}
		
		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
	}
	
	private Image getSpacer() {
		Image image = new Image("clear.cache.gif");
		image.setHeight("40px");
		return image;
	}

	protected Widget getScreenTitle() {
		return new  BigLabel(isBidPage() ? "Gæt podium placeringer" : "Podium resultatet");
	}

	protected Rule[] getRules() {
		Rule[] rules = new Rule[isBidPage() ? 3 : 4];
		rules[0] = new UniqueDriverRule(drivers[0], drivers);
		rules[1] = new UniqueDriverRule(drivers[1], drivers);
		rules[2] = new UniqueDriverRule(drivers[2], drivers);
		if (isResultPage()) {
			rules[3] = new UniqueDriverRule(drivers[3], drivers);
		}
		return rules;
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
