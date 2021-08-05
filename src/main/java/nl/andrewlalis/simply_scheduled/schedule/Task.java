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

	/**
	 * Constructs a new task that will run the given runnable according to the
	 * given schedule. Allows for specifying a {@link Clock}, this is mostly
	 * useful for testing purposes.
	 * @param clock The clock to use for time-sensitive operations.
	 * @param runnable The code to run when the task is executed.
	 * @param schedule The schedule which determines when the task is executed.
	 */
	public Task(Clock clock, Runnable runnable, Schedule schedule) {
		this.clock = clock;
		this.runnable = runnable;
		this.schedule = schedule;
	}

	/**
	 * Constructs a new task that will run the given runnable according to the
	 * given schedule.
	 * @param runnable The code to run when the task is executed.
	 * @param schedule The schedule which determines when the task is executed.
	 */
	public Task(Runnable runnable, Schedule schedule) {
		this(Clock.systemDefaultZone(), runnable, schedule);
	}

	/**
	 * @return The runnable which will be executed when this task is scheduled.
	 */
	public Runnable getRunnable() {
		return runnable;
	}

	/**
	 * @return The schedule for this task.
	 */
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
