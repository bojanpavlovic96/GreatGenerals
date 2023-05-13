package model.intention;

import root.model.event.ClientIntention;

public class ReadyForReplayIntention extends ClientIntention {
	public String roomId;

	public ReadyForReplayIntention(String playerName, String roomId) {
		super(ClientIntentionType.ReadyForReplay, playerName);

		this.roomId = roomId;
	}

}
