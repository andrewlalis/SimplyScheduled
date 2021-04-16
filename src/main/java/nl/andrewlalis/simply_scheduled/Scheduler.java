package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.Task;

/**
 * A scheduler is responsible for storing and executing tasks as defined by each
 * task's schedule.
 */
public interface Scheduler {
	/**
	 * Adds a task to this scheduler, so that when the scheduler starts, the
	 * task is executed in accordance with its defined schedule.
	 * @param task The task to add.
	 */
	void addTask(Task task);

	/**
	 * Starts the scheduler. A scheduler should only execute tasks once it has
	 * started, and it is up to the implementation to determine whether new
	 * tasks may be added while the scheduler is running.
	 */
	void start();

	/**
	 * Stops the scheduler.
	 * @param force Whether to forcibly stop the scheduler. When set to true,
	 *              any currently-executing tasks are immediately shutdown.
	 */
	void stop(boolean force);
}
