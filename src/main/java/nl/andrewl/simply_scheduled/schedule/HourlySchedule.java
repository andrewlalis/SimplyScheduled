package nl.andrewl.simply_scheduled.schedule;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * An hourly schedule is used to execute a task once per hour, at a specific
 * minute of the hour.
 */
public class HourlySchedule implements Schedule {
	private final ZoneId zoneId;
	private final int minute;

	public HourlySchedule(int minute) {
		this(minute, ZoneId.systemDefault());
	}

	public HourlySchedule(int minute, ZoneId zoneId) {
		if (minute < 0 || minute > 59) {
			throw new IllegalArgumentException("Minute must be in the range [0, 59].");
		}
		this.zoneId = zoneId;
		this.minute = minute;
	}

	@Override
	public Optional<Instant> getNextExecutionTime(Instant referenceInstant) {
		ZonedDateTime currentTime = referenceInstant.atZone(this.zoneId);
		int currentMinute = currentTime.getMinute();
		if (currentMinute < this.minute) {
			return Optional.of(currentTime.truncatedTo(ChronoUnit.MINUTES).plusMinutes(this.minute - currentMinute).toInstant());
		}
		return Optional.of(currentTime.plusHours(1).plusMinutes(this.minute).truncatedTo(ChronoUnit.MINUTES).toInstant());
	}
}
