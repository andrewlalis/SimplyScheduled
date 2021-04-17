package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.RepeatingSchedule;
import nl.andrewlalis.simply_scheduled.schedule.Schedule;
import nl.andrewlalis.simply_scheduled.schedule.Task;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Demo {
	public static void main(String[] args) {
		Scheduler scheduler = new BasicScheduler();
		Schedule schedule = new RepeatingSchedule(ChronoUnit.MILLIS, 250);
		Runnable job = () -> System.out.println("Doing task: " + Instant.now().toString());
		scheduler.addTask(Task.of(job, schedule));
		scheduler.start();
	}
}
