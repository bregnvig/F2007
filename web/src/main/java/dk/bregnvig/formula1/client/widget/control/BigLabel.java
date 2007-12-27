package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.Label;

public class BigLabel extends Label {

	public BigLabel() {
		initialize();
	}

	public BigLabel(String text) {
		super(text);
		initialize();
	}

	public BigLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		initialize();
	}

	private void initialize() {
		addStyleDependentName("big");
	}
}
