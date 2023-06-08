package app.form;

import app.resource_manager.Language;

public interface MessageDisplay {

	// Will stay until cleared or changed.
	public void showStatusMessage(Language.MessageType statusName);

	public String getCurrentStatusMessage();

	// Will dissapear after some period of time.
	public void showInfoMessage(Language.MessageType infoName);

	public String getCurrentInfoMessage();

}
