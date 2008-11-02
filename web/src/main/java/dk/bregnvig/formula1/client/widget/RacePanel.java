package dk.bregnvig.formula1.client.widget;

import java.util.Iterator;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;
import dk.bregnvig.formula1.client.widget.bid.EnterPolePositionPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectFastestDriverPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectFirstCrashDriverPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectGridPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectPodiumPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectedDriverPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.CountDownLabel;
import dk.bregnvig.formula1.client.widget.control.PlayerLabel;
import dk.bregnvig.formula1.client.widget.control.WaitingPanel;
import dk.bregnvig.formula1.client.widget.control.WizardCompletedListener;
import dk.bregnvig.formula1.client.widget.control.WizardPanel;

public class RacePanel extends ContentPanel implements WizardCompletedListener, F2007.Callback {

	private final ParticipantPanel participants;
	private final VerticalPanel rightSide;
	
	private Button commandButton;
	private Button seeButton;
	
	private WizardPanel wizard;
	private SelectGridPanel selectGrid;
	private SelectFastestDriverPanel selectFastest;
	private SelectPodiumPanel selectPodium;
	private SelectedDriverPanel selectedDriver;
	private SelectFirstCrashDriverPanel selectFirstCrash;
	private EnterPolePositionPanel polePosition;
	
	
	public RacePanel(final F2007 mediator, final MainPanel mainPanel, ClientRace race) {
		super(mediator, mainPanel);
		if (race == null) {
			throw new IllegalArgumentException("Race cannot be null");
		}
		rightSide = new VerticalPanel();
		rightSide.setWidth("300px");
		rightSide.add(participants = new ParticipantPanel());
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");

		Image image = new Image("tracks/"+race.getName()+".png");
		panel.add(image);
		
		panel.add(rightSide);
		
		add(panel);
		mediator.setSelectedRace(race, this);
	}

	private void createWizard() {
		selectGrid = new SelectGridPanel(getMediator(), getMainPanel());
		selectFastest = new SelectFastestDriverPanel(getMediator(), getMainPanel());
		selectPodium = new SelectPodiumPanel(getMediator(), getMainPanel());
		selectedDriver = new SelectedDriverPanel(getMediator(), getMainPanel());
		selectFirstCrash = new SelectFirstCrashDriverPanel(getMediator(), getMainPanel());
		polePosition = new EnterPolePositionPanel(getMediator(), getMainPanel());
		wizard = new WizardPanel(this, new AbstractWizardPage[] {selectGrid, selectFastest, selectPodium, selectedDriver, selectFirstCrash, polePosition});
	}

	private void addCommandButton(String text) {
		commandButton = new Button(text, new ClickListener() {
			public void onClick(Widget arg0) {
				getMainPanel().setCenterPanel(wizard);
			}
		});
		addButton(commandButton);
	}
	
	private void addSeeBidButton() {
		seeButton = new Button(getMediator().getSelectedRace().isCompleted() ? "Se resultat" : "Se bud", new ClickListener() {
			public void onClick(Widget arg0) {
				getMainPanel().setCenterPanel(new BidOverviewPanel(getMediator(), getMainPanel()));
			}
		});
		addButton(seeButton);
	}
	
	private void addButton(Button commandButton) {
		rightSide.add(commandButton);
		rightSide.setCellHeight(commandButton, "50px");
		rightSide.setCellVerticalAlignment(commandButton, VerticalPanel.ALIGN_BOTTOM);
	}

	private void removeCommandButton() {
		if (commandButton != null) {
			rightSide.remove(commandButton);
		}
	}
	
	
	private void submitBid() {
		final ClientBid bid = new ClientBid(); 
		mapCommonInformation(bid);
		bid.setFirstCrash(selectFirstCrash.getDriver());
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable exception) {
				getMediator().reportError(exception.getMessage());
			}

			public void onSuccess(Object arg0) {
				getMediator().getSelectedRace().participant();
				getMediator().getSelectedRace().addBid(bid);
				participants.reset();
				removeCommandButton();
				getMainPanel().setCenterPanel(RacePanel.this);
			}

		};
		getMediator().getGameService().addBid(bid, callback);
	}

	private void submitResult() {
		final ClientResult result = new ClientResult();
		mapCommonInformation(result);
		result.setFirstCrashes(selectFirstCrash.getDrivers());
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable exception) {
				getMediator().reportError(exception.getMessage());
			}

			public void onSuccess(Object _race) {
				ClientRace race = (ClientRace) _race;
				race.setFullyLoaded(false);
				getMediator().setSelectedRace(race, RacePanel.this);
				getMainPanel().setCenterPanel(RacePanel.this);
			}
		};
		getMediator().getGameService().setResult(result, callback);
	}
	
	private ClientBid mapCommonInformation(ClientBid bid) {
		bid.setPlayer(getMediator().getPlayer());
		bid.setGrid(selectGrid.getDrivers());
		bid.setFastestLap(selectFastest.getDriver());
		bid.setPodium(selectPodium.getDrivers());
		bid.setSelectedDriver(selectedDriver.getPositions());
		bid.setPolePositionTime(polePosition.getPolePosition());
		return bid;
	}
	
	private class ParticipantPanel extends VerticalPanel  {
		
		private WaitingPanel waiting = new WaitingPanel("234px", "240px");
		
		public ParticipantPanel() {
			add(waiting);
		}
		
		private void addHeadline() {
			Label headline = new ContentTitleLabel("Deltagere");
			headline.addStyleName("north");
			add(headline);
		}
		
		private void addBids() {
			FlexTable table = new FlexTable();
			
			Iterator i = getMediator().getSelectedRace().getBids().iterator();
			int index = 0;
			while(i.hasNext()) {
				ClientBid bid = (ClientBid) i.next();
				ClientPlayer player = bid.getPlayer().equals(getMediator().getPlayer()) ? getMediator().getPlayer() : bid.getPlayer();
				table.setWidget(index, 0, new PlayerLabel(player));
				if (getMediator().getSelectedRace().isCompleted()) {
					table.setWidget(index, 1, new Label(Integer.toString(bid.getPoints())));
				}
				index++;
			}
			add(table);
		}
		
		void reset() {
			this.clear();
			addHeadline();
			addBids();
		}
	}

	protected Widget getScreenTitle() {
		ClientRace race = getMediator().getSelectedRace();
		
		String postFix = " - åbner om&nbsp;";
		if (race.isOpened()) {
			postFix = " - åbent i&nbsp;";
		} else if (race.isClosed() && race.isCompleted() == false) {
			postFix = " - lukket";
		} else if (race.isCompleted() == true){
			postFix = " - afgjort";
		} 
		HorizontalPanel panel = new HorizontalPanel();
		HTML label = new HTML(race.getName() + postFix);
		label.setStylePrimaryName("gwt-Label");
		label.addStyleDependentName("big");
		panel.add(label);
		
		CountDownLabel countDown = new CountDownLabel(race);
		countDown.useMinutes();
		countDown.addStyleDependentName("big");
		panel.add(countDown);
		panel.setCellVerticalAlignment(countDown, HorizontalPanel.ALIGN_BOTTOM);
		return panel;
	}

	public void completed(AbstractWizardPage[] pages) {
		if (getMediator().getSelectedRace().isOpened() == true) {
			submitBid();	
		}
		if (getMediator().getSelectedRace().isClosed() == true) {
			submitResult();
		}
		
	}

	public void completed() {
		if (participants != null) {
			participants.reset();
		}
		removeCommandButton();
		ClientRace race = getMediator().getSelectedRace();
		if (race.isOpened() == true && race.isParticipant() == false) {
			createWizard();
			addCommandButton("Jeg skal deltage");
			if (getMediator().getPlayer().isBettingMoneyAvailable() == false) {
				commandButton.setEnabled(false);
				commandButton.setTitle("Du har ikke penge til at deltage i " + race.getName());
				
			}
		} else if (race.isOpened() && race.isParticipant() == true) {
			addSeeBidButton();
		} else if (race.isOpened() == false && race.isCompleted() == false && race.isWaiting() == false) {
			addSeeBidButton();
			if (getMediator().getPlayer().isGameAdministrator() == true) {
				addCommandButton("Indtast resultat");
			}
			createWizard();
		} else if (race.isCompleted()) {
			addSeeBidButton();
		} 
	}
}
