package dk.bregnvig.formula1.client.widget;

import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
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
		
		table = new Grid(mediator.getSelectedRace().getBids().size()+1, 7);
		table.setWidget(0, 0, new Label(""));
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
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+1, 0, new Label(bid.getPlayer().getFirstName() + " " + bid.getPlayer().getLastName()));
			table.getRowFormatter().addStyleName(i+1, "bidEntryRow");
			table.getRowFormatter().setVerticalAlign(i+1, VerticalPanel.ALIGN_TOP);
		}
		addQualify();
		addFastestCrash();
		addPodium();
		addSelectedDriver();
		addFirstCrash();
		add(table);
	}
	
	private void addQualify() {
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+1, 1, getCategory(bid.getGrid()));
		}
		
	}

	private void addFastestCrash() {
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+1, 2, getBidEntry(1, bid.getFastestLap()));
		}
	}

	private void addPodium() {
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+1, 3, getCategory(bid.getPodium()));
		}
		
	}

	private void addFirstCrash() {
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+1, 5, getBidEntry(1, bid.getFirstCrash()));
		}
	}
	
	private void addSelectedDriver() {
		List bids = getMediator().getSelectedRace().getBids();
		for (int i = 0; i < bids.size(); i++) {
			ClientBid bid = (ClientBid) bids.get(i);
			table.setWidget(i+1, 4, getSelectedDriverPanel(bid));
		}
	}
	
	private Panel getSelectedDriverPanel(ClientBid bid) {
		BidEntrylabel startPosition = new BidEntrylabel("Starter som " + bid.getSelectedDriver()[0]);
		BidEntrylabel endPosition = new BidEntrylabel("Slutter som " + bid.getSelectedDriver()[1]);
		if (bid.getSelectedDriverPoints()[0] != 0) {
			startPosition.addStyleName("gwt-Label-bidEntry-" +bid.getSelectedDriverPoints()[0] + "points");
		}
		if (bid.getSelectedDriverPoints()[1] != 0) {
			endPosition.addStyleName("gwt-Label-bidEntry-" +bid.getSelectedDriverPoints()[1] + "points");
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
		return new BigLabel(getMediator().getSelectedRace().isCompleted() ? "Resultatet" : "Indsendt bud");
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
