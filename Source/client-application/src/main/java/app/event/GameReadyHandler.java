package app.event;

import root.model.PlayerData;

public interface GameReadyHandler {

	void execute(PlayerData player, String roomName, boolean asReplay);

}
