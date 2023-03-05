package view.command;

import java.util.List;

import root.view.View;
import root.command.Command;
import root.model.component.Field;

// this is so bad ... 
public class ClearBattleCommand extends Command {

	private List<Field> fields;

	public ClearBattleCommand(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public void run() {
		((View) targetComponent)
				.getCommandQueue()
				.enqueue(new ClearViewCommand());

		((View) targetComponent)
				.getCommandQueue()
				.enqueue(new LoadBoardCommand(fields));
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
