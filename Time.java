import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Time {
	Date sysClock = new Date();
	long startTime;
	long endTime;
	
	public Time() {
		startTime = 0;
		endTime = 0;
	}
	
	private long[] parseTime(String timeIn) {
		long[] times = new long[4];
		
		String[] hms = timeIn.split(":");
		times[0] = Long.parseLong(hms[0]);		// Hours
		times[1] = Long.parseLong(hms[1]);		// Minutes
		String[] sms = hms[1].split(".");
		times[2] = Long.parseLong(sms[0]);		// Seconds
		times[3] = Long.parseLong(sms[1]);		// Milliseconds
		
		return times;
	}
	
	private long toMills(long[] times) {
		return (TimeUnit.HOURS.toMillis(times[0])
			  + TimeUnit.MINUTES.toMillis(times[1])
			  + TimeUnit.SECONDS.toMillis(times[2])
			  + times[3]);
	}
	
	private String toHMSString(long time) {
		return String.format("%01d:%02d:%02d.%01d",						// [Format] h:mm:ss:m
							 TimeUnit.MILLISECONDS.toHours(time),
						     TimeUnit.MILLISECONDS.toMinutes(time),
						     TimeUnit.MILLISECONDS.toSeconds(time),
						     TimeUnit.MILLISECONDS.toMillis(time));
	}
	
	public void start() {
		startTime = sysClock.getTime();
	}
	
	public void start(String timeIn) {
		long[] times = parseTime(timeIn);
		startTime = toMills(times);
	}
	
	public void finish() {
		endTime = sysClock.getTime();
	}
	
	public void finish(String timeIn) {
		long[] times = parseTime(timeIn);
		endTime = toMills(times);
	}
	
	public String getTime() {
		return toHMSString(sysClock.getTime());
	}
}