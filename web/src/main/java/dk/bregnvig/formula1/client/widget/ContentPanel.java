package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.ui.VerticalPanel;

import dk.bregnvig.formula1.client.F2007;

public abstract class ContentPanel extends VerticalPanel {

	private F2007 mediator;
	private MainPanel mainPanel;
	
	public ContentPanel(F2007 mediator, MainPanel mainPanel) {
		this.mediator = mediator;
		this.mainPanel = mainPanel;
		
		mainPanel.setScreenTitle(getScreenTitle());
		
		setWidth("80%");
		setHeight("90%");
	}
	
	abstract String getScreenTitle();

	public F2007 getMediator() {
		return mediator;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}
	
	
}
