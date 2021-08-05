package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.Schedule;
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
	 * Adds a task to this scheduler.
	 * @param runnable The code to run.
	 * @param schedule The schedule that dictates when the code should run.
	 */
	default void addTask(Runnable runnable, Schedule schedule) {
		addTask(new Task(runnable, schedule));
	}

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

	/**
	 * Stops the scheduler, and waits for any currently-executing tasks to finish.
	 */
	default void stop() {
		stop(false);
	}
}
