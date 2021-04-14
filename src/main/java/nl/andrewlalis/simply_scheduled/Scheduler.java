package nl.andrewlalis.simply_scheduled;

import nl.andrewlalis.simply_scheduled.schedule.Schedule;

public interface Scheduler {
	void addTask(Runnable task, Schedule schedule);
	void start();
	void stop(boolean force);
}
