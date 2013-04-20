package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.TextBox;

public class FormTextBox extends TextBox {

	private String hint;
	
	public FormTextBox() {
		super();
	}
	
	public FormTextBox(String hint) {
		this.hint = hint;
		setTitle(hint);
	}

	public void setTitle(String title) {
		if ((title == null || title.length() == 0) && hint != null) {
			super.setTitle(hint);
		} else {
			super.setTitle(title);
		}
	}
}
