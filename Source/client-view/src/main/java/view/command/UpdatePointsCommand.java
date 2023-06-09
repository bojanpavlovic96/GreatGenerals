package view.command;

import root.command.Command;
import root.view.View;

public class UpdatePointsCommand extends Command {

	private int income;
	private int total;

	public UpdatePointsCommand(int income, int total) {
		this.income = income;
		this.total = total;
	}

	@Override
	public void run() {
		var view = (View) targetComponent;
		view.updatePoints(income, total);
	}

	@Override
	public Command getAntiCommand() {
		return new UpdatePointsCommand(0, 0);
	}

}
