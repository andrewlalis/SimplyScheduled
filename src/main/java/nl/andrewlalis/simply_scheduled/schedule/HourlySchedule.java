package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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
	public Instant getNextExecutionTime(Instant referenceInstant) {
		ZonedDateTime currentTime = referenceInstant.atZone(this.zoneId);
		int currentMinute = currentTime.getMinute();
		if (currentMinute < this.minute) {
			return currentTime.truncatedTo(ChronoUnit.MINUTES).plusMinutes(this.minute - currentMinute).toInstant();
		}
		return currentTime.plusHours(1).plusMinutes(this.minute).truncatedTo(ChronoUnit.MINUTES).toInstant();
	}
}
