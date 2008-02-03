package dk.bregnvig.formula1.client.widget;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.widget.bid.EnterPolePositionPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectFastestDriverPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectGridPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectPodiumPanel;
import dk.bregnvig.formula1.client.widget.bid.SelectedDriverPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.PlayerLabel;
import dk.bregnvig.formula1.client.widget.control.WizardPanel;

public class RacePanel extends ContentPanel {

	private ClientRace race;
	
	private WizardPanel wizard;
	
	public RacePanel(F2007 mediator, final MainPanel mainPanel, ClientRace race) {
		super(mediator, mainPanel);

		SelectGridPanel selectGrid = new SelectGridPanel(mediator, mainPanel);
		SelectFastestDriverPanel selectFastest = new SelectFastestDriverPanel(mediator, mainPanel);
		SelectPodiumPanel selectPodium = new SelectPodiumPanel(mediator, mainPanel);
		SelectedDriverPanel selectedDriver = new SelectedDriverPanel(mediator, mainPanel, race.getSelectedDriver());
		EnterPolePositionPanel polePosition = new EnterPolePositionPanel(mediator, mainPanel, race);
		wizard = new WizardPanel(new AbstractWizardPage[] {selectGrid, selectFastest, selectPodium, selectedDriver, polePosition});
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		
		if (race == null) {
			throw new IllegalArgumentException("Race cannot be null");
		}
		this.race = race;
		Image image = new Image("tracks/"+race.getName()+".png");
		panel.add(image);
		VerticalPanel rightSide = new VerticalPanel();
		rightSide.setWidth("100%");
		rightSide.add(new ParticipantPanel());
		if (race.isParticipant() == false) {
			Button participateButton = new Button("Jeg skal deltage", new ClickListener() {
				public void onClick(Widget arg0) {
					mainPanel.setCenterPanel(wizard);
				}
			});
			rightSide.add(participateButton);
			rightSide.setCellHeight(participateButton, "50px");
			rightSide.setCellVerticalAlignment(participateButton, VerticalPanel.ALIGN_BOTTOM);
		}
		
		panel.add(rightSide);
		
		add(panel);
	}
	
	private class ParticipantPanel extends VerticalPanel {
		
		public ParticipantPanel() {
			addHeadline();
			addBids();
		}
		
		
		private void addHeadline() {
			Label headline = new ContentTitleLabel("Deltagere");
			headline.addStyleName("north");
			add(headline);
		}
		
		private void addBids() {
			Iterator i = race.getBids().iterator();
			while(i.hasNext()) {
				ClientBid bid = (ClientBid) i.next();
				add(new PlayerLabel(bid.getPlayer().getFirstName() + bid.getPlayer().getLastName()));
			}
		}
	}

	protected String getScreenTitle() {
		return race.getName();
	}

}
