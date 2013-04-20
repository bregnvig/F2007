package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;

public class FormTitleLabel extends Label {

	public FormTitleLabel() {
		initialize();
	}

	public FormTitleLabel(String text) {
		super(text);
		initialize();
	}

	public FormTitleLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		initialize();
	}

	private void initialize() {
		setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		addStyleDependentName("form-title");
	}
}
