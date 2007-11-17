package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import dk.bregnvig.formula1.client.F2007;

public class WelcomePanel extends VerticalPanel {
	
	private Label title;
	private F2007 mediator;
	
	public WelcomePanel(F2007 mediator, ClickListener listener) {
		this.mediator = mediator;
		
		setHeight("100%");
		setWidth("100%");
		
		title = new Label();
		title.addStyleDependentName("title");
		title.addClickListener(listener);
		add(title);
		setCellHorizontalAlignment(title, HasAlignment.ALIGN_CENTER);
	}

	protected void onLoad() {
		super.onLoad();
		getSeasonName();
	}
	
	private void getSeasonName() {
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				title.setText("Failure");
			}

			public void onSuccess(Object result) {
				title.setText(result.toString());
			}
		};
		mediator.getGameService().getSeasonName(callback);
	}
}
