package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class WaitingPanel extends HorizontalPanel {

	public WaitingPanel(String width, String height) {
		Image image = new WaitingImage();
		add (image);
		setWidth(width);
		setHeight(height);
		setCellHorizontalAlignment(image, HorizontalPanel.ALIGN_CENTER);
		setCellVerticalAlignment(image, HorizontalPanel.ALIGN_MIDDLE);
	}
}
