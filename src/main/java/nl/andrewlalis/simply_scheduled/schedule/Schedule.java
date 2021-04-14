package nl.andrewlalis.simply_scheduled.schedule;

import java.time.Instant;

public interface Schedule {
	Instant getNextExecutionTime(Instant referenceInstant);
}
