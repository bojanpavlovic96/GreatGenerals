package model.intention;

import root.model.event.ClientIntention;

public class LeaveGameIntention extends ClientIntention {

	public LeaveGameIntention(String playerName) {
		super(ClientIntentionType.LeaveGame, playerName);

	}

}
