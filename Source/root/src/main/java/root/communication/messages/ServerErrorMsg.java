package root.communication.messages;

public class ServerErrorMsg extends Message {

	public String message;

	public ServerErrorMsg(String username, String roomName, String message) {
		super(MessageType.ServerErrorMessage);

		super.setOrigin(username, roomName);

		this.message = message;
	}

}
