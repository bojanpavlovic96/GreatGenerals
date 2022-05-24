package controller.command;

import java.util.ArrayList;
import java.util.List;

import model.component.field.ModelField;
import model.component.unit.BasicUnit;
import root.Point2D;
import root.command.Command;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
import view.command.ClearViewCommand;
import view.command.LoadBoardCommand;

public class CtrlInitializeCommand extends Command {

	public static class FieldDesc {
		public boolean isVisible;
		public Point2D storagePosition;
		public PlayerData player;
		public UnitDesc unit;
		public TerrainDesc terrain;

		public FieldDesc(boolean isVisible,
				Point2D storagePosition,
				PlayerData player,
				UnitDesc unit,
				TerrainDesc terrain) {

			this.isVisible = isVisible;
			this.storagePosition = storagePosition;
			this.player = player;
			this.unit = unit;
			this.terrain = terrain;
		}

	}

	public static class UnitDesc {
		public String name;
		public String moveType;
		public String groundAttackType;
		public String airAttackType;

		public UnitDesc(String name,
				String moveType,
				String groundAttackType,
				String airAttackType) {

			this.name = name;
			this.moveType = moveType;
			this.groundAttackType = groundAttackType;
			this.airAttackType = airAttackType;
		}

	}

	public static class TerrainDesc {
		public String name;
		public int intensity;

		public TerrainDesc(String name, int intensity) {
			this.name = name;
			this.intensity = intensity;
		}

	}

	public static final String name = "initialize-ctrl-command";

	private List<PlayerData> players;
	private List<FieldDesc> fieldDescs;
	public List<Field> fields;

	public CtrlInitializeCommand(List<PlayerData> players, List<FieldDesc> fields) {
		super(CtrlInitializeCommand.name);

		this.players = players;
		this.fieldDescs = fields;
	}

	@Override
	public void run() {

		System.out.println("Calling initializeModel ... @ CtrlInitializeCommand.run");

		var controller = (Controller) super.targetComponent;
		var model = (Model) controller.getModel();

		if (this.fields == null) {
			this.fields = translateDescriptions(this.fieldDescs);
		}

		var possibleOptions = controller.getPossibleFieldOptions();
		for (var field : this.fields) {
			for (var option : possibleOptions) {
				field.addFieldOption(option.getCopy());
			}
		}

		model.initializeModel(players, this.fields);

		// random units used for testing

		model.getField(new Point2D(3, 1))
				.setUnit(model.generateUnit(BasicUnit.unitName));

		model.getField(new Point2D(10, 10))
				.setUnit(model.generateUnit(BasicUnit.unitName));

		model.getField(new Point2D(4, 5))
				.setUnit(model.generateUnit(BasicUnit.unitName));

		model.getField(new Point2D(5, 5))
				.setUnit(model.generateUnit(BasicUnit.unitName));

		model.getField(new Point2D(5, 10))
				.setUnit(model.generateUnit(BasicUnit.unitName));

		model.getField(new Point2D(4, 7))
				.setUnit(model.generateUnit(BasicUnit.unitName));

		var clearCommand = new ClearViewCommand();
		var loadBoardCommand = new LoadBoardCommand(fields);

		System.out.println("Load board command enqueued ... @ CtrlInitializeCommand.run");
		controller.getConsumerQueue().enqueue(clearCommand);
		controller.getConsumerQueue().enqueue(loadBoardCommand);

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

	public List<Field> translateDescriptions(List<FieldDesc> descs) {

		var controller = (Controller) super.targetComponent;
		var model = (Model) controller.getModel();

		// var possibleOptions = controller.getPossibleFieldOptions();

		var fields = new ArrayList<Field>();
		for (FieldDesc fieldDesc : this.fieldDescs) {

			var terrain = new Terrain(fieldDesc.terrain.name, fieldDesc.terrain.intensity);

			var field = new ModelField(fieldDesc.storagePosition,
					fieldDesc.player,
					fieldDesc.isVisible,
					terrain);

			// for (FieldOption oldOption : possibleOptions) {
			// 	field.addFieldOption(oldOption.getCopy());
			// }

			if (fieldDesc.unit != null) {
				var unit = model.generateUnit(fieldDesc.unit.name);
				field.setUnit(unit);
			}

			fields.add(field);
		}

		return fields;
	}

}
