package controller.command;

import model.event.AttackModelEventArg;
import model.event.DefendModelEventArg;
import model.event.MoveModelEventArg;

import root.command.Command;
import root.communication.MessageInterpreter;
import root.communication.messages.AbortAttackMsg;
import root.communication.messages.AbortDefenseMsg;
import root.communication.messages.AbortMoveMsg;
import root.communication.messages.AttackMsg;
import root.communication.messages.DefendMsg;
import root.communication.messages.InitializeMsg;
import root.communication.messages.Message;
import root.communication.messages.MoveMsg;
import root.communication.messages.ReadyForInitMsg;
import root.communication.messages.RecalculatePathMsg;
import root.communication.messages.ServerErrorMsg;
import root.model.event.ModelEventArg;

public class SwitchCaseMsgInterpreter implements MessageInterpreter {

	@Override
	public Command ToCommand(Message message) {
		switch (message.type) {
			case CreateRoomRequest:
			case RoomResponse:
			case JoinRoomRequest:
			case LeaveRoomRequest:
			case StartGameRequest:
				System.out.println("SwitchCaseMsgInterpreter intended for game "
						+ "related messages translation received room/login related message ... ");
				System.out.println(message.type.toString());

				break;
			// Above types are used in roomServer and can not be translated to commands. 
			//TODO Consider spliting message types to login, room and game  related messages. 

			case InitializeMessage:
				return new CtrlInitializeCommand(
						((InitializeMsg) message).players,
						((InitializeMsg) message).moves,
						((InitializeMsg) message).units,
						((InitializeMsg) message).attacks,
						((InitializeMsg) message).fields);

			case MoveMessage:
				return new CtrlMoveCommand(
						((MoveMsg) message).startFieldPos,
						((MoveMsg) message).endFieldPos);

			case AttackMessage:
				return new CtrlAttackCommand(
						((AttackMsg) message).attackType,
						((AttackMsg) message).startFieldPos,
						((AttackMsg) message).endFieldPos);

			case DefendMessage:
				return new CtrlDefendCommand(
						((DefendMsg) message).defendType,
						((DefendMsg) message).startFieldPos,
						((DefendMsg) message).endFieldPos);

			case RecalculatePathMessage:
				return new CtrlRecalculatePathCommand(
						((RecalculatePathMsg) message).unitPosition);

			case AbortMoveMessage:
				return new CtrlAbortMoveCommand(
						((AbortMoveMsg) message).unitPosition);

			case AbortAttackMessage:
				return new CtrlAbortAttackCommand(
						((AbortAttackMsg) message).unitPosition);

			case AbortDefenseMessage:
				return new CtrlAbortDefenseCommand(
						((AbortDefenseMsg) message).unitPosition);

			case ServerErrorMessage:
				return new CtrlServerErrorMessage(
						((ServerErrorMsg) message).message);

			default:
				break;

		}

		return null;
	}

	// endregion

	@Override
	public Message ToMessage(ModelEventArg eventArg) {
		switch (eventArg.getEventType()) {
			case Attack:
				return new AttackMsg(
						((AttackModelEventArg) eventArg).attackType,
						((AttackModelEventArg) eventArg).getSourceField(),
						((AttackModelEventArg) eventArg).getDestinationField());

			case Defend:
				return new DefendMsg(
						((DefendModelEventArg) eventArg).defenseType,
						((DefendModelEventArg) eventArg).sourceField,
						((DefendModelEventArg) eventArg).destinationField);

			case Move:
				return new MoveMsg(
						((MoveModelEventArg) eventArg).getSourceField(),
						((MoveModelEventArg) eventArg).getDestinationField());

			case ReadyForInit:
				// username and roomName are already present in the Message super-class
				// and they are set with .setOrigin method inside the server proxy 
				return new ReadyForInitMsg();

			default:
				break;

		}
		return null;
	}

}