package app.event;

import app.launcher.ConnectionTask;

public interface ConnectionEventHandler {

	void execute(ConnectionTask connectionTask);

}
