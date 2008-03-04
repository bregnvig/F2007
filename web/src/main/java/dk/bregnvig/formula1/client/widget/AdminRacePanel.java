package dk.bregnvig.formula1.client.widget;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.validation.CompositeRule;
import dk.bregnvig.formula1.client.validation.MaxLengthRule;
import dk.bregnvig.formula1.client.validation.RequiredRule;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.bid.DriverSelection;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.DatePicker;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTitleLabel;
import dk.bregnvig.formula1.client.widget.control.TimePicker;

public class AdminRacePanel extends ContentPanel {

	private MessagePanel messagePanel;
	private RaceInfoForm form;
	private boolean editing = false;
	
	public AdminRacePanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		// setHeight("50%");
		Label header = new ContentTitleLabel("Opret eller rediger et løb");
		add(header);
		add(messagePanel = new MessagePanel());
		setCellHeight(messagePanel, "18px");
		
		add(getRaces());
		
		Panel formPanel = new FlowPanel();
		formPanel.add(form = new RaceInfoForm());
		add(formPanel);
		setCellVerticalAlignment(formPanel, VerticalPanel.ALIGN_TOP);
	}
	
	private ListBox getRaces() {
		final ListBox races = new ListBox();
		races.setStyleName("form");
		races.addItem("Opret");
		for (int i = 0; i < getMediator().getSeason().getRaces().size(); i++) {
			ClientRace race = (ClientRace) getMediator().getSeason().getRaces().get(i);
			races.addItem(race.getName(), race.getId().toString());
		}
		
		races.addClickListener(new ClickListener() {

			public void onClick(Widget arg0) {
				if (races.getSelectedIndex() == 0) {
					form.setRace(new ClientRace());
					editing = false;
					return;
				}
				long id  = Long.parseLong(races.getValue(races.getSelectedIndex()));
				for (int i = 0; i < getMediator().getSeason().getRaces().size(); i++) {
					ClientRace race = (ClientRace) getMediator().getSeason().getRaces().get(i);
					if (race.getId().longValue() == id) {
						editing = true;
						form.setRace(race);
					}
				}
			}
		});
		
		return races;
	}

	private class RaceInfoForm extends FlexTable {

		private Label raceNameLabel;
		private TextBox raceName;
		
		private DriverSelection selectedDriver = new DriverSelection(getMediator(), "Udvalgte køre");

		private DatePicker openDate;

		private DatePicker closeDate;
		private TimePicker closeTime;
		
		private ClientRace race;

		public RaceInfoForm() {
			setStyleName("form");
			int row = 0; 
			race = new ClientRace();
			setWidget(row, 0, new FormTitleLabel("Løbs information"));
			getFlexCellFormatter().setColSpan(row++, 0, 2);
			setWidget(row, 0, raceNameLabel = new FormLabel("Spillernavn"));
			setWidget(row++, 1, raceName = new TextBox());

			setWidget(row, 0, new FormLabel("Løbet åbner"));
			setWidget(row++, 1, openDate = new DatePicker(new Date()));
			
			setWidget(row, 0, new FormLabel("Løbet lukker"));
			setWidget(row++, 1, closeDate = new DatePicker(new Date()));
			setWidget(row++, 1, closeTime = new TimePicker(new Date()));

			Label spacer = new Label(" ");
			spacer.setStyleName("form-Spacer");
			setWidget(row, 0, spacer);
			getFlexCellFormatter().setColSpan(row++, 0, 2);
			
			setWidget(row, 0, new FormLabel("Udvalgte køre"));
			setWidget(row++, 1, selectedDriver);

			Button updatePersonalInfo = new Button("Opret", new ClickListener() {

				public void onClick(Widget arg0) {
					if (validate() == true) {
						
						race.setName(raceName.getText());
						race.setSelectedDriver(selectedDriver.getDriver());
						race.setOpenDate(openDate.getDate());
						closeTime.setDate(closeDate.getDate());
						race.setCloseDate(closeTime.getDate());
						
						AsyncCallback callback = new AsyncCallback() {

							public void onFailure(Throwable exception) {
								getMediator().reportError(exception.getMessage());
							}

							public void onSuccess(Object arg0) {
								String message = isEditing() ? "Løbet er blevet opdateret" : "Løbet er blevet oprettet"; 
								messagePanel.setStatus(message);
							}

						};
						if (isEditing()) {
							getMediator().getGameService().updateRace(race, callback);
						} else {
							getMediator().getGameService().createRace(race, callback);
						}
						
					}
				}
			});
			setWidget(row, 1, updatePersonalInfo);
			getFlexCellFormatter().setColSpan(row++, 1, 2);
		}
		
		public void setRace(ClientRace race) {
			if (race == null) {
				return;
			}
			this.race = race;
			raceName.setText(race.getName());
			selectedDriver.setDriver(race.getSelectedDriver());
			openDate.setDate(race.getOpenDate());
			closeDate.setDate(race.getCloseDate());
			closeTime.setTime(race.getCloseDate());
			
		}

		private boolean validate() {
			Rule[] rules = new Rule[] {
				new CompositeRule().addRule(new RequiredRule(raceName, raceNameLabel)).addRule(new MaxLengthRule(raceName, raceNameLabel, 25)),
				};
			List issues = AdminRacePanel.this.validate(rules);

			if (issues.size() != 0) {
				messagePanel.setError("Kunne ikke opdatere dine informationer. Se de markerede felter");
			}

			unmarkFields(rules);
			markIssues(issues);

			return issues.size() == 0 ? true : false;
		}
	}
	
	public boolean isEditing() {
		return editing;
	}

	protected String getScreenTitle() {
		return "Administrer løb";
	}
}
