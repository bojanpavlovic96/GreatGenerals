package view.command;

import root.command.Command;
import root.view.View;

public class UpdateCoinsCommand extends Command {

	private int amount;

	public UpdateCoinsCommand(int amount) {
		this.amount = amount;
	}

	@Override
	public void run() {
		System.out.println("Updating coins: " + amount);
		var view = (View) targetComponent;
		view.showCoins(amount);
	}

	@Override
	public Command getAntiCommand() {
		return new UpdateCoinsCommand(0);
	}

}
