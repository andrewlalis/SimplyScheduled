package nl.andrewl.simply_scheduled.schedule;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract test class which can be extended to test the functionality of a
 * particular schedule's {@link Schedule#getNextExecutionTime(Instant)} method.
 */
public abstract class ScheduleTest {
	protected static class TestCase {
		public final Schedule schedule;
		public final Instant referenceInstant;
		public final Instant expectedNextExecution;
		public TestCase(Schedule schedule, Instant referenceInstant, Instant expectedNextExecution) {
			this.schedule = schedule;
			this.referenceInstant = referenceInstant;
			this.expectedNextExecution = expectedNextExecution;
		}
	}

	@Test
	public void testGetNextExecutionTime() {
		var cases = getTestCases().collect(Collectors.toList());
		for (int i = 0; i < cases.size(); i++) {
			var testCase = cases.get(i);
			var r = testCase.schedule.getNextExecutionTime(testCase.referenceInstant);
			if (testCase.expectedNextExecution == null) {
				assertTrue(r.isEmpty(), "Case " + i + ": next execution time is not empty when it should be.");
			} else {
				assertTrue(r.isPresent(), "Case " + i + ": next execution time is not present when it should be.");
				assertEquals(testCase.expectedNextExecution, r.get(), "Case " + i + ": expected next execution time does not match expected.");
			}
		}
	}

	protected abstract Stream<TestCase> getTestCases();
}
