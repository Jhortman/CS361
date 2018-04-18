import java.util.LinkedList;
public class Run {
	
	private LinkedList<Racer> _queue;
	private String _event;
	private int _runNum;
	
																		//eventually should implement a racer class to assign racers start/end and finish times
	//handles default case when event is null -> event = IND
	public Run(String event, int num) {
		if(event == null) {
			_event = "IND";
			_queue = new LinkedList<Racer>();
			_runNum = num;
		}
		else {
			_event = event;
			_queue = new LinkedList<Racer>();
			_runNum = num;
		}
		
	}
	
	public void pushRacer(Racer bibNum) {		//adds racer to start of queue
		_queue.addFirst(bibNum);				//used in the event of a cancel 
	}
	
	public void add(String bibNum) {			//add racer to end of queue; if queue is empty -> start
	 Racer racer = new Racer(bibNum);	
		_queue.add(racer);
		System.out.println(Time.toHMSString(Time.getTime()) + " " + bibNum + " has been added to queue");
	}
	
	//removes first racer in queue waiting to start 
	public Racer popRacer() {
		return _queue.removeFirst();
	}
	
	//get associated event 
	public String getCurEvent() {
		return _event;
	}
	
	public LinkedList<Racer> getRacers(){
		return _queue;
	}
	//swap out passed in racer finish times with current racer finish time. 
	// might modify method later to handle two passed in racers. 
	public void swapFinish(Racer racerOne, Racer racerTwo) {
		long temp = racerOne.getFinishAsLong();
			
		racerOne.setFinish(racerTwo.getFinishAsLong());
		racerTwo.setFinish(temp);
			
		racerTwo.setRaceTime(Time.toHMSString(racerTwo.getFinishAsLong() - racerTwo.getStartAsLong()));
		racerOne.setRaceTime(Time.toHMSString(racerOne.getFinishAsLong() - racerOne.getStartAsLong()));
		
		}
	
	//returns true if we have racers waiting to race in queue
	public boolean hasRacers() {
		return !_queue.isEmpty();
	}
	
	//get run number
	public int getRunNum() { return _runNum; }
}
