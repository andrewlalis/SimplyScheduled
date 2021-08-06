package nl.andrewl.simply_scheduled;

import nl.andrewl.simply_scheduled.schedule.Task;
import nl.andrewl.simply_scheduled.schedule.RepeatingSchedule;
import nl.andrewl.simply_scheduled.schedule.Schedule;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the {@link BasicScheduler} to reliably execute
 * scheduled tasks.
 */
public class BasicSchedulerTest {

	@Test
	void testSchedule() {
		Clock clock = Clock.fixed(Instant.now().truncatedTo(ChronoUnit.MINUTES), ZoneOffset.UTC);
		Scheduler scheduler = new BasicScheduler(clock, Executors.newSingleThreadExecutor());
		int secondsLeft = 4;
		AtomicBoolean flag = new AtomicBoolean(false);
		Runnable taskRunnable = () -> {
			flag.set(true);
			System.out.println("\tExecuted task.");
		};
		Task task = new Task(clock, taskRunnable, new RepeatingSchedule(ChronoUnit.SECONDS, 2, clock.instant().plusSeconds(1)));
		scheduler.addTask(task);
		scheduler.start();
		System.out.println("Now: " + clock.instant().toString());
		System.out.println("Next task execution: " + task.getSchedule().getNextExecutionTime(clock.instant()));
		System.out.printf("Waiting %d seconds for task to run...", secondsLeft);
		assertFalse(flag.get());
		try {
			Thread.sleep(secondsLeft * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(flag.get());
		scheduler.stop(true);
	}

	@Test
	void testRepeatingSchedule() throws InterruptedException {
		Scheduler scheduler = new BasicScheduler(Clock.systemUTC(), Executors.newFixedThreadPool(3));
		Instant startInstant = Instant.now(Clock.systemUTC());
		Schedule schedule = new RepeatingSchedule(ChronoUnit.SECONDS, 1, startInstant);
		AtomicInteger value = new AtomicInteger(0);
		Runnable taskRunnable = () -> {
			value.set(value.get() + 1);
			System.out.println("\tExecuted task at " + LocalDateTime.now());
		};
		scheduler.addTask(new Task(taskRunnable, schedule));
		assertEquals(0, value.get());
		scheduler.start();

		Thread.sleep(3500);
		// We expect the task to have run 4 times:
		// at t=0, t=1, t=2, and t=3.
		assertEquals(4, value.get());

		scheduler.stop(true);
	}
}
