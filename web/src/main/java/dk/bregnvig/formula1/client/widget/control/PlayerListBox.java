package dk.bregnvig.formula1.client.widget.control;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class PlayerListBox extends ListBox {

	private F2007 mediator;
	
	private List<ClientPlayer> fetchedPlayers;

	public PlayerListBox(F2007 mediator) {
		super();
		this.mediator = mediator;
		addItem("Vælg");
		loadPlayers();
	}
	
	private void loadPlayers() {
		
		AsyncCallback<List<ClientPlayer>> callback = new AsyncCallback<List<ClientPlayer>>() {

			public void onFailure(Throwable exception) {
				mediator.reportError(exception.getMessage());
			}

			public void onSuccess(List<ClientPlayer> argument) {
				fetchedPlayers = argument;
				setPlayers();
			}
		};
		mediator.getGameService().findAllPlayers(callback);
	}
	
	public void setPlayers() {
		
		List<ClientPlayer> seasonPlayers = mediator.getSeason().getPlayers(); 
		for (int i = 0; i < fetchedPlayers.size(); i++) {
			ClientPlayer player = (ClientPlayer) fetchedPlayers.get(i);
			if (seasonPlayers.contains(player)) {
				player.setPartOfSeason(true);
			}
			addItem(player.getName(), player.getPlayername());
		}
	}
	
	/**
	 * @return the selected player or null if no player is selected
	 */
	public ClientPlayer getSelectedPlayer() {
		if (getSelectedIndex() == 0) {
			return null;
		}
		return (ClientPlayer) fetchedPlayers.get(getSelectedIndex()-1);
 	}
}
