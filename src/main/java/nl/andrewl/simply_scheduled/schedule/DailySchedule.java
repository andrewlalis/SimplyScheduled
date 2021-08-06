package nl.andrewl.simply_scheduled.schedule;

import java.time.*;
import java.util.Optional;

/**
 * A daily schedule plans for the execution of a task once per day, at a
 * specified local time.
 */
public class DailySchedule implements Schedule {
	private final ZoneId zoneId;
	private final LocalTime time;

	/**
	 * Constructs a new schedule that will execute at the given time, using the
	 * system's default time zone.
	 * @param time The time at which to execute any tasks using this schedule.
	 */
	public DailySchedule(LocalTime time) {
		this(time, ZoneId.systemDefault());
	}

	/**
	 * Constructs a new schedule that will execute at the given time, using the
	 * given zone id for time zone information.
	 * @param time The time at which to execute any tasks using this schedule.
	 * @param zoneId The time zone id.
	 */
	public DailySchedule(LocalTime time, ZoneId zoneId) {
		this.time = time;
		this.zoneId = zoneId;
	}

	@Override
	public Optional<Instant> getNextExecutionTime(Instant referenceInstant) {
		ZonedDateTime currentTime = referenceInstant.atZone(this.zoneId);
		LocalDate currentDay = LocalDate.ofInstant(referenceInstant, this.zoneId);
		ZonedDateTime sameDayExecution = currentDay.atTime(this.time).atZone(this.zoneId);
		if (sameDayExecution.isAfter(currentTime)) {
			return Optional.of(sameDayExecution.toInstant());
		}
		return Optional.of(sameDayExecution.plusDays(1).toInstant());
	}
}
