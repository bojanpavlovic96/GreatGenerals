package controller.command;

import java.util.List;
import java.util.stream.Collectors;

import model.component.field.ModelField;
import root.command.Command;
import root.communication.messages.components.Field;
import root.communication.messages.components.Terrain;
import root.communication.messages.components.Unit;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import view.command.ClearViewCommand;
import view.command.LoadBoardCommand;

public class CtrlInitializeCommand extends Command {

	private List<PlayerData> players;
	private List<Field> fields;

	public CtrlInitializeCommand(List<PlayerData> players, List<Field> fields) {

		this.players = players;
		this.fields = fields;
	}

	@Override
	public void run() {

		System.out.println("Calling initializeModel ... @ CtrlInitializeCommand.run");

		var controller = (Controller) super.targetComponent;
		var model = (Model) controller.getModel();

		var possibleOptions = controller.getPossibleFieldOptions();

		var modelFields = mapFields(this.fields);

		for (var field : modelFields) {
			for (var option : possibleOptions) {
				field.addFieldOption(option.getCopy());
			}
		}

		model.initializeModel(players, modelFields);

		// random units used for testing

		// model.getField(new Point2D(3, 1))
		// 		.setUnit(model.generateUnit(BasicUnit.unitName));

		// model.getField(new Point2D(10, 10))
		// 		.setUnit(model.generateUnit(BasicUnit.unitName));

		// model.getField(new Point2D(4, 5))
		// 		.setUnit(model.generateUnit(BasicUnit.unitName));

		// model.getField(new Point2D(5, 5))
		// 		.setUnit(model.generateUnit(BasicUnit.unitName));

		// model.getField(new Point2D(5, 10))
		// 		.setUnit(model.generateUnit(BasicUnit.unitName));

		// model.getField(new Point2D(4, 7))
		// 		.setUnit(model.generateUnit(BasicUnit.unitName));

		var clearCommand = new ClearViewCommand();
		var loadBoardCommand = new LoadBoardCommand(modelFields);

		System.out.println("Clear board command enqueued ... @ CtrlInitializeCommand.run");
		controller.getConsumerQueue().enqueue(clearCommand);

		System.out.println("Load board command enqueued ... @ CtrlInitializeCommand.run");
		controller.getConsumerQueue().enqueue(loadBoardCommand);

	}

	private List<root.model.component.Field> mapFields(List<Field> protFields) {
		return protFields.stream()
				.map((pt) -> mapField(pt))
				.collect(Collectors.toList());
	}

	private root.model.component.Field mapField(Field pField) {
		return new ModelField(pField.position,
				findPlayer(pField.owner),
				pField.isVisible,
				mapTerrain(pField.terrain));
	}

	private root.model.component.Terrain mapTerrain(Terrain pTerr) {
		return new root.model.component.Terrain(
				root.model.component.Terrain.TerrainType.valueOf(pTerr.type),
				pTerr.intensity);
	}

	private root.model.component.Unit mapUnit(Unit pUnit){
		return null;
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
