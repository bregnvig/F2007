package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;

public class FormLabel extends Label {

	public FormLabel() {
		initialize();
	}

	public FormLabel(String text) {
		super(text);
		initialize();
	}

	public FormLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		initialize();
	}

	private void initialize() {
		setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		addStyleDependentName("form");
	}
}
