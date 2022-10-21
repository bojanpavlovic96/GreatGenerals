package app.server;

import java.util.ArrayList;
import java.util.List;

import controller.command.CtrlInitializeCommand;
import controller.command.CtrlMoveCommand;
import model.PlayerModelData;
import model.event.MoveModelEventArg;
import root.command.Command;
import root.communication.MessageInterpreter;
import root.communication.messages.InitializeMsg;
import root.communication.messages.Message;
import root.communication.messages.MoveMsg;
import root.model.PlayerData;
import root.model.event.ModelEventArg;

public class MockupMsgInterpreter implements MessageInterpreter {

	public MockupMsgInterpreter() {
	}

	@Override
	public Command ToCommand(Message message) {
		if (message instanceof MoveMsg) {

			var moveCmd = (MoveMsg) message;
			return new CtrlMoveCommand(moveCmd.startFieldPos, moveCmd.endFieldPos);

		} else if (message instanceof InitializeMsg) {

			// this logic can be used in the actual translator implementation

			var initMsg = (InitializeMsg) message;

			var players = new ArrayList<PlayerData>();
			for (var player : initMsg.players) {
				var newModel = new PlayerModelData(player.getUsername(),
						player.getColor(),
						player.getLevel(),
						player.getPoints());

				players.add(newModel);
			}

			var fieldsDesc = new ArrayList<CtrlInitializeCommand.FieldDesc>();
			for (var msgField : initMsg.fields) {

				var playerData = findPlayerIn(msgField.owner.getUsername(), players);
				var unitDesc = new CtrlInitializeCommand.UnitDesc(
						msgField.unit.unitName,
						msgField.unit.moveType,
						msgField.unit.groundAttackType,
						msgField.unit.airAttackType);

				var terrainDesc = new CtrlInitializeCommand.TerrainDesc(
						msgField.terrain.name,
						msgField.terrain.intensity);

				var fieldDesc = new CtrlInitializeCommand.FieldDesc(
						msgField.isVisible,
						msgField.positions,
						playerData,
						unitDesc,
						terrainDesc);

				fieldsDesc.add(fieldDesc);
			}

			return new CtrlInitializeCommand(players, fieldsDesc);
		}
		// debug
		System.out.println("This message type is unknown to mockup msg translator ... ");
		return null;
	}

	@Override
	public Message ToMessage(ModelEventArg eventArg) {
		if (eventArg instanceof MoveModelEventArg) {
			var moveEvent = (MoveModelEventArg) eventArg;
			return new MoveMsg(moveEvent.sourceField, moveEvent.destinationField);
		}

		System.out.println("Failed to convert model event to message ... (it is not a move event ... :) )");
		return null;
	}

	private PlayerData findPlayerIn(String username, List<PlayerData> players) {
		return players.stream()
				.filter(player -> player.getUsername().equals(username))
				.findFirst()
				.get();
	}

}
