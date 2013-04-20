package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;

public class ErrorPanel extends VerticalPanel {
	
	private Label title;
	
	public ErrorPanel(final F2007 mediator, String errorMessage) {
		
		setHeight("100%");
		setWidth("100%");
		
		title = new Label(errorMessage);
		title.addStyleDependentName("error");
		
		Button button = new Button("Forfra");
		button.addClickListener(new ClickListener() {

			public void onClick(Widget arg0) {
				mediator.resetView();
			}
			
		});

		add(title);
		add(button);
		setCellHorizontalAlignment(title, HasAlignment.ALIGN_CENTER);
		setCellHorizontalAlignment(button, HasAlignment.ALIGN_CENTER);
	}
}
