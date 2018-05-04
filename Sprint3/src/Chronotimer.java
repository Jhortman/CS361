
import java.util.LinkedList;

import javax.swing.JTextArea;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Chronotimer {
	private Channel[] _channels;
	private Racer _curRacer;
	private String _event;
	private Run _curRun;
	private ArrayList<Storage> _storage;
	private LinkedList<Racer> _racing;
	private LinkedList<Racer> _finished;
	private boolean power;
	private Time time;
	private static int _runNum = 0;
	private Gson g = new Gson();
	private JTextArea _printer;

	public Chronotimer(JTextArea printer) {
		// initializing all of the timer 
		_channels = new Channel[8];
		for(int i = 0; i < 8; i++) _channels[i] = new Channel(i);
		_event = null;
		power = false;
		_curRun = null;
		_storage = new ArrayList<Storage>();
		_racing = new LinkedList<Racer>();
		_finished = new LinkedList<Racer>();
		time = new Time();
		_printer = printer;
	}
	public Run getRun() {
		return _curRun;
	}
	public boolean getPower() {
		return power;
	}
	
	public void COMMANDS(String action) {
		
		String[] tokens;
		tokens = action.split(" ");
		if(!power && !tokens[0].toUpperCase().equals("POWER")) { // if power is off just return unless "power" is passed in
			return;
		}
			
			switch (tokens[0].toUpperCase()) {
			case "POWER"	: POWER();break;
			case "EXIT"		: EXIT();break;
			case "RESET"	: RESET();break;
			case "TIME"		: TIME(tokens[1]);break;
			case "EVENT"	: _event = tokens[1]; System.out.println(Time.toHMSString(Time.getTime()) + " " + "event is " + _event ); _printer.append("event is " + _event + "\r\n" ); break;
			case "DNF"		: if(_curRun != null) DNF();break;
			case "CANCEL"	: CANCEL();break;
			case "TOG"		: TOG(Integer.parseInt(tokens[1]) - 1);break; // channels stored 0-7, input given 1-8 -> decrement by 1 for right offset
			case "TRIG"		: TRIG(Integer.parseInt(tokens[1])- 1);break;
			case "START"	: START(); break;
			case "FINISH"	: FINISH();  break;
			case "NUM" 		: if (_curRun != null)  _curRun.add(tokens[1],_printer);  break;
			case "NEWRUN"	: newRun();	  break;
			case "ENDRUN"	: endRun();   break;
			case "PRINT"	: Print() ;   break;
			case "EXPORT"	: Export(Integer.parseInt(tokens[1])); break;
			case "SWAP"		: swap(); 	  break;
			case "CONN" 	: conn(tokens[1], Integer.parseInt(tokens[2]) - 1); break;
			case "DISC"		: disc(Integer.parseInt(tokens[1]) - 1);			break;
			
			default: System.out.println("bad input"); break;
			 
		}
	}
	private void POWER() { 
		if(!power) {		// if power is off turn it on and disarm all channels
			power = true;
			System.out.println(Time.toHMSString(Time.getTime()) + " Power On");
			_printer.append("Power On" + "\r\n");
			RESET();
			
		}
		else {
			power = false;
			System.out.println(Time.toHMSString(Time.getTime()) + " Power Off");
			_printer.append("Power Off" + "\r\n");
		}
	}
	private void EXIT() {
		System.exit(0);
	}
	private void RESET() {
		_curRun = null;							//reset run
		time.setTime();							//set time to current time in system
		_event = null;							// reset event
		_finished.clear();						//clear all racers from finished queue;
		_racing.clear();						//clear all racers from active queue;
		_storage.clear();   					//clear out storage
		for(int i = 0; i < 8; i++) {			// disarm all channels if active
			if(_channels[i].getState()) {
				_channels[i].toggleState();		
			}
		}
		System.out.println(Time.toHMSString(Time.getTime()) + " System has been reset");
		_printer.append("System has been reset\n");
	
	}
	private void TIME(String s) {
		Time.setTime(s);		//set system time to the inputed time in string format
	}
	private void DNF() {		//current racer in race receives DNF
		_curRacer.DNF();
		System.out.println(Time.toHMSString(Time.getTime()) + " " + "Racer " + _curRacer.getName() + " DNF!!!");
		_printer.append("Racer " + _curRacer.getName() + " DNF!!!" + "\r\n");
		_finished.add(_racing.remove(_racing.indexOf(_curRacer))); //remove racer out of active racers and put into finished racers
	}
	private void CANCEL() {
			
		_curRacer.resetTime();		  //null and void all of current racer's times
		System.out.println(Time.toHMSString(Time.getTime()) + " " + "Racer " + _curRacer.getName() + "'s time has been discarded and was moved to start of the line" );
		_printer.append("Racer " + _curRacer.getName() + "'s time has been discarded and was moved to start of the line"  + "\r\n");
		_curRun.pushRacer(_curRacer); //push racer to start of queue
		
	}
	
	//if channel is active -> disarm and vice versa
	private void TOG(int i) {
		_channels[i].toggleState();
		if(_channels[i].getState()) {
			System.out.println(Time.toHMSString(Time.getTime()) + " " + _channels[i].getNum() + " is active.");
			_printer.append(_channels[i].getNum() + " is active." + "\r\n");
		}
		else {
			System.out.println(Time.toHMSString(Time.getTime()) + " " + _channels[i].getNum() + " is Shutting Down.");
			_printer.append(_channels[i].getNum() + " is Shutting Down." + "\r\n");
		}
	}
	
	//NEED to simplify compound conditionals here 
	//only triggers if event is going on
	private void TRIG(int i) {
		
		if(_curRun == null) {			//if no run active / nothing to do / just return
			return;
		}
		
		//allows start triggers to occur only if there is an active run/ racers in queue / channel is on / and odd parity
		if(_curRun.hasRacers() && _channels[i].getState() && ((_channels[i].getNum() % 2) == 1)){
			
			if(_curRun.getCurEvent().toUpperCase().equals("GRP")){ //if event is GRP event then dequeue all racers in waiting queue upon triggering channel 1 (all racers have same start)
				int qsize = _curRun.getRacers().size();
				for(int j = 0; j < qsize; j++){			//iterate through queue popping all racers
					
					_curRacer = _curRun.popRacer(); //pop racer from start queue 
					_curRacer.start();
					_racing.add(_curRacer); // add racer to racers: currently racing queue
					System.out.println(Time.toHMSString(Time.getTime()) + " Start time for " + _curRacer.getName() + " is " + _curRacer.getStart());
					_printer.append("Start time for " + _curRacer.getName() + " is " + _curRacer.getStart() + "\r\n");
				}
				
			}
			else{
				_curRacer = _curRun.popRacer(); //pop racer from start queue 
				_curRacer.start();
				_racing.add(_curRacer); // add racer to racers: currently racing queue
				System.out.println(Time.toHMSString(Time.getTime()) + " Start time for " + _curRacer.getName() + " is " + _curRacer.getStart());
				_printer.append("Start time for " + _curRacer.getName() + " is " + _curRacer.getStart() + "\r\n");
			}
			
		}
		//allows finish triggers to occur only if there is an active run/ active racer / channel is on / and even parity
		if(!_racing.isEmpty() && _channels[i].getState() && ((_channels[i].getNum() % 2) == 0)){// trigger finish only if channel is on and parity is even 
			_curRacer = _racing.removeFirst(); // first in first to finish 
			_curRacer.finish();
			_finished.add(_curRacer);  // add racer to finished queue
			System.out.println(Time.toHMSString(Time.getTime()) + " Finish time for " + _curRacer.getName() + " is " +  _curRacer.getFinish() );
			_printer.append("Finish time for " + _curRacer.getName() + " is " +  _curRacer.getFinish() + "\r\n" );
		}
		//else do nothing
	}
	//NEED to simplify compound conditionals here 
	public void START() {   // same as triggering start on channel 1
		
		if(_curRun!= null && _curRun.hasRacers() && _channels[0].getState()) {
			_curRacer = _curRun.popRacer();
			_curRacer.start();
			_racing.add(_curRacer); // add racer to racers: currently racing queue
			System.out.println(Time.toHMSString(Time.getTime()) + " Start time for " + _curRacer.getName() + " is " + _curRacer.getStart() );
			_printer.append("Start time for " + _curRacer.getName() + " is " + _curRacer.getStart() + "\r\n");
		}

	}
	//NEED to simplify compound conditionals here 
	private void FINISH() {  // same as triggering finish on channel 2
		
		if(_curRun!= null && !_racing.isEmpty() && _channels[0].getState()) {
			_curRacer = _racing.removeFirst(); // first in first to finish 
			_curRacer.finish();
			_finished.add(_curRacer);  // add racer to finished queue
			System.out.println(Time.toHMSString(Time.getTime()) + " Finish time for " + _curRacer.getName() + " is " + _curRacer.getFinish() );
			_printer.append("Finish time for " + _curRacer.getName() + " is " + _curRacer.getFinish() + "\r\n");
		}
	}
	
	
	//start new Run only if there currently isn't one running
	private void newRun() {	
		if(_curRun == null) {
			_curRun = new Run(_event, ++_runNum);
			System.out.println(Time.toHMSString(Time.getTime()) + " Current event " + _curRun.getCurEvent() + " is running");
			_printer.append("Current event " + _curRun.getCurEvent() + " is running\r\n");
		}
	}
	
	//terminate current run
	private void endRun() {
		if(_curRun == null) {
			System.out.println(Time.toHMSString(Time.getTime()) + " no run currently active");
			_printer.append("no run currently active\r\n");
		}
		else {
			System.out.println(Time.toHMSString(Time.getTime()) + " Event " + _curRun.getCurEvent() + " is ending (Saving run: Clearing ended run from immediate memory)");
			_printer.append("Event " + _curRun.getCurEvent() + " is ending (Saving run: Clearing ended run from immediate memory)" + "\r\n");
			_storage.add(new Storage(_curRun, _finished));  	//save run results into storage class
			_curRun = null;				//deactivate run
			_finished.clear();			//clear all racers from finished queue;
			_racing.clear();			//clear all racers from active queue;
		}
	}
	
	//prints current run of racer
	private void Print() {
		if(_finished.size() == 0) {
			System.out.println(Time.toHMSString(Time.getTime()) + " No times to report");
			_printer.append("No times to report" + "\r\n");
		}
		else {
			for(int i = 0 ; i < _finished.size(); i++) {
				System.out.println(Time.toHMSString(Time.getTime()) + " Racer " + _finished.get(i).getName() + " runtime is " + _finished.get(i).getRaceTime());
				_printer.append("Racer " + _finished.get(i).getName() + " runtime is " + _finished.get(i).getRaceTime() + "\r\n");
			}
		}
	}
	//try to write run to file in standard JSON format.
	private void Export(int runNumber) {
		String out = "";
		if(_storage.isEmpty()) {
			System.out.println(Time.toHMSString(Time.getTime()) + " Nothing to export!" );
			_printer.append("Nothing to export!\n");
			return;
		}
		for(int i = 0; i < _storage.size(); i++) {
			if(_storage.get(i).getRun().getRunNum() == runNumber){
				try{    
			           FileWriter file = new FileWriter("Run" + runNumber + ".txt");
			           out = g.toJson(_storage.get(i));					//get object in storage that we want to convert to JSON formatted string
			           file.write(out);									//write jSON string to file
			           File f = new File("Run" + runNumber + ".txt");	//open freshly created file 
			           System.out.println(f.getAbsolutePath());			//find path to where file was created 
			           file.close(); 
			          }catch(Exception e){
			        	  System.out.println(e);
			          }  
			}
			else {
				System.out.println(Time.toHMSString(Time.getTime()) + " Could not find run number in storage!");
				_printer.append("Could not find run number in storage!" + "\r\n");
				return;
			}
		}
			
	}
	
	private void swap() {
		if(_racing.size() >= 2 && _curRun.getCurEvent().equals("IND")) {
			
			//next two to finish for IND are swapped in the current racing queue
			Racer temp = _racing.get(0);
			_racing.set(0, _racing.get(1));
			_racing.set(1, temp);	
			System.out.println(Time.toHMSString(Time.getTime()) + " " + _racing.get(1).getName() + " and " + _racing.get(0).getName() + " have been swapped");
			_printer.append(_racing.get(1) + " and " + _racing.get(0) + " have been swapped\n");
		}
		else {
			System.out.println(Time.toHMSString(Time.getTime()) + " Invalid conditions for Swap");
			_printer.append("Invalid conditions for swap\n");
		}
	}
	
	private void conn(String s, int i) {
		try {
			if (s.toUpperCase().equals("EYE")  || 
				s.toUpperCase().equals("GATE") ||
				s.toUpperCase().equals("PAD")) {
				_channels[i].setSensor(s);
				System.out.println(Time.toHMSString(Time.getTime()) + " " +_channels[i].getSensor() + " is connected to " + _channels[i].getNum());
				_printer.append(" " +_channels[i].getSensor() + " is connected to " + _channels[i].getNum() + "\n");
			}
			else if(s.toUpperCase().equals("NONE")) {} //do nothing if NONE is sent over from gui
			else {
				System.out.println(Time.toHMSString(Time.getTime())+ " Unknown sensor type '" + s + "'");
				_printer.append(" Unknown sensor type '" + s + "'\n");
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(Time.toHMSString(Time.getTime()) + " Invalid channel number");
			_printer.append("Invalid channel number\n");
		
		}
	}
	private void disc(int i) {
		try {
			if(!_channels[i].getSensor().equals("NONE")) {
				System.out.println(Time.toHMSString(Time.getTime()) + " " + _channels[i].getSensor() + " Sensor has been disconected from " + _channels[i].getNum());
				_printer.append(" " + _channels[i].getSensor() + " Sensor has been disconected from " + _channels[i].getNum() + "\n");
				_channels[i].disconnect();
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(Time.toHMSString(Time.getTime()) + " Invalid channel number");
			_printer.append("Invalid channel number\n");
		}
	}

}