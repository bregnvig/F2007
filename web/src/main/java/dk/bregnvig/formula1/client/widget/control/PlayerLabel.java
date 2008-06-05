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
		Label name = new Label(player.getName()); 
		add(name);
		if (player.isWbcParticipant()) {
			Image image = new Image("other/goblet_gold_add.png");
			image.addStyleDependentName("ZeroMargin");
			image.setTitle("Deltager i WBC'en");
			add(image);
		}
		setHeight("20px");
	}
}
