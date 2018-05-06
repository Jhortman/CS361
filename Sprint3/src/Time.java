
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Time {
	private static Date sysClock; //might need to use new call of date to get updated time 
	
	public Time() {
		sysClock = null;
	}
	//convert formatted time into an array of longs holding h/m/s/ms
	protected static long[] parseTime(String timeIn) {
		long[] times = new long[4];
		
		String[] hms = timeIn.split(":");
		times[0] = Long.parseLong(hms[0]);		// Hours
		times[1] = Long.parseLong(hms[1]);		// Minutes
		String[] sms = hms[2].split("\\.");
		times[2] = Long.parseLong(sms[0]);		// Seconds
		times[3] = Long.parseLong(sms[1]);		// Milliseconds
		
		return times;
	}
	
	//convert long holding h/m/s/ms into one giant long holding milliseconds
	protected static long toMills(long[] times) {
		return (TimeUnit.HOURS.toMillis(times[0])
			  + TimeUnit.MINUTES.toMillis(times[1])
			  + TimeUnit.SECONDS.toMillis(times[2])
			  + times[3]);
	}
	
	//parse time in ms to string format h:m:s:hundreths
	protected static String toHMSString(long time) {
		String ret = "";
		
		long hours = time / 1000 / 60 / 60;
		long minutes = (time / 1000 / 60) - (hours * 60);
		long seconds = (time / 1000) - (hours * 60 * 60) - (minutes * 60);
		long hSeconds = (time - (hours * 60 * 60 * 1000) - (minutes * 60 * 1000) - (seconds * 1000)) / 10; // divided by 10 because we want hundreths not ms
		
		if (hours > 24) {		// testing cases for very long inputs 
			hours = (hours % 12) + 7; // offset for timezone
		}
		
		if(hours > 12) {
			hours -= 12;
		}
		
		if (hours < 10) ret += "0" + hours + ":"; else ret += hours + ":";
		if (minutes < 10) ret += "0" + minutes + ":"; else ret += minutes + ":";
		if (seconds < 10) ret += "0" + seconds; else ret += seconds;
		ret += "." + hSeconds;
		return ret;
	}
	//for getting a simplified raceTime
	protected static String raceHMSString(long time) {
		String ret = "";
		
		long hours = time / 1000 / 60 / 60;
		long minutes = (time / 1000 / 60) - (hours * 60);
		long seconds = (time / 1000) - (hours * 60 * 60) - (minutes * 60);
		long hSeconds = (time - (hours * 60 * 60 * 1000) - (minutes * 60 * 1000) - (seconds * 1000)) / 10; // divided by 10 because we want hundreths not ms
		
		if (hours > 24) {		// testing cases for very long inputs 
			hours = (hours % 12) + 7; // offset for timezone
		}
		
		if(hours > 12) {
			hours -= 12;
		}
		//if hours | minutes | seconds are equal to zero
		if (hours == 0){} else if (hours < 10) { ret += "0" + hours + ":"; } else ret += hours + ":";
		if (minutes == 0){} else if (minutes < 10){ ret += "0" + minutes + ":"; } else ret += minutes + ":";
		if (seconds < 10) ret += "0" + seconds; else ret += seconds;
		ret += "." + hSeconds;
		return ret;
	}
	
	
	
	public static long getTime() {
		if (sysClock != null) {			// assigned sysClock time from reading time from files or commands
			return sysClock.getTime();
		}
		return System.currentTimeMillis();	//just current time in world
	}
	
	
	public void setTime() {			//initialize time to null 
		sysClock = null;
	}
	//used for setting system time that is parsed in through given file or even input
	public static void setTime(String timeIn) {
		long[] times = parseTime(timeIn);
		sysClock = new Date(toMills(times));
	}
	public String getTimeAsString() {
		String ret = toHMSString(sysClock.getTime());
		return ret;
	}
	
	public String getRacingTime(long time) {
		return raceHMSString(time);
	}
	
	
}