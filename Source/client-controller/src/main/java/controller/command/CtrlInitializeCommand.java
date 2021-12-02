package controller.command;

import java.util.List;

import javafx.geometry.Point2D;
import root.command.Command;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearViewCommand;
import view.command.LoadBoardCommand;

public class CtrlInitializeCommand extends Command {

	private String username;

	private List<PlayerData> players;
	private List<Field> fields;

	public CtrlInitializeCommand(List<PlayerData> players, List<Field> fields) {
		super("initialize-ctrl-command");

		this.players = players;
		this.fields = fields;
	}

	@Override
	public void run() {

		// ATTENTION
		// this implementation is hardcoded for testing purpose

		Model model = ((Controller) super.targetComponent).getModel();

		var options = ((Controller) super.targetComponent).getPossibleFieldOptions();

		for (Field field : this.fields) {

			for (FieldOption oldOption : options) {
				field.addFieldOption(oldOption.getCopy());
			}

			// field.addFieldOptions(((Controller)
			// super.target_component).getFieldOptions());

		}

		System.out.println("Calling initializeModel ... @ CtrlInitializeCommand.run");
		model.initializeModel(this.players, this.fields);

		model.getField(new Point2D(10, 10)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(4, 5)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(5, 5)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(5, 10)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(4, 7)).setUnit(model.generateUnit("basic-unit"));

		ClearViewCommand clear_command = new ClearViewCommand();
		LoadBoardCommand view_command = new LoadBoardCommand(this.fields);
		System.out.println("Load board command enqueue ... @ CtrlInitializeCommand.run");
		((Controller) super.targetComponent).getConsumerQueue().enqueue(clear_command);
		((Controller) super.targetComponent).getConsumerQueue().enqueue(view_command);

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
