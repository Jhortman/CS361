import java.util.LinkedList;

import javax.swing.JTextArea;
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
	
	public void add(String bibNum, JTextArea printer) {			//add racer to end of queue; if queue is empty -> start
		for(Racer r: _queue) {
			if(r.getName().equals(bibNum)) {
	
				return;
			}
		}
		
		int compare = Integer.parseInt(bibNum);
		if(compare < 0 || compare > 99999) {
			
		}
		else {
			Racer racer = new Racer(bibNum);	
			_queue.add(racer);
			
		}
		
		
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
	
	//returns true if we have racers waiting to race in queue
	public boolean hasRacers() {
		return !_queue.isEmpty();
	}
	
	//swap passed in racers finish times.  
	
	//get run number
	public int getRunNum() { return _runNum; }
}
