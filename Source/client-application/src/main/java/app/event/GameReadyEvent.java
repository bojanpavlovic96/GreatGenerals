package app.event;

import java.util.List;

import model.PlayerData;

public interface GameReadyEvent {

	void execute(List<PlayerData> players);

}
