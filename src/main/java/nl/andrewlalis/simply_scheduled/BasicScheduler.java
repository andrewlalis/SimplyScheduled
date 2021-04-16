package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.Task;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A simple thread-based scheduler that sleeps until the next task, runs it
 * using a work-stealing executor thread pool, and continues with the next task.
 */
public class BasicScheduler extends Thread implements Scheduler {
	private final Clock clock;
	private final PriorityBlockingQueue<Task> tasks;
	private final ExecutorService executorService;
	private boolean running = false;

	public BasicScheduler(Clock clock) {
		this.clock = clock;
		this.tasks = new PriorityBlockingQueue<>();
		this.executorService = Executors.newWorkStealingPool();
	}

	public BasicScheduler() {
		this(Clock.systemDefaultZone());
	}

	@Override
	public void addTask(Task task) {
		this.tasks.add(task);
	}

	@Override
	public void run() {
		this.running = true;
		while (this.running) {
			try {
				Task nextTask = this.tasks.take();
				Instant now = this.clock.instant();
				long waitTime = nextTask.getSchedule().getNextExecutionTime(now).toEpochMilli() - now.toEpochMilli();
				if (waitTime > 0) {
					Thread.sleep(waitTime);
				}
				this.executorService.execute(nextTask.getRunnable());
				nextTask.getSchedule().markExecuted(this.clock.instant());
				this.tasks.put(nextTask); // Put the task back in the queue.
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
