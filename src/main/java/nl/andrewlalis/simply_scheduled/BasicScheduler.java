package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.Schedule;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BasicScheduler implements Scheduler {
	private ScheduledExecutorService executorService;
	private final Clock clock;

	public BasicScheduler(Clock clock) {
		this.clock = clock;
		this.executorService = new ScheduledThreadPoolExecutor(1);
	}

	public BasicScheduler() {
		this(Clock.systemDefaultZone());
	}

	@Override
	public void addTask(Runnable task, Schedule schedule) {
		Instant nextExecution = schedule.getNextExecutionTime(this.clock.instant());
		long diff = nextExecution.toEpochMilli() - System.currentTimeMillis();
		if (diff < 1) return; // Exit immediately, if the next scheduled execution is in the past.
		this.executorService.schedule(task, diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop(boolean force) {
		if (this.executorService != null) {
			if (force) {
				this.executorService.shutdownNow();
			} else {
				this.executorService.shutdown();
			}
		}
	}
}
