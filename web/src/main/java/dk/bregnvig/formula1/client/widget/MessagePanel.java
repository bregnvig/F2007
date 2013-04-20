package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import dk.bregnvig.formula1.client.widget.control.ErrorLabel;

public class MessagePanel extends FlowPanel {

	private Label status;
	private ErrorLabel error;
	
	public MessagePanel() {
		add(status = new Label());
		add(error = new ErrorLabel());
	}

	public void setStatus(String statusText) {
		Timer timer = new Timer() {
			public void run() {
				status.setText("");
			}
		};
		timer.schedule(2500);
		error.setText("");
		status.setText(statusText);
	}

	public void setError(String errorText) {
		status.setText("");
		error.setText(errorText);
	}
}
