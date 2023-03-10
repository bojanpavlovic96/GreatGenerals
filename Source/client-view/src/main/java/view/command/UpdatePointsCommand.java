package view.command;

import root.command.Command;
import root.view.View;

public class UpdatePointsCommand extends Command {

	private int amount;

	public UpdatePointsCommand(int amount) {
		this.amount = amount;
	}

	@Override
	public void run() {
		var view = (View) targetComponent;
		view.showPoints(amount);
	}

	@Override
	public Command getAntiCommand() {
		return new UpdatePointsCommand(0);
	}

}
