package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A schedule in which a discrete number of execution times are defined at
 * initialization.
 */
public class SpecificInstantSchedule implements Schedule {
	private final List<Instant> executionTimes;

	public SpecificInstantSchedule(Instant... executionTimes) {
		this(new ArrayList<>(Arrays.asList(executionTimes)));
	}

	public SpecificInstantSchedule(ZonedDateTime... zonedDateTimes) {
		this(Arrays.stream(zonedDateTimes).map(ZonedDateTime::toInstant).collect(Collectors.toList()));
	}

	private SpecificInstantSchedule(List<Instant> executionTimes) {
		this.executionTimes = executionTimes;
		Collections.sort(this.executionTimes);
	}

	@Override
	public Optional<Instant> getNextExecutionTime(Instant referenceInstant) {
		while (!this.executionTimes.isEmpty()) {
			Instant nextExecutionTime = this.executionTimes.remove(0);
			if (nextExecutionTime.isBefore(referenceInstant)) {
				return Optional.of(nextExecutionTime);
			}
		}
		return Optional.empty();
	}
}
