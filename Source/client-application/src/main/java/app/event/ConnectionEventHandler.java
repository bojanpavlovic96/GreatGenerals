package app.event;

import app.launcher.RabbitConnectionEventType;
import app.launcher.RabbitConnectionTask;

public interface ConnectionEventHandler {

	void handleConnectionEvent(
			RabbitConnectionTask connectionTask,
			RabbitConnectionEventType eventType);

}
