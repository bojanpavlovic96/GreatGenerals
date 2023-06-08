package controller.command;

import root.command.Command;
import root.controller.Controller;
import view.command.UpdatePointsCommand;

public class CtrlPointsUpdateCommand extends Command {

	private String player;
	private int income; // NOTE this could be negative
	private int totalAmount;

	public CtrlPointsUpdateCommand(String player, int income, int totalAmount) {
		this.player = player;
		this.income = income;
		this.totalAmount = totalAmount;
	}

	@Override
	public void run() {

		var controller = (Controller) targetComponent;

		if (!controller.isOwner(player)) {
			return;
		}

		controller
				.getModel()
				.getOwner()
				.setCoins(totalAmount);

		var view = controller.getView();
		var viewCommand = new UpdatePointsCommand(income, totalAmount);
		view.getCommandQueue().enqueue(viewCommand);

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
