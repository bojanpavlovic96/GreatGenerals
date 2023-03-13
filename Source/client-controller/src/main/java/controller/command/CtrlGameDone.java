package controller.command;

import root.command.Command;
import root.controller.Controller;
import view.command.EndGameCommand;

public class CtrlGameDone extends Command {

	private String winner;
	private int bonusAmount;

	public CtrlGameDone(String winner, int bonusAmount) {
		this.winner = winner;
		this.bonusAmount = bonusAmount;
	}

	@Override
	public void run() {
		var controller = (Controller) targetComponent;
		var view = controller.getView();

		Command showNotif = null;
		if (controller.isOwner(winner)) {
			showNotif = new EndGameCommand(winner, bonusAmount);
		} else {
			showNotif = new EndGameCommand(winner, 0);
		}

		view.getCommandQueue().enqueue(showNotif);
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
