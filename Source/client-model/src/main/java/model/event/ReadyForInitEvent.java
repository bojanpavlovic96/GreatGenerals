package model.event;

import root.model.event.ModelEventArg;

public class ReadyForInitEvent extends ModelEventArg {

	public String roomName;

	public ReadyForInitEvent(String playerName, String roomName) {
		super(ModelEventType.ReadyForInit, playerName);

		this.roomName = roomName;
	}

}
