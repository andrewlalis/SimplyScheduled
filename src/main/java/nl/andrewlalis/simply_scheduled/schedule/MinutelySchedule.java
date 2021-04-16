package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MinutelySchedule implements Schedule {

	private final int second;
	private final ZoneId zoneId;

	public MinutelySchedule(int second, ZoneId zoneId) {
		this.second = second;
		this.zoneId = zoneId;
	}

	public MinutelySchedule(int second) {
		this(second, ZoneId.systemDefault());
	}

	@Override
	public Instant getNextExecutionTime(Instant referenceInstant) {
		ZonedDateTime currentTime = referenceInstant.atZone(this.zoneId);
		int currentSecond = currentTime.getSecond();
		if (currentSecond >= this.second) {
			return currentTime.plusMinutes(1).withSecond(this.second).truncatedTo(ChronoUnit.SECONDS).toInstant();
		}
		return currentTime.withSecond(this.second).truncatedTo(ChronoUnit.SECONDS).toInstant();
	}
}
