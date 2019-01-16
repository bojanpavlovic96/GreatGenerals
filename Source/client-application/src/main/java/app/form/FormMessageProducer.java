package app.form;

public interface FormMessageProducer {

	void setStatusMessageHandler(FormMessageHandler hanlder);

	void setInfoMessageHandler(FormMessageHandler handler);

	FormMessageHandler getStatusMessageHandler();

	FormMessageHandler getInfoMessageHanlder();

}
