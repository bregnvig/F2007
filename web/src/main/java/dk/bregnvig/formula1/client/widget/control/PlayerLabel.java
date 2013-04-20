package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class PlayerLabel extends HorizontalPanel {
	
	private ClientPlayer player;

	public PlayerLabel(ClientPlayer player) {
		this.player = player;
		initialize();
	}

	private void initialize() {
		String imageName = null;
		switch (player.getLastYearWBC()) {
		case 1:
			imageName = "other/goblet_gold.png"; 
			break;
		case 2:
			imageName = "other/goblet_silver.png"; 
			break;
		case 3:
			imageName = "other/goblet_bronze.png"; 
			break;
		default:
			break;
		}
		if (imageName != null) {
			Image image = new Image(imageName);
			image.addStyleDependentName("ZeroMargin");
			image.setTitle("Nummer " + player.getLastYearWBC() + " i sidste års WBC");
			add(image);
		}
		Label name = new Label(player.getName()); 
		add(name);
		if (player.isWbcParticipant()) {
			Image image = new Image("other/Star-16x16.png");
			image.addStyleDependentName("ZeroMargin");
			image.setTitle("Deltager i WBC'en");
			add(image);
		}
		setHeight("20px");
	}
}
