package root.communication.messages;

import java.util.List;

import root.communication.messages.components.Field;
import root.communication.messages.components.PlayerData;

public class InitializeCmdMsg extends Message {

	public static final String name = "initialize-cmd-msg";

	public String username;

	public List<PlayerData> players;
	public List<Field> fields;

	public InitializeCmdMsg() {
		super(InitializeCmdMsg.name);

	}

}
