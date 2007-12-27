package dk.bregnvig.formula1.client.widget;

import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;

public class MainPanel extends DockPanel {

	private F2007 mediator;
	private boolean loadSeasonCompleted = false;
	private boolean loadOpenRaceCompleted = false;
	private Panel currentCenter;
	private Label screenTitle;
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientRace>
	 */
	private ClientRace openRace;
	
	public MainPanel(F2007 mediator) {
		this.mediator = mediator;
		setWidth("100%");
		setHeight("100%");
		screenTitle = new Label();
	}
	

	protected void onLoad() {
		super.onLoad();
		loadSeason();
		loadOpenRace();
	}
	
	/**
	 * Fetches all the races from the server
	 */
	private void loadSeason() {
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				mediator.reportError(caught.getMessage());
			}

			public void onSuccess(Object result) {
				loadSeasonCompleted = true;
				mediator.setSeason((ClientSeason) result);
				createNorth();
			}
		};
		mediator.getGameService().getSeason(callback);
	}
	
	public void setScreenTitle(String title) {
		screenTitle.setText(title);
	}

	/**
	 * Fetches all the races from the server
	 */
	private void loadOpenRace() {
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				mediator.reportError(caught.getMessage());
			}

			public void onSuccess(Object result) {
				loadOpenRaceCompleted = true;
				openRace = (ClientRace) result;
				createNorth();
			}
		};
		mediator.getGameService().getOpenRace(callback);
	}

	private void createNorth() {
		if (isAsyncCompleted() == false) {
			return;
		}
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("97%");
		panel.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
		panel.setStyleName("north");
		
	    // Make a command that we will execute from all leaves.

	    Label welcomeLabel = new Label(mediator.getSeason().getName());
	    panel.add(welcomeLabel);
		panel.setCellHorizontalAlignment(welcomeLabel, HorizontalPanel.ALIGN_LEFT);
		
		panel.add(screenTitle);
		panel.setCellHorizontalAlignment(screenTitle, HorizontalPanel.ALIGN_CENTER);
	    
		MenuBar playerMenu = new MenuBar(true);
		playerMenu.addItem("Konto", new OpenAccountCommand());
		playerMenu.addItem("Oplysninger", cmd);

		MenuBar mainMenu = new MenuBar();
		mainMenu.addItem("Løb", getRaceMenuBar());
		mainMenu.addItem("Min menu", playerMenu);
		mainMenu.setAutoOpen(true);
		
		panel.add(mainMenu);
		panel.setCellHorizontalAlignment(mainMenu, HorizontalPanel.ALIGN_RIGHT);
		add(panel, DockPanel.NORTH);
		setCellHeight(panel, "5px"); // Automatically adjust to minimum size required
	}
	
	private void setCenterPanel(Panel panel) {
		if (currentCenter != null) {
			remove(currentCenter);
		}
		add(panel, DockPanel.CENTER);
		setCellVerticalAlignment(panel, DockPanel.ALIGN_TOP);
		currentCenter = panel;
	}
	
	private MenuBar getRaceMenuBar() {
		Iterator i = mediator.getSeason().getRaces().iterator();
		MenuBar raceMenu = new MenuBar(true);
		
		while (i.hasNext()) {
			ClientRace race = (ClientRace) i.next();
			raceMenu.addItem(new MenuItem(race.getName(), cmd));
		}
		return raceMenu;
	}
	
	private boolean isAsyncCompleted() {
		return loadOpenRaceCompleted && loadSeasonCompleted;
	}
	
    Command cmd = new Command() {
	      public void execute() {
	        Window.alert("You selected a menu item!");
	      }
	    };
	 
    private class OpenAccountCommand implements Command {
		public void execute() {
			setCenterPanel(new AccountPanel(mediator, MainPanel.this));
		}
    }
}
