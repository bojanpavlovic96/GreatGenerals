package app.form;

public interface MessageDisplay {

	public void showStatusMessage(String message_name);

	public String getCurrentStatusMessage();

	public void showInfoMessage(String message_name);

	public String getCurrentInfoMessage();

}
