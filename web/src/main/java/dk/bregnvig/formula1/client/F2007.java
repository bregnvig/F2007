package dk.bregnvig.formula1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.client.service.GameServiceAsync;
import dk.bregnvig.formula1.client.widget.ErrorPanel;
import dk.bregnvig.formula1.client.widget.LoginPanel;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.WelcomePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class F2007 implements EntryPoint, GWT.UncaughtExceptionHandler {
	
	private WelcomePanel welcome;
	private ClientPlayer player;
	private GameServiceAsync service;
	private ClientSeason season;
	private ClientRace selectedRace;
	
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
		RootPanel.get("slot1").setWidth("100%");
		RootPanel.get("slot1").setHeight("100%");
		RootPanel.get("slot1").setStyleName("f2007-root-panel");
		loadGameService();
		if (isLoggedIn() == false) {
			resetView();
		} else {
			// Display main content
		}
	}
	
	public void resetView() {
		clear();
		add(welcome);
	}
	
	public void reportError(String errorMessage) {
		clear();
		add(new ErrorPanel(this, errorMessage));
	}
	
	private class LoginClickListener implements ClickListener {

		public void onClick(Widget noOneCares) {
			clear();
			add(new LoginPanel(F2007.this));
		}
	}
	
	public void setLoggedIn(ClientPlayer player) {
		this.player = player;
		if (player != null) {
			setGamePage();
		}
	}
	
	public boolean isLoggedIn() {
		return player != null;
	}
	
	public ClientPlayer getPlayer() {
		return player;
	}

	private void setGamePage() {
		clear();
		add(new MainPanel(this));
	}
	
	private void clear() {
		RootPanel.get("slot1").clear();
	}
	
	private void add(Panel panel) {
		RootPanel.get("slot1").add(panel);
	}
	
	public GameServiceAsync getGameService() {
		return service;
	}

	public void onUncaughtException(Throwable throwable) {
		GWT.log("Oh no!", throwable);
		reportError(throwable.getMessage());
	}

	
	private void loadGameService() {
		service = (GameServiceAsync) GWT.create(GameService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc/game";
		endpoint.setServiceEntryPoint(moduleRelativeURL);		
	}

	public ClientSeason getSeason() {
		return season;
	}

	public void setSeason(ClientSeason season) {
		this.season = season;
	}
	
	public boolean isTesting() {
		return false;
	}

	/**
	 * If no race is selected an exception will be thrown
	 * @return
	 */
	public ClientRace getSelectedRace() {
		if (selectedRace == null) {
			throw new IllegalStateException("No race selected. You should not try to get the current race");
		}
		return selectedRace;
	}
	
	public void forceReloadSelectedRace(final Callback callback) {
		getSelectedRace().setFullyLoaded(false);
		setSelectedRace(getSelectedRace(), callback);
	}

	public void setSelectedRace(final ClientRace selectedRace, final Callback callback) {
		
		int index = season.getRaces().indexOf(selectedRace);
		this.selectedRace = (ClientRace) season.getRaces().get(index);
		if (this.selectedRace.isFullyLoaded() == false) {
			AsyncCallback<ClientRace> gwtCallback = new AsyncCallback<ClientRace>() {

				public void onFailure(Throwable caught) {
					reportError("Failure - could not load " + selectedRace.getName() + " fully");
				}

				public void onSuccess(ClientRace result) {
					int index = season.getRaces().indexOf(selectedRace);
					season.getRaces().remove(index);
					season.getRaces().add(index, result);
					F2007.this.selectedRace = (ClientRace) result;
					callback.completed();
				}
			};
			getGameService().getRace(selectedRace.getId(), gwtCallback);
		} else {
			callback.completed();
		}
	}
	
	public interface Callback {
		void completed();
	}
}
