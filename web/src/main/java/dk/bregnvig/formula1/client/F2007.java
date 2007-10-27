package dk.bregnvig.formula1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class F2007 implements EntryPoint {
	
	private String seasonName;
	private final Label label = new Label();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button button = new Button("Click me");

		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (label.getText().equals(""))
					getSeasonName();
				else
					label.setText("");
			}
		});

		// Assume that the host HTML has elements defined whose
		// IDs are "slot1", "slot2". In a real app, you probably would not want
		// to hard-code IDs. Instead, you could, for example, search for all
		// elements with a particular CSS class and replace them with widgets.
		//
		DockPanel panel = new DockPanel();
		panel.add(button, DockPanel.CENTER);
		panel.setCellWidth(button, "200px%");
		panel.setCellHeight(button, "400px");
		panel.setCellHorizontalAlignment(button, HasAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(button, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.add(label, DockPanel.SOUTH);
		panel.setCellHorizontalAlignment(label, HasAlignment.ALIGN_CENTER);
		RootPanel.get("slot1").add(panel);
	}

	private void getSeasonName() {
		SeasonServiceAsync service = (SeasonServiceAsync) GWT.create(SeasonService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc/season";
		endpoint.setServiceEntryPoint(moduleRelativeURL);

		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				label.setText("Failure! " + caught.getMessage());
			}

			public void onSuccess(Object result) {
				seasonName = (String) result;
				label.setText(seasonName);
			}
		};
		service.getSeasonName(callback);
	}
}
