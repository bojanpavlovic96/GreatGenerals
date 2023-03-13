package view.command;

import root.command.Command;
import root.view.View;

public class EndGameCommand extends Command {

	public String winner;
	private int amount;

	public EndGameCommand(String winner, int amount) {
		this.winner = winner;
		this.amount = amount;
	}

	@Override
	public void run() {

		var view = (View) targetComponent;

		view.showWinner(winner, amount);

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
