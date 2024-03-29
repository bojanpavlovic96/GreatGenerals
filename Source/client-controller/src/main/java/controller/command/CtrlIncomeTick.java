package controller.command;

import root.command.Command;
import root.controller.Controller;
import view.command.UpdateCoinsCommand;

public class CtrlIncomeTick extends Command {

	public String player;
	public int amount;

	public CtrlIncomeTick(String player, int amount) {
		this.player = player;
		this.amount = amount;
	}

	@Override
	public void run() {

		var controller = (Controller) targetComponent;

		if (!controller.isOwner(player)) {
			// System.out.println("Received payment for: " + player + " ... (not for me)");
			return;
		}

		System.out.println("Income tick ... : " + amount);
		controller
				.getModel()
				.getOwner()
				.setCoins(amount);

		var updateCommand = new UpdateCoinsCommand(amount);
		controller
				.getView()
				.getCommandQueue()
				.enqueue(updateCommand);

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
