package dk.bregnvig.formula1.client.widget;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.wbc.ClientWBCEntry;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.PlayerLabel;
import dk.bregnvig.formula1.client.widget.control.WaitingPanel;

public class WBCPanel extends ContentPanel {

	private final ParticipantPanel participants;
	private final VerticalPanel rightSide;
	private final BigLabel title = new BigLabel("WBC Mesterskabet");

	private List<ClientWBCEntry> standing;
	private List<ClientWBCEntry> selected;

	
	public WBCPanel(final F2007 mediator, final MainPanel mainPanel) {
		super(mediator, mainPanel);

		rightSide = new VerticalPanel();
		rightSide.setWidth("300px");
		rightSide.add(participants = new ParticipantPanel());
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		panel.setStyleName("standard-indent");

		panel.add(new RacesPanel());
		
		panel.add(rightSide);
		
		add(panel);
	}

	protected void onLoad() {
		super.onLoad();
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				WBCPanel.this.getMediator().reportError("Failure - could not load WBC");
			}

			public void onSuccess(Object result) {
				selected = standing = (List) result;
				participants.reset();
			}
		};
		participants.waiting();
		getMediator().getGameService().fetchWBCStanding(callback);
	}

	private class ParticipantPanel extends VerticalPanel  {
		
		private WaitingPanel waiting = new WaitingPanel("234px", "240px");
		
		public ParticipantPanel() {
			waiting();
		}
		
		private void addHeadline() {
			Label headline = new ContentTitleLabel("Spiller");
			headline.addStyleName("north");
			add(headline);
		}
		
		private void addBids() {
			FlexTable table = new FlexTable();
			
			int index = 0;
			Iterator i = selected.iterator();
			while(i.hasNext()) {
				ClientWBCEntry entry = (ClientWBCEntry) i.next();
				if (entry.getPoints() == 0) {
					break;
				}
				ClientPlayer player = entry.getPlayer();
				table.setWidget(index, 0, new PlayerLabel(player));
				table.setWidget(index, 1, new Label(Integer.toString(entry.getPoints())));
				index++;
			}
			if (selected.size() == 0) {
				table.setWidget(0, 0, new Label("Løb ikke afsluttet endnu"));
			}
			add(table);
		}
		
		void reset() {
			this.clear();
			addHeadline();
			addBids();
		}
		
		void waiting() {
			this.clear();
			add(waiting);
		}

	}
	
	private class RacesPanel extends VerticalPanel {
		ContentTitleLabel total = new ContentTitleLabel();
		
		public RacesPanel() {
			total.setText("Samlet stilling");
			total.addStyleName("north");
			total.addClickListener(new ClickListener() {
				public void onClick(Widget ignore) {
					title.setText("WBC Mesterskabet");
					selected = WBCPanel.this.standing;
					participants.reset();
				}
			});
			total.addMouseListener(highlighter);
			add(total);
			
			Iterator i = getMediator().getSeason().getRaces().iterator();
			while(i.hasNext()) {
				ClientRace race = (ClientRace) i.next();
				add(new RaceLink(race));
			}
		}
	}
	
	private class RaceLink extends Label {
		
		private List<ClientWBCEntry> standing;
		private final ClientRace race;

		RaceLink(ClientRace race) {
			this.race = race;
			setText(race.getName());
			addMouseListener(highlighter);
			addClickListener(new ClickListener() {

				public void onClick(Widget ignore) {
					if (standing == null || standing.size() == 0) {
						AsyncCallback callback = new AsyncCallback() {

							public void onFailure(Throwable caught) {
								WBCPanel.this.getMediator().reportError("Failure - could not WBC");
							}

							public void onSuccess(Object result) {
								selected = standing = (List) result;
								participants.reset();
							}
						};
						participants.waiting();
						getMediator().getGameService().fetchWBCStanding(RaceLink.this.race, callback);
					} else {
						WBCPanel.this.selected = standing;
						WBCPanel.this.participants.reset();
					}
					title.setText("WBC stillingen for " + RaceLink.this.race.getName());
				}
			});

		}

		
	}
	
	private TextHighLight highlighter = new TextHighLight();
	
	private class TextHighLight extends MouseListenerAdapter {

		public void onMouseEnter(Widget widget) {
			widget.addStyleDependentName("over");
		}

		public void onMouseLeave(Widget widget) {
			widget.removeStyleDependentName("over");
		}
	}

	protected Widget getScreenTitle() {
		return title;
	}
}
