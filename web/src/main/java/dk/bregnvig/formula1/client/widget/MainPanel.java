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
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.WaitingPanel;

public class MainPanel extends DockPanel {

	private F2007 mediator;
	private boolean loadSeasonCompleted = false;
	private boolean loadOpenRaceCompleted = false;
	private Panel currentCenter;
	private HorizontalPanel northPanel;
	private Widget screenTitle;
	private WaitingPanel waiting = new WaitingPanel("100%", "100%");
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientRace>
	 */
	private ClientRace openRace;
	
	public MainPanel(F2007 mediator) {
		this.mediator = mediator;
		setWidth("100%");
		setHeight("100%");
		setCenterPanel(waiting);
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
				initialized();
			}
		};
		mediator.getGameService().getSeason(callback);
	}
	
	public void setScreenTitle(Widget title) {
		int index = northPanel.getWidgetIndex(screenTitle);
		northPanel.remove(index);
		northPanel.insert(screenTitle = title, index);
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
				initialized();
			}
		};
		mediator.getGameService().getCurrentRace(callback);
	}
	
	private void initialized() {
		if (isAsyncCompleted() == false) {
			return;
		}
		createNorth();
		if (openRace != null) {
			setCenterPanel(new RacePanel(mediator, this, openRace));
		} else {
			HorizontalPanel panel = new HorizontalPanel();
			panel.setWidth("100%");
			panel.setHeight("100%");
			BigLabel message = new BigLabel("Der er ikke flere løb");
			panel.add(message);
			panel.setCellHorizontalAlignment(message, HorizontalPanel.ALIGN_CENTER);
			panel.setCellVerticalAlignment(message, HorizontalPanel.ALIGN_MIDDLE);
			setCenterPanel(panel);
		}
	}

	private void createNorth() {
		northPanel = new HorizontalPanel();
		northPanel.setWidth("97%");
		northPanel.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
		northPanel.setStyleName("southBorder");
		
	    // Make a command that we will execute from all leaves.

	    Label welcomeLabel = new Label(mediator.getSeason().getName());
	    northPanel.add(welcomeLabel);
		northPanel.setCellHorizontalAlignment(welcomeLabel, HorizontalPanel.ALIGN_LEFT);
		
		northPanel.add(screenTitle = new Label());
		northPanel.setCellHorizontalAlignment(screenTitle, HorizontalPanel.ALIGN_CENTER);
	    
		MenuBar playerMenu = new MenuBar(true);
		playerMenu.addItem("Konto", new OpenAccountCommand());
		playerMenu.addItem("WBC", new OpenWBCCommand());
		playerMenu.addItem("Oplysninger", new OpenUserCommand());
		playerMenu.addItem("Regler", new OpenRulesCommand());
		
		MenuBar mainMenu = new MenuBar();
		mainMenu.addItem("Løb", getRaceMenuBar());
		mainMenu.addItem("Min menu", playerMenu);
		mainMenu.setAutoOpen(true);

		if (mediator.getPlayer().isGameAdministrator()) {
			MenuBar adminMenu = new MenuBar(true);
			adminMenu.addItem("Spiller", new CreateUserCommand());
			adminMenu.addItem("Løb", new RaceAdminCommand());
			adminMenu.addItem("Køre", new DriverAdminCommand());
			adminMenu.addItem("Konto", new AccountAdminCommand());
			mainMenu.addItem("Admin", adminMenu);
		}

		northPanel.add(mainMenu);
		northPanel.setCellHorizontalAlignment(mainMenu, HorizontalPanel.ALIGN_RIGHT);
		add(northPanel, DockPanel.NORTH);
		setCellHeight(northPanel, "5px"); // Automatically adjust to minimum size required
	}
	
	public void setCenterPanel(Panel panel) {
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
			OpenRaceCommand command = new OpenRaceCommand(race);
			raceMenu.addItem(new MenuItem(race.getName(), command));
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

    private class OpenWBCCommand implements Command {
    	private WBCPanel panel = new WBCPanel(mediator, MainPanel.this);
		public void execute() {
			setCenterPanel(panel);
		}
    }

    private class OpenUserCommand implements Command {
		public void execute() {
			setCenterPanel(new AdminPlayerPanel(mediator, MainPanel.this, false));
		}
    }

    private class OpenRulesCommand implements Command {
		public void execute() {
			setCenterPanel(new RulesPanel(mediator, MainPanel.this));
		}
    }

    private class CreateUserCommand implements Command {
		public void execute() {
			setCenterPanel(new AdminPlayerPanel(mediator, MainPanel.this, true));
		}
    }

    private class RaceAdminCommand implements Command {
		public void execute() {
			setCenterPanel(new AdminRacePanel(mediator, MainPanel.this));
		}
    }

    private class DriverAdminCommand implements Command {
		public void execute() {
			setCenterPanel(new AdminDriverPanel(mediator, MainPanel.this));
		}
    }
    
    private class AccountAdminCommand implements Command {
		public void execute() {
			setCenterPanel(new AdminAccountPanel(mediator, MainPanel.this));
		}
    }
    
    private class OpenRaceCommand implements Command {
    	
    	private ClientRace race;
    	
    	public OpenRaceCommand(ClientRace race) {
    		this.race = race;
    	}
    	
		public void execute() {
			setCenterPanel(new RacePanel(mediator, MainPanel.this, race));
		}
    }    
}
