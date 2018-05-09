import java.util.LinkedList;
public class Storage {

	private Run _run;
	private LinkedList<Racer> _finished = new LinkedList<Racer>();
	
	public Storage() {
		_run = null;
		_finished = new LinkedList<Racer>();
	}
	
	//each event has a run and a queue of finished racers and their times associated with it
	public Storage(Run run, LinkedList<Racer> finished) {
		_run = run;
		_finished.addAll(finished);
		
	}
	
	public Run getRun() { return _run; 	}
	
	public void setRun(Run run) { _run = run; 	}
	
	public LinkedList<Racer> getFinish() { return _finished; 	}
	
	public void setFinish(LinkedList<Racer> finish) { _finished = finish; 	}
	
	
}
