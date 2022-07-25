package root.view.event;

import java.util.List;

public interface ViewEventProducer {

	void addEventHandler(String eventName, ViewEventHandler eventHandler);

	List<ViewEventHandler> getEventHandlers(String eventName);

	ViewEventHandler getSingleEventHandler(String eventName, String handlerName);

	ViewEventHandler removeEventHandler(String eventName, String handlerName);

	List<ViewEventHandler> removeAllEventHandlers(String eventName);

}
