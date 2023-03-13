package root.model.event;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import root.ActiveComponent;

public class ConcurrentExecutorTimer implements Timer, ActiveComponent {

	private ScheduledExecutorService executor;

	public ConcurrentExecutorTimer(int threadCount) {

		this.executor = Executors.newScheduledThreadPool(threadCount);
		// remove task from waiting queue after it is cancelled
		((ScheduledThreadPoolExecutor) this.executor).setRemoveOnCancelPolicy(true);
		((ScheduledThreadPoolExecutor) this.executor).getActiveCount();
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable runnable, long msDelay, TimeUnit unit) {
		return executor.schedule(runnable, msDelay, unit);
	}

	@Override
	public void shutdown() {
		// attention this could possibly throw some exceptions if some tasks are still
		// running or waiting
		if (!executor.isShutdown()) {
			executor.shutdown();
		}
	}

	@Override
	public int getCount() {
		return ((ScheduledThreadPoolExecutor) this.executor).getQueue().size();
	}

}
