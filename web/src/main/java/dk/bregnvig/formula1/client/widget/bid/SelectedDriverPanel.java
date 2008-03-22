package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.validation.RequiredPositionSelectorRule;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.DriverImage;
import dk.bregnvig.formula1.client.widget.control.FormLabel;

public class SelectedDriverPanel extends AbstractWizardPage {
	
	private final PositionSelector startListBox;
	private final PositionSelector endListBox;
	
	public SelectedDriverPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		
		Grid grid = new Grid(3, 2);
		grid.setCellPadding(10);
		grid.setWidget(0, 1, new DriverImage(getMediator().getSelectedRace().getSelectedDriver()));
		Label driverName = new ContentTitleLabel(getMediator().getSelectedRace().getSelectedDriver().getName());
		grid.setWidget(0, 0, driverName);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_RIGHT);
		grid.setWidget(1, 0, new FormLabel("Jeg tror " + getMediator().getSelectedRace().getSelectedDriver().getName() + " starter som "));
		HorizontalPanel temp = new HorizontalPanel();
		temp.add(startListBox = new PositionSelector(mediator.getSeason().getActiveDriver().size()));
		grid.setWidget(1, 1, temp);
		grid.setWidget(2, 0, new FormLabel("Jeg tror " + getMediator().getSelectedRace().getSelectedDriver().getName() + " slutter som "));
		temp = new HorizontalPanel();
		temp.add(endListBox = new PositionSelector(mediator.getSeason().getActiveDriver().size()));
		grid.setWidget(2, 1, temp);
		
		if (mediator.isTesting()) {
			startListBox.setSelectedPosition(1);
			endListBox.setSelectedPosition(3);
		}
		
		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
	}
	
	protected Widget getScreenTitle() {
		return new  BigLabel(getMediator().getSelectedRace().isOpened() ? "Gæt den udvalgte køres start- og slutposition" : "Udvalgte køres start- og slutposition");
	}

	protected Rule[] getRules() {
		Rule[] rules = new Rule[2];
		rules[0] = new RequiredPositionSelectorRule(startListBox, "Forventet startposition");
		rules[1] = new RequiredPositionSelectorRule(endListBox, "Forventet slutposition");
		return rules;
	}
	
	public int[] getPositions() {
		int[] positions = new int[2];
		positions[0] = startListBox.getSelectedPostion();
		positions[1] = endListBox.getSelectedPostion();
		return positions;
	}

}
