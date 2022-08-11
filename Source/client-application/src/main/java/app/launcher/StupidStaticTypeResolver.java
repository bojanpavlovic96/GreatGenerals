package app.launcher;

import protocol.NameTypeResolver;
import root.command.Command;
import root.communication.messages.CreateRoomRequestMsg;
import root.communication.messages.InitializeMsg;
import root.communication.messages.RoomResponseMsg;
import root.communication.messages.JoinRoomRequestMsg;
import root.communication.messages.MessageType;
import root.communication.messages.MoveMsg;

public class StupidStaticTypeResolver implements NameTypeResolver {

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

		}

		return Command.class;
	}

}
