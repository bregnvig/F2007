package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.Image;

import dk.bregnvig.formula1.client.domain.ClientDriver;

public class DriverImage extends Image {

	public DriverImage() {
		setWidth("179px");
		setHeight("117px");
	}
	
	public DriverImage(ClientDriver driver) {
		setDriver(driver);
	}

	public void setDriver(ClientDriver driver) {
		if (driver == null || driver.getName() == null) {
			setUrl(null);
		} else {
			setUrl("drivers/"+driver.getName()+".png");
		}
	}
}


