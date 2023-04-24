package app.form;

import app.event.FormMessageHandler;

// userForm and roomForm implement this interface.
// When one of these has some message to be displayed
// it calls this handlers (set from initialPage),
// initial page has reference to messageDisplay so then 
// initial page calls method from messageDisplay 
public interface FormMessageProducer {

	void setStatusMessageHandler(FormMessageHandler hanlder);

	void setInfoMessageHandler(FormMessageHandler handler);

	// getters are kinda useless ... 
	FormMessageHandler getStatusMessageHandler();

	FormMessageHandler getInfoMessageHandler();

}