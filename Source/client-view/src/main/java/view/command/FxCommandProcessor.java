package view.command;

import javafx.application.Platform;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.command.CommandProcessor;
import root.command.CommandQueue;

public class FxCommandProcessor implements CommandProcessor {

	private CommandDrivenComponent command_target;

	public FxCommandProcessor(CommandDrivenComponent command_target) {

		this.command_target = command_target;

	}

	@Override
	public void execute(CommandQueue command_queue) {

		/*
		 * attention maybe whole while should be inside platform.runlater
		 * From runLater declatration:
		 * 
		 * NOTE: applications should avoid flooding JavaFX with too many
		 * pending Runnables. Otherwise, the application may become unresponsive.
		 * Applications are encouraged to batch up multiple operations into fewer
		 * runLater calls.
		 * 
		 */
		while (!command_queue.isEmpty()) {

			Command command = command_queue.dequeue();
			command.setTargetComponent(this.command_target);

			Platform.runLater((Runnable) command);

		}

	}

}
