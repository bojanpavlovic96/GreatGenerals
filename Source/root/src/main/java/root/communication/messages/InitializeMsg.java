package root.communication.messages;

import java.util.List;

import root.communication.messages.components.Field;
import root.communication.messages.components.PlayerData;

public class InitializeMsg extends Message {

	public String username;

	public List<PlayerData> players;
	public List<Field> fields;

	public InitializeMsg() {
		super(MessageType.InitializeMsg);

	}

}
