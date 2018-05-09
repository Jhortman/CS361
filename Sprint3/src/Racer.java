

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
	public void setName(String rname){
		name = rname;
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
	
	public long getFinishAsLong() {
				return finish;
			}
	
	public long getStartAsLong() {
				return start;
			}
			
	public void setFinish(long fin) {
				finish = fin;
			}
	
	public void setRaceTime(String time) {
				raceTime = time;
			}
	
	// make static call to time class to access method that gets the right system time to assign
	public void start() {
		start = Time.getTime();
	}
	
	//if successful finish go ahead and calculate racetime
	public void finish() {
		finish = Time.getTime();
		raceTime = Time.raceHMSString(finish - start);
		
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