package nl.andrewl.simply_scheduled.schedule;

import java.time.*;
import java.util.stream.Stream;

/**
 * Tests the ability of the {@link DailySchedule} to reliably give the correct
 * next execution time.
 */
public class DailyScheduleTest extends ScheduleTest {
	@Override
	protected Stream<TestCase> getTestCases() {
		var utc = ZoneOffset.UTC;
		// For this test, we use a fixed clock at 12:30:45 on August 6, 2021, UTC.
		Clock clock = Clock.fixed(ZonedDateTime.of(2021, 8, 6, 12, 30, 45, 0, utc).toInstant(), utc);
		ZonedDateTime time = ZonedDateTime.ofInstant(clock.instant(), utc);
		return Stream.of(
				new TestCase( // A daily schedule whose time has already passed will be scheduled for tomorrow.
						new DailySchedule(LocalTime.of(12, 0), utc),
						clock.instant(),
						time.plusDays(1).toLocalDate().atTime(12, 0).toInstant(utc)
				),
				new TestCase( // A daily schedule whose time has not yet passed will be scheduled for today.
						new DailySchedule(LocalTime.of(18, 44, 3), utc),
						clock.instant(),
						time.toLocalDate().atTime(18, 44, 3).toInstant(utc)
				),
				new TestCase( // Account for a time zone which introduces some offset.
						new DailySchedule(LocalTime.of(10, 30), ZoneOffset.ofHours(-5)),
						clock.instant(),
						time.toLocalDate().atTime(15, 30).toInstant(utc)
				),
				new TestCase( // Account for a time zone whose offset makes it such that the next event is scheduled for tomorrow, in UTC.
						new DailySchedule(LocalTime.of(10, 30), ZoneOffset.ofHours(-1)),
						clock.instant(),
						time.plusDays(1).toLocalDate().atTime(11, 30).toInstant(utc)
				)
		);
	}
}
