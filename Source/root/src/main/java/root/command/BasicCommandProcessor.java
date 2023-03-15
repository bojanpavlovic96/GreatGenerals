package root.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import root.ActiveComponent;

public class BasicCommandProcessor implements CommandProcessor, ActiveComponent {

	private ExecutorService executor = null;
	private CommandDrivenComponent commandTarget;

	public BasicCommandProcessor(CommandDrivenComponent commandTarget) {
		this.executor = Executors.newSingleThreadExecutor();
		this.commandTarget = commandTarget;
	}

	public void execute(CommandQueue commandQueue) {

		while (!commandQueue.isEmpty()) {
			Command command = commandQueue.dequeue();
			command.setTargetComponent(this.commandTarget);

			this.executor.execute(command);
		}

	}

	@Override
	public void shutdown() {
		if (this.executor != null && !this.executor.isShutdown()) {
			this.executor.shutdown();
		}
	}

}
