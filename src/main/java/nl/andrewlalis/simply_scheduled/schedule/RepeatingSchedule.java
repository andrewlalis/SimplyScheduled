package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * A schedule which repeatedly executes a task at a regular interval specified
 * as some integer multiple of a unit of time, such as 5 seconds, or 2 minutes.
 */
public class RepeatingSchedule implements Schedule {
	private final ChronoUnit unit;
	private final long multiple;
	private long elapsedIntervals = 0;
	private final Instant start;

	/**
	 * Constructs a new repeating schedule.
	 * @param unit The unit of time that the interval consists of.
	 * @param multiple The number of units of time that each interval consists of.
	 * @param start The starting point for this schedule.
	 */
	public RepeatingSchedule(ChronoUnit unit, long multiple, Instant start) {
		this.unit = unit;
		this.multiple = multiple;
		this.start = start;
	}

	/**
	 * Constructs a new repeating schedule, using {@link Instant#now()} as the
	 * starting point.
	 * @param unit The unit of time that the interval consists of.
	 * @param multiple The number of units of time that each interval consists of.
	 */
	public RepeatingSchedule(ChronoUnit unit, long multiple) {
		this(unit, multiple, Instant.now());
	}

	/**
	 * Computes the next execution time for a task. This keeps track of the last
	 * execution time, so that tasks repeat at an exact interval.
	 * @param referenceInstant The instant representing the current time.
	 * @return The next instant to execute the task at.
	 */
	@Override
	public Optional<Instant> getNextExecutionTime(Instant referenceInstant) {
		return Optional.of(this.start.plus(elapsedIntervals * multiple, unit));
	}

	@Override
	public void markExecuted(Instant instant) {
		this.elapsedIntervals++;
	}
}
