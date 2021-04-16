# Simply Scheduled

This is a very simple cron-like mechanism for scheduling tasks to execute at scheduled points in time. Here's an example:

```java
Runnable job = () -> System.out.println("Doing some work...");
Schedule schedule = new RepeatingSchedule(ChronoUnit.SECONDS, 5);
Scheduler scheduler = new BasicScheduler();
scheduler.add(new Task(job, schedule));
scheduler.start();
```
> In the above example, we create a new job that simply prints a string to standard output. We create a schedule that repeats the task every 5 seconds. Finally, we add the job and schedule together as a Task, add that task to a scheduler, and start the scheduler.

Besides the `RepeatingSchedule`, this module also includes the following pre-made schedule implementations:

* `DailySchedule` - Executes a task once per day, at a specified time.
* `HourlySchedule` - Executes a task once per hour, at a specified minute of the hour.
* `MinutelySchedule` - Executes a task once per minute, at a specified second.

## Extensibility

It is very simple to provide your own custom Schedule implementation if you want a more specific, fine-tuned schedule than what is provided here. Furthermore, you can also provide your own Scheduler implementation, if you prefer to use a different mechanism than the default dynamic thread pool executor service.
