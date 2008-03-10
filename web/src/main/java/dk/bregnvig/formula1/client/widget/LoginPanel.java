package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTitleLabel;

public class LoginPanel extends VerticalPanel {

	private final F2007 mediator;
	private final TextBox playerName;
	private final PasswordTextBox password;
	
	public LoginPanel(final F2007 mediator) {
		
		this.mediator = mediator;
		if (this.mediator == null) {
			throw new IllegalArgumentException("Must be specified");
		}
		
		setStyleName("f2007-input-panel");
		
		setWidth("300");
		
		FlexTable table = new FlexTable();
		table.setWidth("20%");
		
		table.setWidget(0, 0, new FormLabel("Spiller"));
		table.setWidget(0, 1, playerName = new TextBox());
		
		table.setWidget(1, 0, new FormLabel("Adgangskode"));
		table.setWidget(1, 1, password = new PasswordTextBox());
		
		if (mediator.isTesting()) {
			playerName.setText("flb");
			password.setText("flb");
		}
		password.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyPress(Widget widget, char keyCode, int modifiers) {
				if (keyCode == KeyboardListener.KEY_ENTER) {
					mediator.getGameService().login(playerName.getText(), password.getText(), callback);
				}
			}
		});
		
		add(new FormTitleLabel("Login"));
		add(table);
		Button button = new Button("Kom ind");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget ignore) {
				mediator.getGameService().login(playerName.getText(), password.getText(), callback);
			}
		});
		add(button);
		
		setCellHorizontalAlignment(table, HasAlignment.ALIGN_CENTER);
		setCellHorizontalAlignment(button, HasAlignment.ALIGN_CENTER);
	}
	
	private AsyncCallback callback = new AsyncCallback() {

		public void onFailure(Throwable caught) {
			mediator.setLoggedIn(null);
			if (caught instanceof CredentialException) {
				mediator.reportError("Kunne ikke logge på");
			}
		}

		public void onSuccess(Object result) {
			mediator.setLoggedIn((ClientPlayer) result);
		}
	};
}
