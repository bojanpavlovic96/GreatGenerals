package app.form;

import app.event.FormMessageHandler;

// TODO this should be removed maybe, i don't understand why this exists

public interface FormMessageProducer {

	void setStatusMessageHandler(FormMessageHandler hanlder);

	void setInfoMessageHandler(FormMessageHandler handler);

	FormMessageHandler getStatusMessageHandler();

	FormMessageHandler getInfoMessageHanlder();

}