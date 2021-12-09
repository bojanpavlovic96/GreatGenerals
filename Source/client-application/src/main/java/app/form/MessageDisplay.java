package app.form;

import app.resource_manager.Language;

public interface MessageDisplay {

	public void showStatusMessage(Language.MessageType statusName);

	public String getCurrentStatusMessage();

	public void showInfoMessage(Language.MessageType infoName);

	public String getCurrentInfoMessage();

}
