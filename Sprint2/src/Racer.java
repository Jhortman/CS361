

public class Racer{
	
	private String name;
	private long start;
	private long finish;
	private String raceTime;
	
	public Racer(String bibNum) {
		
		name = bibNum;
		start = 0;
		finish = 0;
		raceTime = null;
	}
	
	public String getName() {
		return name;
	}
	
	//might use later
	public String getStart() {
		return Time.toHMSString(start);
	}
	
	public String getFinish() {
		return Time.toHMSString(finish);
	}
	
	public String getRaceTime() {
		return raceTime;
	}
	
	//swap out passed in racer finish times with current racer finish time. 
	// might modify method later to handle two passed in racers. 
	public void swapFinish(Racer racerOne, Racer racerTwo) {
		long temp = racerOne.finish;
		
		racerOne.finish = racerTwo.finish;
		racerTwo.finish = temp;
		
		racerTwo.raceTime = Time.toHMSString(racerTwo.finish - racerTwo.start);
		racerOne.raceTime = Time.toHMSString(racerOne.finish - racerOne.start);
		
	}
	// make static call to time class to access method that gets the right system time to assign
	public void start() {
		start = Time.getTime();
	}
	
	//if successful finish go ahead and calculate racetime
	public void finish() {
		finish = Time.getTime();
		raceTime = Time.toHMSString(finish - start);
		
	}
	public void resetTime() {
		start = 0;
		finish = 0;
		raceTime = null;
	}
	
	//if racer receives a DNF, record it as their racetime
	public void DNF() {
		raceTime = "DNF";
	}
	
	
	

}
