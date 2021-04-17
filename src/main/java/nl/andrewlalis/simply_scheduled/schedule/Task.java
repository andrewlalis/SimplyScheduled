package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

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

	public static Task of(Runnable runnable, Schedule schedule) {
		return new Task(Clock.systemDefaultZone(), runnable, schedule);
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
		Optional<Instant> t1 = this.schedule.getNextExecutionTime(now);
		Optional<Instant> t2 = o.getSchedule().getNextExecutionTime(now);

		if (t1.isEmpty() && t2.isEmpty()) return 0;
		if (t1.isPresent() && t2.isEmpty()) return 1;
		if (t1.isEmpty()) return -1;

		return t1.get().compareTo(t2.get());
	}
}
