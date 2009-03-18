package dk.bregnvig.formula1.client.widget.bid;

import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.widget.control.DriverImage;

public class DriverSelection extends VerticalPanel {
	
	private int currentDriverIndex;
	
	private final List<ClientDriver> activeDrivers;
	private final Label name = new Label();
	private final DriverImage image = new DriverImage();
	private final String title;
	
	public DriverSelection(F2007 mediator, String title) {

		setTitle(this.title = title);
		name.addStyleDependentName("driverSelection");
		activeDrivers = mediator.getSeason().getActiveDriver();
		
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		add(image);
		add(new ButtonPanel());
	}
	
	private class ButtonPanel extends HorizontalPanel {
		
		private final Button previous = new Button("&lt;");
		private final Button next = new Button("&gt;");
		
		public ButtonPanel() {
			setWidth("200px");
			previous.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if (currentDriverIndex > 0) {
						currentDriverIndex--;
					} else if (currentDriverIndex == 0) {
						currentDriverIndex = activeDrivers.size()-1;
					}
					changeDriver();
				}
			});
			
			next.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if (activeDrivers.size() > currentDriverIndex+1) {
						currentDriverIndex++;
					} else {
						currentDriverIndex = 0;
					}
					changeDriver();
				}
			});
			add(previous);
			setCellHorizontalAlignment(previous, HorizontalPanel.ALIGN_LEFT);
			add(name);
			setCellHorizontalAlignment(name, HorizontalPanel.ALIGN_CENTER);
			add(next);
			setCellHorizontalAlignment(next, HorizontalPanel.ALIGN_RIGHT);
			changeDriver();
		}
	}
	
	void setCurrentDriverIndex(int index) {
		currentDriverIndex = index;
		changeDriver();
	}
	
	private void changeDriver() {
		name.setText(getDriver().getName());
		image.setDriver(getDriver());
	}
	
	public ClientDriver getDriver() {
		return (ClientDriver) activeDrivers.get(currentDriverIndex);
	}
	
	public void setDriver(ClientDriver driver) {
		for (int i = 0; i < activeDrivers.size(); i++) {
			ClientDriver activeDriver = (ClientDriver) activeDrivers.get(i); 
			if (activeDriver.equals(driver)) {
				setCurrentDriverIndex(i);
				break;
			}
		}
	}
	
	public void setTitle(String title) {
		if (title == null || title.length() == 0) {
			super.setTitle(this.title);
		} else {
			super.setTitle(title);
		}
	}

	public String getTitle() {
		return title;
	}

}
