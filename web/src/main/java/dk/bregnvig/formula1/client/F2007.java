package dk.bregnvig.formula1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.client.service.GameServiceAsync;
import dk.bregnvig.formula1.client.widget.ErrorPanel;
import dk.bregnvig.formula1.client.widget.LoginPanel;
import dk.bregnvig.formula1.client.widget.WelcomePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class F2007 implements EntryPoint, GWT.UncaughtExceptionHandler {
	
	private WelcomePanel welcome;
	private boolean loggedIn = false;
	private GameServiceAsync service;
	
	public F2007() {
		GWT.setUncaughtExceptionHandler(this);
		welcome = new WelcomePanel(this, new LoginClickListener());
		
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// Assume that the host HTML has elements defined whose
		// IDs are "slot1", "slot2". In a real app, you probably would not want
		// to hard-code IDs. Instead, you could, for example, search for all
		// elements with a particular CSS class and replace them with widgets.
		//
		loadGameService();
		
		if (loggedIn == false) {
			resetView();
		} else {
			// Display main content
		}
	}
	
	public void resetView() {
		RootPanel.get("slot1").clear();
		RootPanel.get("slot1").add(welcome);
	}
	
	public void reportError(String errorMessage) {
		RootPanel.get("slot1").clear();
		RootPanel.get("slot1").add(new ErrorPanel(this, errorMessage));
	}
	
	private class LoginClickListener implements ClickListener {

		public void onClick(Widget noOneCares) {
			RootPanel.get("slot1").clear();
			RootPanel.get("slot1").add(new LoginPanel(F2007.this));
		}
	}
	
	public void setLoggedIn(boolean loogedIn) {
		if (loogedIn == true) {
			reportError("Ha ha. loggedIn");
		}
		this.loggedIn = loogedIn;
	}
	
	private void loadGameService() {
		service = (GameServiceAsync) GWT.create(GameService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc/game";
		endpoint.setServiceEntryPoint(moduleRelativeURL);		
	}
	
	public GameServiceAsync getGameService() {
		return service;
	}

	public void onUncaughtException(Throwable throwable) {
		GWT.log("Oh no!", throwable);
		reportError(throwable.getMessage());
	}
}
