package controller.command;

import java.util.List;

import model.PlayerData;
import model.component.field.Field;
import view.command.LoadBoardCommand;

public class CtrlInitializeCommand extends CtrlCommand {

	private List<PlayerData> players;
	private List<Field> fields;

	/**
	 * @param primary_field - leave it null, it is just inherited from ctrlCommand
	 * 
	 */
	public CtrlInitializeCommand(Field primary_field, List<PlayerData> players, List<Field> fields) {
		super(primary_field);

		this.players = players;
		this.fields = fields;
	}

	@Override
	public void run() {

		System.out.println("Calling initalizeModel ... @ CtrlInitializeCommand.run");
		this.model.initializeModel(this.players, this.fields);

		LoadBoardCommand view_command = new LoadBoardCommand(this.fields);
		System.out.println("Load board command enqueue ... @ CtrlInitializeCommand.run");
		this.view_command_queue.enqueue(view_command);

	}

}
