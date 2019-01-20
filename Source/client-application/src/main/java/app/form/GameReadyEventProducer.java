package app.form;

import app.event.GameReadyHandler;

public interface GameReadyEventProducer {

	void setOnGameReadyHandler(GameReadyHandler handler);

	GameReadyHandler getGameReadyHandler();

}
