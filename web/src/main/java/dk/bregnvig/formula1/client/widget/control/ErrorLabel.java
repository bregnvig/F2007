package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.Label;

public class ErrorLabel extends Label {

	public ErrorLabel() {
		initialize();
	}

	public ErrorLabel(String text) {
		super(text);
		initialize();
	}

	public ErrorLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		initialize();
	}

	private void initialize() {
		addStyleDependentName("error");
	}
}
