package view.command;

import javafx.application.Platform;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.command.CommandQueue;

public class FxQueue implements CommandQueue {

	private CommandDrivenComponent target;

	public FxQueue(CommandDrivenComponent target) {
		this.target = target;
	}

	@Override
	public void enqueue(Command newCmd) {
		newCmd.setTargetComponent(target);
		Platform.runLater(newCmd);
	}

	@Override
	public CommandDrivenComponent getTarget() {
		return this.target;
	}

}
