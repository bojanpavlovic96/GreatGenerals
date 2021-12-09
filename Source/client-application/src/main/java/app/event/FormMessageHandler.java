package app.event;

import app.resource_manager.Language;

public interface FormMessageHandler {

	void execute(Language.MessageType messageType);

}
