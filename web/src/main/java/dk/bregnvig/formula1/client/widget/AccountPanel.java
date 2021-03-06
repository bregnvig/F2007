package dk.bregnvig.formula1.client.widget;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.account.ClientAccountEntry;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.WaitingImage;

public class AccountPanel extends ContentPanel {
	
	private static int rowsPerPage = 10;
	
	private int currentPage = 0;
	private ClientAccount account;
	private Label amount = new Label();
	private Grid table;
	private ButtonPanel buttonPanel;
	private ClientPlayer player;
	
	public AccountPanel(F2007 mediator, MainPanel mainPanel, ClientPlayer player) {
		super(mediator, mainPanel);
		
		this.player = player;
		if (player != null) {
			setWidth("100%");
		}
		HorizontalPanel balancePanel = balancePanel();
		add(balancePanel);
		setCellHeight(balancePanel, "30%");
		
		tabelPanel();
		add(table);
		setCellHeight(table, "65%");

		add(buttonPanel = new ButtonPanel());
		setCellHeight(buttonPanel, "5%");
	}

	public AccountPanel(F2007 mediator, MainPanel mainPanel) {
		this(mediator, mainPanel, null);
	}

	protected Widget getScreenTitle() {
		return new  BigLabel("Konto og posteringer");
	}
	
	protected void onLoad() {
		super.onLoad();
		loadAccount();
	}


	/**
	 * Fetches all the races from the server
	 */
	private void loadAccount() {
		AsyncCallback<ClientAccount> callback = new AsyncCallback<ClientAccount>() {

			public void onFailure(Throwable caught) {
				getMediator().reportError(caught.getMessage());
			}

			public void onSuccess(ClientAccount result) {
				account = result;
				amount.setText(Integer.toString(account.getBalance()));
				table.resizeRows(1);
				fillTable();
				buttonPanel.changeState();
			}
		};
		if (player == null) {
			getMediator().getGameService().getAccount(callback);
		} else {
			getMediator().getGameService().getAccount(player, callback);
		}
	}
	

	private void fillTable() {
		DateTimeFormat formatter = DateTimeFormat.getFormat("dd.MM.yyyy");
		table.resizeRows(getNumberOfRows()+1);
		for (int i = 0; i < getNumberOfRows(); i++) {
			ClientAccountEntry entry = (ClientAccountEntry) account.getEntries().get(rowsPerPage*currentPage+i);
			Label date = new AccountLabel(formatter.format(entry.getDate()));
			Label message = new AccountLabel(entry.getMessage());
			Label amount = new AccountLabel(Integer.toString(entry.getAmount())+",00");
			amount.addStyleName("amount");
			table.setWidget(i+1, 0, date);
			table.setWidget(i+1, 1, message);
			table.setWidget(i+1, 2, amount);
			table.getRowFormatter().addStyleName(i+1, "accountEntryRow");
		}
	}
	
	private int getNumberOfRows() {
		return Math.min(rowsPerPage, (account.getEntries().size() - (rowsPerPage*currentPage)));
	}
	
	private class AccountLabel extends Label {

		public AccountLabel(String text) {
			super(text);
			addStyleName("accountEntry");
		}
	}
	
	private class ButtonPanel extends HorizontalPanel {
		
		private final Button previous = new Button("Forrige");
		private final Button next = new Button("Næste");
		private final Label pages = new Label();
		
		public ButtonPanel() {
			setWidth("100%");
			previous.setEnabled(false);
			previous.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if (currentPage > 0) {
						currentPage--;
						fillTable();
						changeState();
					}
				}
			});
			
			next.setEnabled(false);
			next.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if ((account.getEntries().size() / rowsPerPage) > currentPage) {
						currentPage++;
						fillTable();
						changeState();
					}
				}
			});
			FlowPanel tempPanel = new FlowPanel();
			tempPanel.add(previous);
			tempPanel.add(next);
			add(tempPanel);
			add(pages);
			setCellHorizontalAlignment(pages, HorizontalPanel.ALIGN_RIGHT);
		}
		
		void changeState() {
			previous.setEnabled(currentPage != 0);
			next.setEnabled((account.getEntries().size() / rowsPerPage) != currentPage);
			pages.setText("Side " + (currentPage + 1) + " af " + (account.getEntries().size() / rowsPerPage + 1));
		}
		
	}


	private void tabelPanel() {
		table = new Grid(2, 3);
		table.setWidget(0, 0, new Label("Dato"));
		table.setWidget(0, 1, new Label("Tekst"));
		Label amountLabel = new Label("Beløb");
		amountLabel.setHorizontalAlignment(Label.ALIGN_RIGHT);
		table.setWidget(0, 2, amountLabel);
		table.setWidth("100%");
		table.getCellFormatter().addStyleName(0, 0, "accountHeader");
		table.getCellFormatter().setWidth(0, 0, "33%");
		table.getCellFormatter().addStyleName(0, 1, "accountHeader");
		table.getCellFormatter().addStyleName(0, 2, "accountHeader");
		table.setWidget(1, 1, new WaitingImage());
		table.getCellFormatter().setHorizontalAlignment(1, 1, HorizontalPanel.ALIGN_CENTER);
	}

	private HorizontalPanel balancePanel() {
		HorizontalPanel balancePanel = new HorizontalPanel();
		balancePanel.setHeight("100%");
		balancePanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		HTML htmlLabel = new HTML("Nuværende saldo: kr.&nbsp;");
		htmlLabel.addStyleName("gwt-Label");
		htmlLabel.addStyleName("gwt-Label-big");
		balancePanel.add(htmlLabel);
		balancePanel.add(amount = new BigLabel("0"));
		balancePanel.add(new BigLabel(",00"));
		return balancePanel;
	}
	
}
