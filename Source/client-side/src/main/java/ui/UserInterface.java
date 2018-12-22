package ui;

import java.util.List;

public interface UserInterface {

	void addEventHandler(String event_name, UIEventHandler event_handler);

	List<UIEventHandler> getEventHandlers(String event_name);
	
	UIEventHandler getSingleEventHandler(String event_name, String handler_id);
	
	boolean removeEventHandler(String event_name, String handler_id);

	void startGameLoop();

	void stopGameLoop();

}
