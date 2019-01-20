package app.event;

import java.util.List;

import model.PlayerData;

public interface GameReadyHandler {

	void execute(List<PlayerData> players);

}
