package root.communication.messages;

import java.util.List;

import root.communication.PlayerDescription;
import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.FieldDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.UnitDesc;

public class InitializeMsg extends Message {

	public List<PlayerDescription> players;
	public List<MoveDesc> moves;
	public List<UnitDesc> units;
	public List<AttackDesc> attacks;
	public List<FieldDesc> fields;

	public InitializeMsg() {
		super(MessageType.InitializeMessage);
	}

}
