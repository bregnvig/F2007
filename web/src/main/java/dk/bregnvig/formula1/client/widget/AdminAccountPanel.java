package dk.bregnvig.formula1.client.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.validation.CompositeRule;
import dk.bregnvig.formula1.client.validation.IntegerRule;
import dk.bregnvig.formula1.client.validation.MaxValueRule;
import dk.bregnvig.formula1.client.validation.RequiredListboxSelectionRule;
import dk.bregnvig.formula1.client.validation.RequiredRule;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTitleLabel;

public class AdminAccountPanel extends ContentPanel {

	private MessagePanel messagePanel;
	private AccountInfoForm form;
	
	/**
	 *  @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientPlayer>
	 */
	private List fetchedPlayers;
	
	public AdminAccountPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		// setHeight("50%");
		Label header = new ContentTitleLabel("Indsæt, hæv eller overfør");
		add(header);
		add(messagePanel = new MessagePanel());
		setCellHeight(messagePanel, "18px");
		
		HorizontalPanel formPanel = new HorizontalPanel();
		formPanel.add(form = new AccountInfoForm());
		add(formPanel);
		setCellVerticalAlignment(formPanel, VerticalPanel.ALIGN_MIDDLE);
	}
	
	
	
	protected void onLoad() {
		super.onLoad();
		loadPlayers();
	}



	private void loadPlayers() {
		
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable exception) {
				getMediator().reportError(exception.getMessage());
			}

			public void onSuccess(Object argument) {
				fetchedPlayers = (List) argument;
				form.setPlayers();
			}
		};
		getMediator().getGameService().findAllPlayers(callback);
	}

	private class AccountInfoForm extends FlexTable {

		private Label playerNameLabel;
		private ListBox playerName;
		
		private Label actionLabel;
		private final ListBox action;
		
		private Label toPlayerNameLabel;
		private ListBox toPlayerName;

		private Label amountLabel;
		private TextBox amount;

		private Label messageLabel;
		private TextBox message;
		
		private Button commitButton;
		
		public AccountInfoForm() {
			setStyleName("form");
			int row = 0; 
			setWidget(row, 0, new FormTitleLabel("Konto"));
			getFlexCellFormatter().setColSpan(row++, 0, 2);
			setWidget(row, 0, playerNameLabel = new FormLabel("Navn"));
			setWidget(row++, 1, playerName = new ListBox());
			playerName.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					ClientPlayer player = findPlayerByPlayername(playerName.getValue(playerName.getSelectedIndex()));
					if (player != null) {
						messagePanel.setStatus(player.getFirstName() + " " + player.getLastName() + " har kr. " + player.getBalance());
					}
 				}
				
			});

			setWidget(row, 0, actionLabel = new FormLabel("Handling"));
			setWidget(row++, 1, action = new ListBox());
			action.addItem("Vælg");
			action.addItem("Hæv", "withdraw");
			action.addItem("Indsæt", "deposit");
			action.addItem("Overfør", "transfer");
			action.addClickListener(new ClickListener() {
				public void onClick(Widget widget) {
					setToPlayerEnabled(isTransfer());
					if (isWithdraw()) {
						message.setText("Hævet");
					} else if (isDeposit()) {
						message.setText("Overført via bank");
					} else {
						message.setText("Overført");
					}
				}
			});
			
			setWidget(row, 0, toPlayerNameLabel = new FormLabel("Til"));
			setWidget(row++, 1, toPlayerName = new ListBox());
			setToPlayerEnabled(false);

			setWidget(row, 0, amountLabel = new FormLabel("Beløb"));
			setWidget(row++, 1, amount = new TextBox());
			amount.setMaxLength(5);
			
			setWidget(row, 0, messageLabel = new FormLabel("Besked"));
			setWidget(row++, 1, message = new TextBox());

			commitButton = new Button("Udfør", new ClickListener() {

				public void onClick(Widget arg0) {
					if (validate() == true) {
						AsyncCallback callback = new AsyncCallback() {
							public void onFailure(Throwable exception) {
								getMediator().reportError(exception.getMessage());
							}
							public void onSuccess(Object arg0) {
								playerName.setSelectedIndex(0);
								toPlayerName.setSelectedIndex(0);
								String message = "Transaktionen er gennemført"; 
								messagePanel.setStatus(message);
							}
						};
						if (isTransfer()) {
							transfer(callback);
						} else if (isDeposit()) {
							deposit(callback);
						} else {
							withdraw(callback);
						}
					}
				}
			});
			setWidget(row, 1, commitButton);
			getFlexCellFormatter().setColSpan(row++, 1, 2);
		}
		
		public void setToPlayerEnabled(boolean visible) {
			toPlayerName.setEnabled(visible);
		}
		
		public void setPlayers() {
			
			playerName.addItem("Vælg");
			toPlayerName.addItem("Vælg");
			
			for (int i = 0; i < fetchedPlayers.size(); i++) {
				ClientPlayer player = (ClientPlayer) fetchedPlayers.get(i);
				playerName.addItem(player.getName(), player.getPlayername());
				toPlayerName.addItem(player.getName(), player.getPlayername());
			}
		}

		private boolean validate() {
			Rule[] rules = new Rule[isTransfer() ? 5 : 4];
			rules[0] = new CompositeRule().addRule(new RequiredListboxSelectionRule(playerName, playerNameLabel, false)); 
			rules[1] = new CompositeRule().addRule(new RequiredListboxSelectionRule(action, actionLabel, false));
			rules[2] = new CompositeRule().addRule(new RequiredRule(amount, amountLabel)).addRule(new IntegerRule(amount, amountLabel)).addRule(new MaxValueRule(amount, amountLabel, 2000));
			rules[3] = new RequiredRule(message, messageLabel);
			if (isTransfer()) {
				rules[4] = new CompositeRule().addRule(new RequiredListboxSelectionRule(toPlayerName, toPlayerNameLabel, false));
			}
			List issues = AdminAccountPanel.this.validate(rules);

			if (issues.size() != 0) {
				messagePanel.setError("Kunne ikke gennemføre transaktionen. Se de markerede felter");
			}

			unmarkFields(rules);
			markIssues(issues);

			return issues.size() == 0 ? true : false;
		}
		
		private boolean isTransfer() {
			return "transfer".equals(action.getValue(action.getSelectedIndex()));
		}

		private boolean isDeposit() {
			return "deposit".equals(action.getValue(action.getSelectedIndex()));
		}
		
		private boolean isWithdraw() {
			return "withdraw".equals(action.getValue(action.getSelectedIndex()));
		}
		
		private void transfer(AsyncCallback callback) {
			ClientPlayer fromPlayer = findPlayerByPlayername(playerName.getValue(playerName.getSelectedIndex()));
			ClientPlayer toPlayer = findPlayerByPlayername(toPlayerName.getValue(toPlayerName.getSelectedIndex()));
			getMediator().getGameService().accountTransfer(fromPlayer, toPlayer, message.getText(), Integer.parseInt(amount.getText()), callback);
		}

		private void deposit(AsyncCallback callback) {
			ClientPlayer player = findPlayerByPlayername(playerName.getValue(playerName.getSelectedIndex()));
			getMediator().getGameService().accountDeposit(player, message.getText(), Integer.parseInt(amount.getText()), callback);
		}

		private void withdraw(AsyncCallback callback) {
			ClientPlayer player = findPlayerByPlayername(playerName.getValue(playerName.getSelectedIndex()));
			getMediator().getGameService().accountWithdraw(player, message.getText(), Integer.parseInt(amount.getText()), callback);
		}
	}
	
	private ClientPlayer findPlayerByPlayername(String playername) {
		if (fetchedPlayers == null || fetchedPlayers.isEmpty()) {
			return null;
		}
		for(int i = 0; i < fetchedPlayers.size(); i++) {
			ClientPlayer player = (ClientPlayer) fetchedPlayers.get(i); 
			if (player.getPlayername().equals(playername)) {
				return player;
			}
		}
		return null;
	}
	
	protected Widget getScreenTitle() {
		return new  BigLabel("Bank & Konto");
	}
}
