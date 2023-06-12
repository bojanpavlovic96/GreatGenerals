package protocol;

import root.communication.messages.AbortAttackMsg;
import root.communication.messages.AbortDefenseMsg;
import root.communication.messages.AbortMoveMsg;
import root.communication.messages.AttackMsg;
import root.communication.messages.BuildUnitMsg;
import root.communication.messages.CreateRoomRequestMsg;
import root.communication.messages.DefendMsg;
import root.communication.messages.GameDoneMsg;
import root.communication.messages.IncomeTickMsg;
import root.communication.messages.InitializeMsg;
import root.communication.messages.RoomResponseMsg;
import root.communication.messages.StartGameRequestMsg;
import root.communication.messages.JoinRoomRequestMsg;
import root.communication.messages.LeaveGameMsg;
import root.communication.messages.Message;
import root.communication.messages.MessageType;
import root.communication.messages.MoveMsg;
import root.communication.messages.PointsUpdateMsg;
import root.communication.messages.ReadyForInitMsg;
import root.communication.messages.ReadyForReplayMsg;
import root.communication.messages.RemovePlayerMsg;
import root.communication.messages.ReplayMsg;

public class SwitchCaseTypeResolver implements NameTypeResolver {

	@Override
	public Class<?> resolve(MessageType type) {

		switch (type) {

			case InitializeMessage:
				return InitializeMsg.class;

			case MoveMessage:
				return MoveMsg.class;

			case CreateRoomRequest:
				return CreateRoomRequestMsg.class;

			case JoinRoomRequest:
				return JoinRoomRequestMsg.class;

			case RoomResponse:
				return RoomResponseMsg.class;

			case LeaveRoomRequest:
				return RoomResponseMsg.class;

			case StartGameRequest:
				return StartGameRequestMsg.class;

			case AttackMessage:
				return AttackMsg.class;

			case DefendMessage:
				return DefendMsg.class;

			case ReadyForInitMsg:
				return ReadyForInitMsg.class;

			case ReadyForReplayMsg:
				return ReadyForReplayMsg.class;

			case AbortMoveMessage:
				return AbortMoveMsg.class;

			case AbortAttackMessage:
				return AbortAttackMsg.class;

			case AbortDefenseMessage:
				return AbortDefenseMsg.class;

			case RecalculatePathMessage:
				break;

			case ServerErrorMessage:
				break;

			case IncomeTick:
				return IncomeTickMsg.class;

			case PointsUpdate:
				return PointsUpdateMsg.class;

			case BuildUnit:
				return BuildUnitMsg.class;

			case GameDone:
				return GameDoneMsg.class;

			case ReplayMessage:
				return ReplayMsg.class;

			case LeaveGame:
				return LeaveGameMsg.class;

			case RemovePlayer:
				return RemovePlayerMsg.class;

			default:
				break;

		}

		return Message.class;
	}

}
