package model.intention;

import root.model.event.ClientIntention;

public class ReadyForInitIntention extends ClientIntention {

	public String roomName;

	public ReadyForInitIntention(String playerName, String roomName) {
		super(ClientIntentionType.ReadyForInit, playerName);

		this.roomName = roomName;
	}

}
