package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.MinutelySchedule;
import nl.andrewlalis.simply_scheduled.schedule.RepeatingSchedule;
import nl.andrewlalis.simply_scheduled.schedule.Schedule;
import nl.andrewlalis.simply_scheduled.schedule.Task;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

	@Test
	void testSchedule() {
		Clock clock = Clock.fixed(Instant.now().truncatedTo(ChronoUnit.MINUTES), ZoneOffset.UTC);
		Scheduler scheduler = new BasicScheduler(clock);
		int secondsLeft = 4;
		AtomicBoolean flag = new AtomicBoolean(false);
		Runnable taskRunnable = () -> {
			flag.set(true);
			System.out.println("\tExecuted task.");
		};
		Task task = new Task(clock, taskRunnable, new MinutelySchedule(3));
		scheduler.addTask(task);
		scheduler.start();
		System.out.println("Now: " + clock.instant().toString());
		System.out.println("Next task execution: " + task.getSchedule().computeNextExecutionTime(clock.instant()));
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
	void testRepeatingSchedule() {
		Scheduler scheduler = new BasicScheduler();
		Schedule schedule = new RepeatingSchedule(ChronoUnit.SECONDS, 1);
		AtomicInteger value = new AtomicInteger(0);
		Runnable taskRunnable = () -> {
			value.set(value.get() + 1);
			System.out.println("\tExecuted task.");
		};
		Task task = new Task(taskRunnable, schedule);
		scheduler.addTask(task);
		assertEquals(0, value.get());
		scheduler.start();
		System.out.println("Waiting 3.5 seconds for 3 iterations.");
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(3, value.get());
		scheduler.stop(true);
	}
}
