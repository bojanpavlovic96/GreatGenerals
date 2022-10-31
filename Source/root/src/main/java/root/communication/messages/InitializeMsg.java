package root.communication.messages;

import java.util.List;

import root.communication.PlayerDescription;
import root.communication.messages.components.Field;

public class InitializeMsg extends Message {

	public List<PlayerDescription> players;
	public List<Field> fields;

	public InitializeMsg() {
		super(MessageType.InitializeMessage);

	}

}
