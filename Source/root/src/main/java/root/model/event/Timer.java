package root.model.event;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Timer {

	ScheduledFuture<?> schedule(Runnable runnable, long msDelay, TimeUnit unit);

	int getCount(); // just for debbug purpose 

}
