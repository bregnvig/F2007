package dk.bregnvig.formula1.client.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.validation.CompositeRule;
import dk.bregnvig.formula1.client.validation.EmailRule;
import dk.bregnvig.formula1.client.validation.Issue;
import dk.bregnvig.formula1.client.validation.MaxLengthRule;
import dk.bregnvig.formula1.client.validation.PhoneNumberRule;
import dk.bregnvig.formula1.client.validation.RequiredRule;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTitleLabel;
import dk.bregnvig.formula1.client.widget.control.PlayerListBox;

public class AdminPlayerPanel extends ContentPanel {

	private MessagePanel messagePanel;
	private boolean adminMode;
	private boolean createMode = true;
	
	public AdminPlayerPanel(F2007 mediator, MainPanel mainPanel, boolean adminMode) {
		super(mediator, mainPanel);
		this.adminMode = adminMode;
		// setHeight("50%");
		Label header = new ContentTitleLabel(isPlayerMode() ? "Opdater dine oplysninger her" : "Spiller administration");
		add(header);
		add(messagePanel = new MessagePanel());
		setCellHeight(messagePanel, "18px");
		Panel formPanel = new HorizontalPanel();
		if (isPlayerMode()) {
			formPanel.add(new PasswordForm());
		}
		formPanel.add(new PersonalInfoForm());
		add(formPanel);
		setCellVerticalAlignment(formPanel, VerticalPanel.ALIGN_MIDDLE);
	}

	private class PasswordForm extends FlexTable {
		private TextBox playerName;

		private Label password0Label;

		private PasswordTextBox password0;

		private Label password1Label;

		private PasswordTextBox password1;

		public PasswordForm() {
			setStyleName("form");
			setWidget(0, 0, new FormTitleLabel("Adgangskode"));
			getFlexCellFormatter().setColSpan(0, 0, 2);
			setWidget(1, 0, new FormLabel("Spiller navn"));
			setWidget(1, 1, playerName = new TextBox());
			playerName.setText(getMediator().getPlayer().getPlayername());
			playerName.setReadOnly(true);

			setWidget(2, 0, password0Label = new FormLabel("Adgangskode"));
			setWidget(2, 1, password0 = new PasswordTextBox());

			setWidget(3, 0, password1Label = new FormLabel("Adgangskode igen"));
			setWidget(3, 1, password1 = new PasswordTextBox());

			Button updatePassword = new Button("Opdater", new ClickListener() {

				public void onClick(Widget arg0) {
					if (validate() == true) {
						AsyncCallback callback = new AsyncCallback() {

							public void onFailure(Throwable exception) {
								getMediator().reportError(exception.getMessage());
							}

							public void onSuccess(Object arg0) {
								password0.setText("");
								password1.setText("");
								messagePanel.setStatus("Din adgangskoden er blevet opdateret");
							}

						};
						getMediator().getGameService().updatePassword(password0.getText(), callback);
					}
				}
			});
			setWidget(4, 1, updatePassword);
			getFlexCellFormatter().setColSpan(4, 1, 2);
		}

		private boolean validate() {
			Rule[] rules = { new RequiredRule(password0, password0Label), new RequiredRule(password1, password1Label) };
			List issues = AdminPlayerPanel.this.validate(rules);

			if (issues.size() == 0 && password0.getText().equals(password1.getText()) == false) {
				String statusText = "Adgangskoden skal være ens";
				Issue issue = new Issue(password0, statusText, password0Label);
				issues.add(issue);
				messagePanel.setError(statusText);
			} else if (issues.size() != 0) {
				messagePanel.setError("Adgangskoderne kan ikke være tomme");
			}
			unmarkFields(rules);
			markIssues(issues);

			return issues.size() == 0 ? true : false;
		}
	}

	private class PersonalInfoForm extends FlexTable {

		private Label playerNameLabel;
		private TextBox playerName;
		
		private Label firstNameLabel;
		private TextBox firstName;

		private Label lastNameLabel;
		private TextBox lastName;

		private Label emailAddressLabel;
		private TextBox emailAddress;

		private Label smsNumberLabel;
		private TextBox smsNumber;
		
		private PlayerListBox players;
		private CheckBox wbcParticipant;
//		private CheckBox partOfSeason;
		private Button updatePersonalInfo;
		
		private ClientPlayer player;

		public PersonalInfoForm() {
			setStyleName("form");
			
			if (isPlayerMode()) {
				player = getMediator().getPlayer();
			} else {
				player = new ClientPlayer();
			}
			int row = 0;
			setWidget(row++, 0, new FormTitleLabel("Personlig information"));
			getFlexCellFormatter().setColSpan(0, 0, 2);
			if (isAdminMode()) {
				setWidget(row, 0, new FormLabel("Spillere"));
				setWidget(row++, 1, players = new PlayerListBox(getMediator()));
				players.addClickListener(new ClickListener() {

					public void onClick(Widget arg0) {
						if (players.getSelectedPlayer() == null) {
							resetPlayer();
						} else {
							player = players.getSelectedPlayer();
							displayDriver();
						}
					}
					
				});
				setWidget(row, 0, playerNameLabel = new FormLabel("Spillernavn"));
				setWidget(row++, 1, playerName = new TextBox());
			}

			setWidget(row, 0, firstNameLabel = new FormLabel("Fornavn"));
			setWidget(row++, 1, firstName = new TextBox());

			setWidget(row, 0, lastNameLabel = new FormLabel("Efternavn"));
			setWidget(row++, 1, lastName = new TextBox());

			setWidget(row, 0, emailAddressLabel = new FormLabel("E-mail-adresse"));
			setWidget(row++, 1, emailAddress = new TextBox());

			setWidget(row, 0, smsNumberLabel = new FormLabel("Mobilnummer"));
			setWidget(row++, 1, smsNumber = new TextBox());

			if (isAdminMode()) {
//				setWidget(row, 0, new FormLabel("Deltager i sæsonen"));
//				setWidget(row++, 1, partOfSeason = new CheckBox());
				setWidget(row, 0, new FormLabel("Med i WBC?"));
				setWidget(row++, 1, wbcParticipant = new CheckBox());
			}

			updatePersonalInfo = new Button(isPlayerMode() ? "Opdater" : "Opret", new ClickListener() {

				public void onClick(Widget arg0) {
					if (validate() == true) {
						if (isAdminMode()) {
							player.setPlayername(playerName.getText());
							player.setWbcParticipant(wbcParticipant.isChecked());
//							player.setPartOfSeason(partOfSeason.isChecked());
						}
						player.setFirstName(firstName.getText());
						player.setLastName(lastName.getText());
						player.setEmailAddress(emailAddress.getText());
						player.setSms(smsNumber.getText());
						AsyncCallback callback = new AsyncCallback() {

							public void onFailure(Throwable exception) {
								getMediator().reportError(exception.getMessage());
							}

							public void onSuccess(Object arg0) {
								String message = isCreateMode() ? "Spilleren er blevet oprettet" : "Dine informationer er blevet opdateret"; 
								messagePanel.setStatus(message);
								if (isAdminMode()) {
									resetPlayer();
								}
							}

						};
						if (isCreateMode() == false) {
							getMediator().getGameService().updatePlayer(player, callback);
						} else {
							getMediator().getGameService().createPlayer(player, callback);
						}
					}
				}
			});
			setWidget(row, 1, updatePersonalInfo);
			getFlexCellFormatter().setColSpan(row++, 1, 2);
			if (isPlayerMode()) {
				displayDriver();
			}
		}

		private boolean validate() {
			Rule[] rules = new Rule[isAdminMode() ? 5 :4]; 
			rules[0] = new CompositeRule().addRule(new RequiredRule(firstName, firstNameLabel)).addRule(new MaxLengthRule(firstName, firstNameLabel, 25));
			rules[1] = new CompositeRule().addRule(new RequiredRule(lastName, lastNameLabel)).addRule(new MaxLengthRule(lastName, lastNameLabel, 25));
			rules[2] = new EmailRule(emailAddress, emailAddressLabel);
			rules[3] = new CompositeRule().addRule(new PhoneNumberRule(smsNumber, smsNumberLabel));
			if (isAdminMode()) {
				rules[4] = new CompositeRule().addRule(new RequiredRule(playerName, playerNameLabel)).addRule(new MaxLengthRule(playerName, playerNameLabel, 15));
			}
			List issues = AdminPlayerPanel.this.validate(rules);

			if (issues.size() != 0) {
				messagePanel.setError("Kunne ikke opdatere dine informationer. Se de markerede felter");
			}

			unmarkFields(rules);
			markIssues(issues);

			return issues.size() == 0 ? true : false;
		}

		private void resetPlayer() {
			playerName.setText("");
			firstName.setText("");
			lastName.setText("");
			emailAddress.setText("");
			smsNumber.setText("");
			updatePersonalInfo.setText("Opret");
			wbcParticipant.setChecked(false);
			players.setSelectedIndex(0);
			createMode = true;
		}
		
		private void displayDriver() {
			
			firstName.setText(player.getFirstName());
			lastName.setText(player.getLastName());
			emailAddress.setText(player.getEmailAddress());
			smsNumber.setText(player.getSms());
			if (isAdminMode()) {
				playerName.setText(player.getPlayername());
				wbcParticipant.setChecked(player.isWbcParticipant());
			}
			updatePersonalInfo.setText("Opdater");
			createMode = false;
		}
	}

	protected Widget getScreenTitle() {
		return new  BigLabel("Mine oplysninger");
	}

	public final boolean isAdminMode() {
		return adminMode;
	}
	
	private final boolean isCreateMode() {
		return createMode;
	}
	
	public final boolean isPlayerMode() {
		return adminMode == false;
	}

}
