package controller.command;

import java.util.List;

import javafx.geometry.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import view.command.LoadBoardCommand;

public class CtrlInitializeCommand extends Command {

	private List<PlayerData> players;
	private List<Field> fields;

	public CtrlInitializeCommand(List<PlayerData> players, List<Field> fields) {
		super("initialize-ctrl-command");

		this.players = players;
		this.fields = fields;
	}

	@Override
	public void run() {

		System.out.println("Calling initalizeModel ... @ CtrlInitializeCommand.run");
		((Model) super.target_component).initializeModel(this.players, this.fields);

		Model model = ((Controller) super.target_component).getModel();

		model.getField(new Point2D(10, 10)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(4, 5)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(5, 5)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(5, 10)).setUnit(model.generateUnit("basic-unit"));
		model.getField(new Point2D(4, 7)).setUnit(model.generateUnit("basic-unit"));

		LoadBoardCommand view_command = new LoadBoardCommand(this.fields);
		System.out.println("Load board command enqueue ... @ CtrlInitializeCommand.run");
		((Controller) super.target_component).getCommandConsumer().enqueue(view_command);

	}

}
