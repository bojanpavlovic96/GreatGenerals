package controller.command;

import java.util.List;
import java.util.stream.Collectors;

import root.command.Command;
import root.communication.PlayerDescription;
import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.FieldDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.UnitDesc;
import root.controller.Controller;
import root.model.Model;
import view.command.ClearViewCommand;
import view.command.LoadBoardCommand;
import view.command.UpdateCoinsCommand;
import view.command.UpdatePointsCommand;

public class CtrlInitializeCommand extends Command {

	private List<PlayerDescription> players;
	private List<MoveDesc> moves;
	private List<UnitDesc> units;
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

		System.out.println(attacks.get(0).type);

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

		var ownerName = controller.getModel().getOwner().getUsername();
		var ownerData = players.stream()
				.filter((p) -> p.getUsername().equals(ownerName))
				.findFirst()
				.get();

		controller.getModel().getOwner().setCoins(ownerData.getCoins());

		System.out.println("InCtrlInit command owner balance: ");
		System.out.println("\tc: " + ownerData.getCoins());
		System.out.println("\tp: " + ownerData.getPoints());

		var updatePoints = new UpdatePointsCommand(ownerData.getPoints(), ownerData.getPoints());
		controller.getConsumerQueue().enqueue(updatePoints);

		var updateCoins = new UpdateCoinsCommand(ownerData.getCoins());
		controller.getConsumerQueue().enqueue(updateCoins);
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
