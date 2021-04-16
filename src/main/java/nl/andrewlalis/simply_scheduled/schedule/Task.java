package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Clock;
import java.time.Instant;

/**
 * A task consists of a runnable job, and a schedule that defines precisely when
 * the job will be run. It implements comparable so that schedulers may use a
 * priority queue to insert new tasks.
 */
public class Task implements Comparable<Task>{
	private final Clock clock;
	private final Runnable runnable;
	private final Schedule schedule;

	public Task(Clock clock, Runnable runnable, Schedule schedule) {
		this.clock = clock;
		this.runnable = runnable;
		this.schedule = schedule;
	}

	public Task(Runnable runnable, Schedule schedule) {
		this(Clock.systemDefaultZone(), runnable, schedule);
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	@Override
	public int compareTo(Task o) {
		Instant now = clock.instant();
		return this.schedule.getNextExecutionTime(now).compareTo(o.getSchedule().getNextExecutionTime(now));
	}
}
