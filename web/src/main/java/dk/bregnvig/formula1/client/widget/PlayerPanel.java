package dk.bregnvig.formula1.client.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTitleLabel;

public class PlayerPanel extends ContentPanel {

	private MessagePanel messagePanel;
	
	public PlayerPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		// setHeight("50%");
		add(new ContentTitleLabel("Opdater dine oplysninger her"));
		add(messagePanel = new MessagePanel());
		setCellHeight(messagePanel, "18px");
		Panel formPanel = new HorizontalPanel();
		formPanel.add(new PasswordForm());
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
			List issues = PlayerPanel.this.validate(rules);

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

		private Label firstNameLabel;

		private TextBox firstName;

		private Label lastNameLabel;

		private TextBox lastName;

		private Label emailAddressLabel;

		private TextBox emailAddress;

		private Label smsNumberLabel;

		private TextBox smsNumber;

		public PersonalInfoForm() {
			setStyleName("form");
			setWidget(0, 0, new FormTitleLabel("Personlig information"));
			getFlexCellFormatter().setColSpan(0, 0, 2);
			setWidget(1, 0, firstNameLabel = new FormLabel("Fornavn"));
			setWidget(1, 1, firstName = new TextBox());
			firstName.setText(getMediator().getPlayer().getFirstName());

			setWidget(2, 0, lastNameLabel = new FormLabel("Efternavn"));
			setWidget(2, 1, lastName = new TextBox());
			lastName.setText(getMediator().getPlayer().getLastName());

			setWidget(3, 0, emailAddressLabel = new FormLabel("E-mail-adresse"));
			setWidget(3, 1, emailAddress = new TextBox());
			emailAddress.setText(getMediator().getPlayer().getEmailAddress());

			setWidget(4, 0, smsNumberLabel = new FormLabel("Mobilnummer"));
			setWidget(4, 1, smsNumber = new TextBox());
			smsNumber.setText(getMediator().getPlayer().getSms());

			Button updatePersonalInfo = new Button("Opdater", new ClickListener() {

				public void onClick(Widget arg0) {
					if (validate() == true) {
						ClientPlayer player = getMediator().getPlayer();
						player.setFirstName(firstName.getText());
						player.setLastName(lastName.getText());
						player.setEmailAddress(emailAddress.getText());
						player.setSms(smsNumber.getText());
						AsyncCallback callback = new AsyncCallback() {

							public void onFailure(Throwable exception) {
								getMediator().reportError(exception.getMessage());
							}

							public void onSuccess(Object arg0) {
								messagePanel.setStatus("Dine informationer er blevet opdateret");
							}

						};
						getMediator().getGameService().updatePlayer(player, callback);
					}
				}
			});
			setWidget(5, 1, updatePersonalInfo);
			getFlexCellFormatter().setColSpan(5, 1, 2);
		}

		private boolean validate() {
			Rule[] rules = { new CompositeRule().addRule(new RequiredRule(firstName, firstNameLabel)).addRule(new MaxLengthRule(firstName, firstNameLabel, 25)),
					new CompositeRule().addRule(new RequiredRule(lastName, lastNameLabel)).addRule(new MaxLengthRule(lastName, lastNameLabel, 25)),
					new CompositeRule().addRule(new RequiredRule(emailAddress, emailAddressLabel)).addRule(new EmailRule(emailAddress, emailAddressLabel)),
					new CompositeRule().addRule(new PhoneNumberRule(smsNumber, smsNumberLabel)) };
			List issues = PlayerPanel.this.validate(rules);

			if (issues.size() != 0) {
				messagePanel.setError("Kunne ikke opdatere dine informationer. Se de markerede felter");
			}

			unmarkFields(rules);
			markIssues(issues);

			return issues.size() == 0 ? true : false;
		}
	}

	protected String getScreenTitle() {
		return "Mine oplysninger";
	}

}
