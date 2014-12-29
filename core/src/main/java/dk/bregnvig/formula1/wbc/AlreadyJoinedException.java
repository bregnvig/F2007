package dk.bregnvig.formula1.wbc;

import dk.bregnvig.formula1.Player;

public class AlreadyJoinedException extends WBCException {

	public AlreadyJoinedException(Player player) {
		super(String.format("%s has already joined WBC", player.getPlayername()));
	}
}
