package root.communication.messages;

import java.util.Date;

public class ServerErrorMsg extends Message {

	public String message;

	public ServerErrorMsg(Date timestamp,String username, String roomName, String message) {
		super(MessageType.ServerErrorMessage, timestamp);

		super.setOrigin(username, roomName);

		this.message = message;
	}

}
