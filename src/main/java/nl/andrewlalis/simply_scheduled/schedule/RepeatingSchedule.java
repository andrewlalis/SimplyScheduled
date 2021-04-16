package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A schedule which repeatedly executes a task at a regular interval specified
 * as some integer multiple of a unit of time, such as 5 seconds, or 2 minutes.
 */
public class RepeatingSchedule implements Schedule {
	private final ChronoUnit unit;
	private final long multiple;
	private Instant lastExecution;

	/**
	 * Constructs a new repeating schedule.
	 * @param unit The unit of time that the interval consists of.
	 * @param multiple The
	 */
	public RepeatingSchedule(ChronoUnit unit, long multiple) {
		this.unit = unit;
		this.multiple = multiple;
		this.lastExecution = null;
	}

	/**
	 * Computes the next execution time for a task. This keeps track of the last
	 * execution time, so that tasks repeat at an exact interval.
	 * @param referenceInstant The instant representing the current time.
	 * @return The next instant to execute the task at.
	 */
	@Override
	public Instant computeNextExecutionTime(Instant referenceInstant) {
		if (this.lastExecution == null) {
			this.lastExecution = referenceInstant;
		}
		Instant nextExecution = this.lastExecution.plus(multiple, unit);
		this.lastExecution = nextExecution;
		return nextExecution;
	}
}
