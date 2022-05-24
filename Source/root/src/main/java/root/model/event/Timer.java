package root.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// not finished 
// not worth it, many recommended to just use concurrent.Executors ... 
// it kinda works ... 
public class Timer {

	class RunnableWithFuture {
		public Runnable runnable;
		public ScheduledFuture future;

		public RunnableWithFuture(Runnable runnable, ScheduledFuture future) {
			this.runnable = runnable;
			this.future = future;
		}

	}

	class RunnableWithDoneCallback implements Runnable {
		public Runnable mainTask;
		public Runnable onDone;

		public RunnableWithDoneCallback(Runnable mainTask, Runnable onDone) {
			this.mainTask = mainTask;
			this.onDone = onDone;
		}

		@Override
		public void run() {
			if (mainTask != null) {
				mainTask.run();
			}

			if (onDone != null) {
				onDone.run();
			}
		}

	}

	private ScheduledExecutorService executor;

	private List<Timer.RunnableWithFuture> runnables;

	public Timer() {
		this.executor = Executors.newScheduledThreadPool(3);
		((ScheduledThreadPoolExecutor) this.executor).setRemoveOnCancelPolicy(true);

		this.runnables = new ArrayList<RunnableWithFuture>();
	}

	public void schedule(Runnable eventHandler, long msDelay) {
		var future = this.executor.schedule(eventHandler, msDelay, TimeUnit.MILLISECONDS);
		this.runnables.add(new RunnableWithFuture(eventHandler, future));
		((ScheduledThreadPoolExecutor) this.executor).remove(eventHandler);
	}

	public void cancel(Runnable targetHandler) {
		for (int i = 0; i < this.runnables.size(); i++) {
			var current = runnables.get(i);
			if (current == targetHandler) {
				current.future.cancel(false);
				// false -> if handler execution already started nothing will happen

				this.runnables.remove(i);
				break;
			}
		}
	}

}
