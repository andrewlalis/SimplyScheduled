package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.Task;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * A simple thread-based scheduler that sleeps until the next task, runs it
 * using a work-stealing executor thread pool, and continues with the next task.
 */
public class BasicScheduler extends Thread implements Scheduler {
	private final Clock clock;
	private final PriorityBlockingQueue<Task> tasks;
	private final ExecutorService executorService;
	private boolean running = false;

	public BasicScheduler(Clock clock, ExecutorService executorService) {
		this.clock = clock;
		this.tasks = new PriorityBlockingQueue<>();
		this.executorService = executorService;
	}

	public BasicScheduler() {
		this(Clock.systemDefaultZone(), Executors.newWorkStealingPool());
	}

	/**
	 * Adds a task to this scheduler's queue.
	 * @param task The task to add.
	 * @throws RuntimeException If a task is added while the scheduler is running.
	 */
	@Override
	public void addTask(Task task) {
		if (this.running) {
			throw new RuntimeException("Cannot add tasks to the basic scheduler while it is running.");
		}
		this.tasks.add(task);
	}

	@Override
	public void run() {
		this.running = true;
		while (this.running) {
			try {
				final Task nextTask = this.tasks.take();
				final Instant now = this.clock.instant();
				final Optional<Instant> optionalNextExecution = nextTask.getSchedule().getNextExecutionTime(now);
				if (optionalNextExecution.isEmpty()) {
					continue; // Skip if the schedule doesn't have a next execution planned.
				}
				long waitTime = optionalNextExecution.get().toEpochMilli() - now.toEpochMilli();
				if (waitTime > 0) {
					Thread.sleep(waitTime);
				}
				try {
					this.executorService.execute(nextTask.getRunnable());
				} catch (RejectedExecutionException e) {
					if (!this.executorService.isShutdown()) {
						// Only show the stack trace if the executor service is not being shut down.
						// We expect the service to reject executions if it is shutting down.
						e.printStackTrace();
					}
				}
				nextTask.getSchedule().markExecuted(this.clock.instant());
				if (nextTask.getSchedule().isRepeating()) {
					this.tasks.put(nextTask);
				}
			} catch (InterruptedException e) {
				this.setRunning(false);
			}
		}
	}

	@Override
	public void stop(boolean force) {
		this.setRunning(false);
		if (force) {
			this.executorService.shutdownNow();
		} else {
			this.executorService.shutdown();
		}
	}

	private synchronized void setRunning(boolean running) {
		this.running = running;
	}
}
