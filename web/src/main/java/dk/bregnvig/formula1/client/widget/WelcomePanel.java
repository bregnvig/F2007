package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.widget.control.CountDownLabel;

public class WelcomePanel extends VerticalPanel {
	
	private Label title;
	private CountDownLabel countDown;
	private F2007 mediator;
	
	public WelcomePanel(F2007 mediator, ClickListener listener) {
		this.mediator = mediator;
		
		title = new Label();
		title.addStyleDependentName("title");
		title.addClickListener(listener);
		countDown = new CountDownLabel();
		countDown.addStyleDependentName("mini");
		add(title);
		add(countDown);
		setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		setCellHorizontalAlignment(title, HasAlignment.ALIGN_CENTER);
	}

	protected void onLoad() {
		super.onLoad();
		getSeasonName();
		getCurrentRace();
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

	private void getCurrentRace() {
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				title.setText("Failure");
			}

			public void onSuccess(Object result) {
				if (result != null) {
					countDown.setRace((ClientRace) result);
				}
			}
		};
		mediator.getGameService().getCurrentRace(callback);
	}
}
