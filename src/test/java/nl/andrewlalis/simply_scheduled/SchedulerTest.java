package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.HourlySchedule;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SchedulerTest {

	@Test
	void testSchedule() {
		Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
		Scheduler scheduler = new BasicScheduler(clock);
		LocalTime time = LocalTime.now();
		int secondsLeft = 60 - time.getSecond() + 1;
		AtomicBoolean flag = new AtomicBoolean(false);
		scheduler.addTask(() -> flag.set(true), new HourlySchedule(time.getMinute() + 1));
		scheduler.start();
		System.out.printf("Waiting %d seconds for task to run...", secondsLeft);
		try {
			Thread.sleep(secondsLeft * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(flag.get());
	}
}
