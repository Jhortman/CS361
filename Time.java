import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Time {
	Date sysClock = new Date();
	long startTime;
	long endTime;
	long currentTime;
	
	public Time() {
		startTime = 0;
		endTime = 0;
		currentTime = 0;
	}
	
	private long[] parseTime(String timeIn) {
		long[] times = new long[4];
		
		String[] hms = timeIn.split(":");
		times[0] = Long.parseLong(hms[0]);		// Hours
		times[1] = Long.parseLong(hms[1]);		// Minutes
		String[] sms = hms[2].split("\\.");
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
		long hours = time / 1000 / 60 / 60;
		long minutes = (time / 1000 / 60) - (hours * 60);
		long seconds = (time / 1000) - (hours * 60 * 60) - (minutes * 60);
		long mSeconds = time - (hours * 60 * 60 * 1000) - (minutes * 60 * 1000) - (seconds * 1000);
		
		return hours + ":" + minutes + ":" + seconds + "." + mSeconds;
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
		return LocalTime.now().toString().substring(0,10);
	}
	
	public void setTime(String timeIn) {
		long[] times = parseTime(timeIn);
		currentTime = toMills(times);
	}
	
	public String getStart() {
		return toHMSString(this.startTime);
	}
	
	public String getFinish() {
		return toHMSString(this.endTime);
	}
	
	public String totalTime() {
		return toHMSString(this.endTime - this.startTime);
	}
}