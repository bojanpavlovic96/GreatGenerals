package root.view.event;

import java.util.List;

public interface ViewEventProducer {

	void addEventHandler(String event_name, ViewEventHandler event_handler);

	List<ViewEventHandler> getEventHandlers(String event_name);

	ViewEventHandler getSingleEventHandler(String event_name, String handler_name);

	ViewEventHandler removeEventHandler(String event_name, String handler_name);

	List<ViewEventHandler> removeAllEventHandlers(String event_name);

}
