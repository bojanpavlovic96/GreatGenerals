package ui;

import java.util.List;

import board.Board;

public interface UserInterface {

	void addEventHandler(String event_name, UIEventHandler event_handler);

	List<UIEventHandler> getEventHandlers(String event_name);

	UIEventHandler getSingleEventHandler(String event_name, String handler_name);

	boolean removeEventHandler(String event_name, String handler_name);

	void startGameLoop();

	void stopGameLoop();

	Board getPrintingContent();

	void setPrintingContent();

	void render();

}
