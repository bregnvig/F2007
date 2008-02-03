package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.Label;

public class PlayerLabel extends Label {

	public PlayerLabel() {
		initialize();
	}

	public PlayerLabel(String text) {
		super(text);
		initialize();
	}

	public PlayerLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		initialize();
	}

	private void initialize() {
	}
}
