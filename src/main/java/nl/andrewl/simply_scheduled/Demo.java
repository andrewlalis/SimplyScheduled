package nl.andrewl.simply_scheduled;

import nl.andrewl.simply_scheduled.schedule.RepeatingSchedule;
import nl.andrewl.simply_scheduled.schedule.Schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Simple demonstration of scheduling functionality.
 */
public class Demo {
	public static void main(String[] args) {
		Scheduler scheduler = new BasicScheduler();
		Schedule schedule = new RepeatingSchedule(ChronoUnit.SECONDS, 5, Instant.now().truncatedTo(ChronoUnit.SECONDS));
		Runnable job = () -> System.out.println("Doing task: " + Instant.now().toString());
		scheduler.addTask(job, schedule);
		scheduler.start();
	}
}
