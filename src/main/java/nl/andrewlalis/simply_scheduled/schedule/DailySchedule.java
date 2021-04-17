package nl.andrewlalis.simply_scheduled.schedule;

import java.time.*;
import java.util.Optional;

/**
 * A daily schedule plans for the execution of a task once per day, at a
 * specified local time.
 */
public class DailySchedule implements Schedule {
	private final ZoneId zoneId;
	private final LocalTime time;

	public DailySchedule(LocalTime time) {
		this(time, ZoneId.systemDefault());
	}

	public DailySchedule(LocalTime time, ZoneId zoneId) {
		this.time = time;
		this.zoneId = zoneId;
	}

	@Override
	public Optional<Instant> getNextExecutionTime(Instant referenceInstant) {
		ZonedDateTime currentTime = referenceInstant.atZone(this.zoneId);
		LocalDate currentDay = LocalDate.from(referenceInstant);
		ZonedDateTime sameDayExecution = currentDay.atTime(this.time).atZone(this.zoneId);
		if (sameDayExecution.isBefore(currentTime)) {
			return Optional.of(sameDayExecution.toInstant());
		}
		return Optional.of(sameDayExecution.plusDays(1).toInstant());
	}
}
