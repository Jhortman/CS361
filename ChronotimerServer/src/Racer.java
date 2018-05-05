

public class Racer{
	
	private String name;
	private String realName;
	private long start;
	private long finish;
	private String raceTime;
	
	public Racer(String bibNum) {
		name = bibNum;
		start = 0;
		finish = 0;
		raceTime = null;	
		realName = "";
	}
	
	public String getName() {
		return name;
	}
	public String getRealName() {
		return realName;
	}
	public String getRaceTime() {
		return raceTime;
	}
	public void setRealName(String rN) {
		realName = rN;
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
	
	
	

}