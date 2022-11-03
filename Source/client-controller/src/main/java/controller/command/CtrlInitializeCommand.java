package controller.command;

import java.util.List;
import java.util.stream.Collectors;

import model.component.field.HexagonField;
import root.command.Command;
import root.communication.PlayerDescription;
import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.FieldDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.TerrainDesc;
import root.communication.messages.components.UnitDesc;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import view.command.ClearViewCommand;
import view.command.LoadBoardCommand;

public class CtrlInitializeCommand extends Command {

	private List<PlayerDescription> players;
	private List<UnitDesc> units;
	private List<MoveDesc> moves;
	private List<AttackDesc> attacks;
	private List<FieldDesc> fields;

	public CtrlInitializeCommand(List<PlayerDescription> pds,
			List<MoveDesc> moves,
			List<UnitDesc> units,
			List<AttackDesc> attacks,
			List<FieldDesc> fields) {

		this.players = pds;
		this.moves = moves;
		this.units = units;
		this.attacks = attacks;
		this.fields = fields;

	}

	@Override
	public void run() {

		System.out.println("Calling initializeModel ... @ CtrlInitializeCommand.run");

		var controller = (Controller) super.targetComponent;
		var model = (Model) controller.getModel();

		model.initializeModel(players,
				units,
				moves,
				attacks,
				fields,
				controller.getPossibleFieldOptions());

		System.out.println("Clear board command enqueued ... @ CtrlInitializeCommand.run");
		var clearCommand = new ClearViewCommand();
		controller.getConsumerQueue().enqueue(clearCommand);

		System.out.println("Load board command enqueued ... @ CtrlInitializeCommand.run");
		var loadBoardCommand = new LoadBoardCommand(model.getFields());
		controller.getConsumerQueue().enqueue(loadBoardCommand);

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

	private PlayerData findPlayer(String name) {
		return players.stream()
				.filter((p) -> p.getUsername().equals(name))
				.findFirst()
				.get();
	}

}
