package app.form;

import app.event.FormMessageHandler;

public interface FormMessageProducer {

	void setStatusMessageHandler(FormMessageHandler hanlder);

	void setInfoMessageHandler(FormMessageHandler handler);

	FormMessageHandler getStatusMessageHandler();

	FormMessageHandler getInfoMessageHanlder();

}