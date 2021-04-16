package nl.andrewlalis.simply_scheduled.schedule;

import nl.andrewlalis.simply_scheduled.Scheduler;

import java.time.Instant;

/**
 * A schedule is used by a {@link Scheduler} to determine how long to wait for
 * the next time that a task should be executed.
 */
public interface Schedule {
	/**
	 * Given some instant referring to the current time, this method should
	 * produce an instant sometime in the future at which the next execution of
	 * a task should happen.
	 *
	 * @param referenceInstant The instant representing the current time.
	 * @return An instant in the future indicating the next time at which a task
	 * using this schedule should be executed.
	 */
	Instant getNextExecutionTime(Instant referenceInstant);

	/**
	 * This method is called on the schedule as an indication that the scheduler
	 * should move on to planning the next execution time.
	 * @param instant The instant at which the schedule's task(s) were executed.
	 */
	default void markExecuted(Instant instant) {
		// Default no-op.
	}
}
