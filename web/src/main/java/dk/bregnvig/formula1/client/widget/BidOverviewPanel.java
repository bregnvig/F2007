package dk.bregnvig.formula1.client.widget;

import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.widget.control.BigLabel;

public class BidOverviewPanel extends ContentPanel {
	
	private Grid table;

	public BidOverviewPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		setWidth("95%");
		
		add(getLegend());
		
		table = new Grid(getNumberOfRows(), 7);
		table.setWidget(0, 0, new Label("Navn"));
		table.setWidget(0, 1, new Label("Qualify"));
		table.setWidget(0, 2, new Label("Hurtigste kører"));
		table.setWidget(0, 3, new Label("Podium"));
		table.setWidget(0, 4, new Label("Udvalgte kører"));
		table.setWidget(0, 5, new Label("Først udgået"));
		table.setWidget(0, 6, new Label("pole position tid"));
		table.setWidth("100%");
		table.getCellFormatter().addStyleName(0, 0, "bidHeader");
		table.getCellFormatter().addStyleName(0, 1, "bidHeader");
		table.getCellFormatter().addStyleName(0, 2, "bidHeader");
		table.getCellFormatter().addStyleName(0, 3, "bidHeader");
		table.getCellFormatter().addStyleName(0, 4, "bidHeader");
		table.getCellFormatter().addStyleName(0, 5, "bidHeader");
		table.getCellFormatter().addStyleName(0, 6, "bidHeader");
		
		List bids = mediator.getSelectedRace().getBids();
		int offset = mediator.getSelectedRace().isCompleted() ? 2 : 1;
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			Panel panel = new FlowPanel();
			panel.add(new Label(bid.getPlayer().getFirstName() + " " + bid.getPlayer().getLastName()));
			if (getMediator().getSelectedRace().isCompleted()) {
				panel.add(new Label("(" + bid.getPoints() + " points)"));
			}
			table.setWidget(i+offset, 0, panel);
			table.getRowFormatter().addStyleName(i+offset, "bidEntryRow");
			table.getRowFormatter().setVerticalAlign(i+offset, VerticalPanel.ALIGN_TOP);
		}
		if (offset == 2) {
			table.setWidget(1, 0, new Label("Resultat"));
			table.getRowFormatter().addStyleName(1, "bidEntryRow");
			table.getRowFormatter().setVerticalAlign(1, VerticalPanel.ALIGN_TOP);
		}
		addQualify();
		addFastestLap();
		addPodium();
		addSelectedDriver();
		addFirstCrash();
		addPolePositionTime();
		add(table);
	}

	private int getNumberOfRows() {
		int size = getMediator().getSelectedRace().getBids().size()+1; 
		if (getMediator().getSelectedRace().isCompleted()) {
			size++;
		}
		return size;
	}

	private Panel getLegend() {
		Panel legend = new HorizontalPanel();
//		legend.setWidth("100%");
		Label zeroPoint = new BidEntrylabel("Nul point ");
		Label onePoint = new BidEntrylabel("Et point ");
		onePoint.addStyleName("gwt-Label-bidEntry-1points");
		
		Label twoPoints = new BidEntrylabel("To point ");
		twoPoints.addStyleName("gwt-Label-bidEntry-2points");
		Label threePoints = new BidEntrylabel("Tre point");
		threePoints.addStyleName("gwt-Label-bidEntry-3points");
		legend.add(zeroPoint);
		legend.add(onePoint);
		legend.add(twoPoints);
		legend.add(threePoints);
		return legend;
	}
	
	private void addQualify() {
		int offset = 1;
		if (getMediator().getSelectedRace().isCompleted()) {
			table.setWidget(offset, 1, getCategory(getMediator().getSelectedRace().getRaceResult().getGrid()));
			offset++;
		}
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+offset, 1, getCategory(bid.getGrid()));
		}
		
	}

	private void addFastestLap() {
		int offset = 1;
		if (getMediator().getSelectedRace().isCompleted()) {
			table.setWidget(offset, 2, getBidEntry(1, getMediator().getSelectedRace().getRaceResult().getFastestLap()));
			offset++;
		}
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+offset, 2, getBidEntry(1, bid.getFastestLap()));
		}
	}

	private void addPodium() {
		int offset = 1;
		if (getMediator().getSelectedRace().isCompleted()) {
			table.setWidget(offset, 3, getCategory(getMediator().getSelectedRace().getRaceResult().getPodium()));
			offset++;
		}
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+offset, 3, getCategory(bid.getPodium()));
		}
		
	}
	
	private void addSelectedDriver() {
		int offset = 1;
		if (getMediator().getSelectedRace().isCompleted()) {
			table.setWidget(offset, 4, getSelectedDriverPanel(getMediator().getSelectedRace().getRaceResult()));
			offset++;
		}
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+offset, 4, getSelectedDriverPanel(bid));
		}
	}

	private void addFirstCrash() {
		int offset = 1;
		if (getMediator().getSelectedRace().isCompleted()) {
			table.setWidget(offset, 5, getCategory(getMediator().getSelectedRace().getRaceResult().getFirstCrashes()));
			offset++;
		}
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+offset, 5, getBidEntry(1, bid.getFirstCrash()));
		}
	}
	
	private void addPolePositionTime() {
		int offset = 1;
		if (getMediator().getSelectedRace().isCompleted()) {
			table.setWidget(offset, 6, new BidEntrylabel(getMediator().getSelectedRace().getRaceResult().getPolePositionTimeInText()));
			offset++;
		}
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+offset, 6, new BidEntrylabel(bid.getPolePositionTimeInText()));
		}
	}
	
	
	
	private Panel getSelectedDriverPanel(ClientBid bid) {
		BidEntrylabel startPosition = new BidEntrylabel("Starter som " + bid.getSelectedDriver()[0]);
		BidEntrylabel endPosition = new BidEntrylabel("Slutter som " + bid.getSelectedDriver()[1]);
		if (getMediator().getSelectedRace().isCompleted()) {
			if (bid.getSelectedDriverPoints()[0] != 0) {
				startPosition.addStyleName("gwt-Label-bidEntry-" +bid.getSelectedDriverPoints()[0] + "points");
				startPosition.setTitle(bid.getSelectedDriverPoints()[0] + " points");
			}
			if (bid.getSelectedDriverPoints()[1] != 0) {
				endPosition.addStyleName("gwt-Label-bidEntry-" +bid.getSelectedDriverPoints()[1] + "points");
				endPosition.setTitle(bid.getSelectedDriverPoints()[1] + " points");
			}
		}
		Panel panel = new FlowPanel();
		panel.add(startPosition);
		panel.add(endPosition);
		return panel;
		
	}
	
	private Panel getCategory(ClientDriver[] drivers) {
		Panel panel = new FlowPanel();
		
		for (int i = 0; i < drivers.length; i++) {
			panel.add(getBidEntry(i+1, drivers[i]));
		}
		return panel;
	}
	
	private Label getBidEntry(int position, ClientDriver driver) {
		Label label = new BidEntrylabel(Integer.toString(position) + ". " + driver.getName());
		label.setTitle(Integer.toString(driver.getPoints()) + " points");
		if  (driver.getPoints() != 0) {
			label.addStyleName("gwt-Label-bidEntry-" +driver.getPoints() + "points");
		}
		return label;
	}

	protected Widget getScreenTitle() {
		return new BigLabel(getMediator().getSelectedRace().getName() + (getMediator().getSelectedRace().isCompleted() ? " - resultat" : " - indsendte bud"));
	}
	
	private class BidEntrylabel extends Label {

		public BidEntrylabel() {
			super();
			setStyleName("gwt-Label-bidEntry");
		}

		public BidEntrylabel(String text) {
			super(text);
			setStyleName("gwt-Label-bidEntry");
		}
		
	}

}
