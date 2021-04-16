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
	 * <p>
	 *     <strong>Note that certain implementations may introduce side-effects
	 *     when this method is called more than once.</strong>
	 * </p>
	 *
	 * @param referenceInstant The instant representing the current time.
	 * @return An instant in the future indicating the next time at which a task
	 * using this schedule should be executed.
	 */
	Instant computeNextExecutionTime(Instant referenceInstant);
}
