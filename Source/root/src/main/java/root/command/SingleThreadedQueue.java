package root.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import root.ActiveComponent;

public class SingleThreadedQueue implements CommandQueue, ActiveComponent {

	private ExecutorService executor;
	private CommandDrivenComponent target;

	public SingleThreadedQueue(CommandDrivenComponent target) {
		executor = Executors.newSingleThreadExecutor();
		this.target = target;
	}

	@Override
	public void enqueue(Command newCmd) {
		newCmd.setTargetComponent(target);
		executor.execute(newCmd);
	}

	@Override
	public void shutdown() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdown();
		}
	}

	@Override
	public CommandDrivenComponent getTarget() {
		return this.target;
	}

}
