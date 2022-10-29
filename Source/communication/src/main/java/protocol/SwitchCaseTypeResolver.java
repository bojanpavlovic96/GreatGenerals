package protocol;

import root.communication.messages.AttackMsg;
import root.communication.messages.CreateRoomRequestMsg;
import root.communication.messages.InitializeMsg;
import root.communication.messages.RoomResponseMsg;
import root.communication.messages.StartGameRequestMsg;
import root.communication.messages.JoinRoomRequestMsg;
import root.communication.messages.Message;
import root.communication.messages.MessageType;
import root.communication.messages.MoveMsg;
import root.communication.messages.ReadyForInitMsg;

public class SwitchCaseTypeResolver implements NameTypeResolver {

	@Override
	public Class<?> resolve(MessageType type) {

		switch (type) {

			case InitializeMsg:
				return InitializeMsg.class;

			case MoveMessage:
				return MoveMsg.class;

			case CreateRoomRequest:
				return CreateRoomRequestMsg.class;

			case JoinRoomRequest:
				return JoinRoomRequestMsg.class;

			case JoinResponse:
				return RoomResponseMsg.class;

			case LeaveRoomRequest:
				return RoomResponseMsg.class;

			case StartGameRequest:
				return StartGameRequestMsg.class;

			case AttackMessage:
				return AttackMsg.class;

			case ReadyForInitMsg:
				return ReadyForInitMsg.class;

		}

		return Message.class;
	}

}
