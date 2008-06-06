package dk.bregnvig.formula1.client.widget.control;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class PlayerListBox extends ListBox {

	private F2007 mediator;
	
	/**
	 *  @gwt.typeArgs <dk.bregnvig.formula1.client.domain.ClientPlayer>
	 */
	private List fetchedPlayers;

	public PlayerListBox(F2007 mediator) {
		super();
		this.mediator = mediator;
		addItem("Vælg");
		loadPlayers();
	}
	
	private void loadPlayers() {
		
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable exception) {
				mediator.reportError(exception.getMessage());
			}

			public void onSuccess(Object argument) {
				fetchedPlayers = (List) argument;
				setPlayers();
			}
		};
		mediator.getGameService().findAllPlayers(callback);
	}
	
	public void setPlayers() {
		
		for (int i = 0; i < fetchedPlayers.size(); i++) {
			ClientPlayer player = (ClientPlayer) fetchedPlayers.get(i);
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
