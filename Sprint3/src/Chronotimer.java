
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.swing.JTextArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class Chronotimer {
	private Channel[] _channels;
	private Racer _curRacer;
	private String _event;
	private Run _curRun;
	private ArrayList<Storage> _storage;
	private LinkedList<Racer> _racing;
	private LinkedList<Racer> _finished;
	private LinkedList<Racer> _tempFinished;
	private HashMap<Integer, Racer> _racingMap;
	private boolean power;
	private Time time;
	private static int _runNum = 0;
	private JTextArea _printer;
	private RunningDisplay _display;
	

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
		_tempFinished = new LinkedList<Racer>();
		_racingMap = new HashMap<Integer,Racer>();
		time = new Time();
		_printer = printer;
	}
	public Run getRun() {
		return _curRun;
	}
	public boolean getPower() {
		return power;
	}
	public Time getTime(){
		return time;
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
			
			
		}
		else {
			power = false;
			System.out.println(Time.toHMSString(Time.getTime()) + " Power Off");
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
		_tempFinished.clear();					//clear all racers from temp queue;
		_racingMap.clear();
		_storage.clear();   					//clear out storage
		for(int i = 0; i < 8; i++) {			// disarm all channels if active
			if(_channels[i].getState()) {
				_channels[i].toggleState();
				disc(_channels[i].getNum());	//disconnect any sensors found connected to respective channel
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
		if(_curRun == null)	{
			return;
		}
		
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
		if((_curRun.hasRacers() || _curRun.getCurEvent().equals("PARGRP")) && _channels[i].getState() && ((_channels[i].getNum() % 2) == 1)){
			
			if(_curRun.getCurEvent().toUpperCase().equals("GRP") && _channels[i].getNum() == 1){ //if event is GRP event then dequeue all racers in waiting queue upon triggering channel 1 (all racers have same start)
				startGRP();	
			}
			else if (_curRun.getCurEvent().toUpperCase().equals("PARGRP") && _channels[i].getNum() == 1) {
				if(!_racing.isEmpty()) {
					trigFinishParGrp(_channels[i].getNum());
					//for when we hit channel one again to trigger a finish instead of a start for PARGRP
				}
				else {
					startGRP();	//PARGRP has same start manner as GRP
				}
				
			}
			else if(_curRun.getCurEvent().toUpperCase().equals("PARGRP") && _channels[i].getNum() != 1) {
				trigFinishParGrp(_channels[i].getNum());
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
			if(_curRun.getCurEvent().toUpperCase().equals("GRP")){ //if event is GRP event then dequeue all racers in waiting queue upon triggering channel 1 (all racers have same start)
				finishGRP();
			}
			else if(_curRun.getCurEvent().toUpperCase().equals("PARGRP")) {
				trigFinishParGrp(_channels[i].getNum());
			}
			else {
			_curRacer = _racing.removeFirst(); // first in first to finish 
			_curRacer.finish();
			_finished.add(_curRacer);  // add racer to finished queue
			System.out.println(Time.toHMSString(Time.getTime()) + " Finish time for " + _curRacer.getName() + " is " +  _curRacer.getFinish() );
			_printer.append("Finish time for " + _curRacer.getName() + " is " +  _curRacer.getFinish() + "\r\n" );
			}
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
			_display = new RunningDisplay();
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
			if(!_racing.isEmpty()) {		//any racers still racing while event ends receive a DNF
				int qsize = _racing.size();
				for(int j = 0; j < qsize;j++) {
					_curRacer = _racing.removeFirst();
					_curRacer.DNF();
					_finished.add(_curRacer);
				}
			
			}
			
			System.out.println(Time.toHMSString(Time.getTime()) + " Event " + _curRun.getCurEvent() + " is ending (Saving run: Clearing ended run from immediate memory)");
			_printer.append("Event " + _curRun.getCurEvent() + " is ending (Saving run: Clearing ended run from immediate memory)" + "\r\n");
			_storage.add(new Storage(_curRun, _finished));  	//save run results into storage class
			
			String out = new Gson().toJson(_storage.get(_storage.size()-1));
			System.out.println(out);
			sendPost(out);	//Convert last created _storage into JSON using gson to send a post message over to our server
			_curRun = null;				//deactivate run
			_finished.clear();			//clear all racers from finished queue;
			_racing.clear();			//clear all racers from active queue;
			_tempFinished.clear();		//clear all racers from temp quueue;
			_racingMap.clear();
			_event = null;
			
			
			
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
			           out = new Gson().toJson(_storage.get(i));					//get object in storage that we want to convert to JSON formatted string
			           file.write(out);									//write jSON string to file
			           File f = new File("Run" + runNumber + ".txt");	//open freshly created file 
			           System.out.println(f.getAbsolutePath());			//find path to where file was created 
			           System.out.println(Time.toHMSString(Time.getTime()) + " " + runNumber + " Successfully exported");
			           _printer.append(runNumber + " Successfully exported\n");
			           file.close(); 
			          }catch(Exception e){
			        	  System.out.println(e);
			        	  _printer.append(e + "\n");
			          }  
			}
			else {
				System.out.println(Time.toHMSString(Time.getTime()) + " Could not find run number in storage!");
				_printer.append("Could not find run number in storage!" + "\r\n");
				return;
			}
		}
			
	}
	
	//method only usable during IND event and swaps the next two racers to finish 
	private void swap() {
		if(_racing.size() >= 2 && (_curRun.getCurEvent().equals("IND") || _curRun.getCurEvent().equals("PARIND"))) {	// need atleast two people racing in order to swap
			
			//next two to finish for IND are swapped in the current racing queue
			Racer temp = _racing.get(0);
			_racing.set(0, _racing.get(1));
			_racing.set(1, temp);	
			System.out.println(Time.toHMSString(Time.getTime()) + " " + _racing.get(1).getName() + " and " + _racing.get(0).getName() + " have been swapped");
			_printer.append(_racing.get(1).getName() + " and " + _racing.get(0).getName() + " have been swapped\n");
		}
		else {
			System.out.println(Time.toHMSString(Time.getTime()) + " Invalid conditions for Swap");
			_printer.append("Invalid conditions for swap\n");
		}
	}
	
	//try to set sensortype to channel from given sensor/channel
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
	//try to disconnect sensor from given channel --> set sensor type to NONE
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
	
	//if event is GRP event then dequeue all racers in waiting queue upon triggering channel 1 (all racers have same start)
	//method written to reduce clutter in trig method
	private void startGRP() {
		int qsize = _curRun.getRacers().size();
		for(int j = 0; j < qsize; j++){			//iterate through queue popping all racers
			
			_curRacer = _curRun.popRacer(); //pop racer from start queue 
			_curRacer.start();
			_racing.add(_curRacer); // add racer to racers: currently racing queue
			
			if(_curRun.getCurEvent().toUpperCase().equals("PARGRP") && j < 8) {
				_racingMap.put(j+1,_curRacer);				//use size of those currently racing to dictate the keys and also line up with channel lanes
				System.out.println(Time.toHMSString(Time.getTime()) + " Start time for " + _curRacer.getName() + " in lane " + (j+1) + " is " + _curRacer.getStart());
				_printer.append("Start time for " + _curRacer.getName() + " in lane " + (j+1) + " is " + _curRacer.getStart() + "\r\n");
			}
			else {
				System.out.println(Time.toHMSString(Time.getTime()) + " Start time for " + _curRacer.getName() + " is " + _curRacer.getStart());
				_printer.append("Start time for " + _curRacer.getName() + " is " + _curRacer.getStart() + "\r\n");
			}
		}
		
	}
	
	//shoves current racers into a temporary queue in which they are to be sorted later with their proper finish times
	private void finishGRP() {
		
		_curRacer = _racing.removeFirst(); // first in first to finish 
		_curRacer.finish();
		_tempFinished.add(_curRacer);  // add racer to temp finished queue
		System.out.println(Time.toHMSString(Time.getTime()) + " Finish recorded at " + _curRacer.getFinish());
		_printer.append("Finish recorded at " + _curRacer.getFinish() + "\r\n");
		
		if(_racing.isEmpty()) {
			System.out.println(Time.toHMSString(Time.getTime()) + " All racers have finished! Please enter bib nums in order: 1st, 2nd, 3rd...using keypad (press # to enter)");
			_printer.append("All racers have finished! Please enter bib nums in order: 1st, 2nd, 3rd...using keypad (press # to enter)\n");
		}
	}
	
	//method for inputting finishes 
	public void inputGrpFinish(String bibnum) {
		if(_tempFinished.isEmpty()) {
			return;
		}
		
		for(int i = 0; i < _tempFinished.size(); i++) {
			
			//if found then valid bibnum
			//if bibnum is found in queue swap their finish time with index 0 (if racer already has proper order in queue it can still swap with itself)
			if(_tempFinished.get(i).getName().equals(bibnum)) {
				
				//swap racer to position one
				Racer temp = _tempFinished.get(0);
				_tempFinished.set(0, _tempFinished.get(i));
				_tempFinished.set(i, temp);	
				
				//swap finish times between two racers
				_curRun.swapFinish(_tempFinished.get(0),_tempFinished.get(i));
				
				System.out.println(Time.toHMSString(Time.getTime()) + " Racer " + _tempFinished.get(0).getName() + "'s new finish time is " + _tempFinished.get(0).getFinish());
				_printer.append("Racer " + _tempFinished.get(0).getName() + "'s new finish time is " + _tempFinished.get(0).getFinish() + "\n");
				_finished.add(_tempFinished.removeFirst());			//add finalized finish time for given racer into official finished racers queue used to be used in storage
				
				return;
			}
		}
		//no racers found in tempFinish or invalid bib number: print and do nothing
		System.out.println(" Bibnum not associated with this race");
		_printer.append("Bibnum not associated with this race\n");
		
		
	}
	private void trigFinishParGrp(int i) {
		if(_racingMap.isEmpty()) {
			return;
		}
		
		
		if(_racingMap.containsKey(i)) {
			Racer temp = _racingMap.get(i);
			_racingMap.remove(i);
			
			for(int j = 0; j < _racing.size();j++) {
				if(_racing.get(j).getName().equals(temp.getName())) {
					_curRacer = _racing.remove(j);
					_curRacer.finish();
					_finished.add(_curRacer);
					System.out.println(Time.toHMSString(Time.getTime()) + " Finish time for " + _curRacer.getName() + " in lane " + i + " is " + _curRacer.getFinish() );
					_printer.append("Finish time for " + _curRacer.getName() + " in lane " + i + " is " + _curRacer.getFinish() + "\r\n");
				}
			}
			
		}
			
	}
	
	private void sendPost(String JSON) {
		try {
			System.out.println("in the client");

			// Client will connect to this location
			URL site = new URL("http://:8000/sendresults");
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();

			// now create a POST request
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			// build a string that contains JSON from console
			String content = "";
			content = JSON;

			// write out string to output buffer for message
			out.writeBytes(content);
			out.flush();
			out.close();

			System.out.println("Done sent to server");

			
			InputStreamReader inputStr = new InputStreamReader(conn.getInputStream());
			
			// string to hold the result of reading in the response
			StringBuilder sb = new StringBuilder();

			// read the characters from the request byte by byte and build up
			// the Response
			int nextChar;
			while ((nextChar = inputStr.read()) > -1) {
				sb = sb.append((char) nextChar);
			}
			System.out.println("Return String: " + sb);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	//sticking class here since its easier to grab what I need from the chronotimer
	//surrounded everything in try/catch since it will be throwing many errors upon trying to check for potential changes in updates to be made for the display
	protected class RunningDisplay
	{
		private String _waiting;
		private String racing;
		private String finished;
		
		private void display()
		{
			switch(_curRun.getCurEvent())
			{
			case "IND":
			{
				try{
					_waiting = "";
					if(!_curRun.getRacers().isEmpty()) {
						_waiting += _curRun.getRacers().get(0).getName() + "  >\n";
						for(int i = 1; i < _curRun.getRacers().size(); i++)
							if(i < 3) {
								_waiting += _curRun.getRacers().get(i).getName() + "\n";
							}
					}
				}catch(IndexOutOfBoundsException e) {}
				
				try{ 
					racing = "";
					if(!_racing.isEmpty()) {
						racing += _racing.get(_racing.size() -1 ).getName() + "  " + time.getRacingTime(System.currentTimeMillis() - _racing.get(_racing.size() -1).getStartAsLong()) + "\n";
						if(_racing.size() > 1){
							for(int i = _racing.size() - 2; i >= 0; i--) {
								racing += _racing.get(i).getName() +  "  " + time.getRacingTime(System.currentTimeMillis() - _racing.get(i).getStartAsLong()) + "\n";
							}
						}
					}
				}catch(IndexOutOfBoundsException e) {}
				
				try{
					finished = "";
					if(!_finished.isEmpty()) {
						finished += _finished.getLast().getName() + "  " + _finished.getLast().getRaceTime();
					}
				}catch(NoSuchElementException e) {}
				break;
			}
			case "PARIND":
			{
				try{
					_waiting = "";
					if(!_curRun.getRacers().isEmpty()) {
						_waiting += _curRun.getRacers().get(0).getName() + " >\n";
					}
				}catch(IndexOutOfBoundsException e) {}
				
				try{
					
					if(_curRun.getRacers().size() > 1) {
						_waiting += _curRun.getRacers().get(1).getName() + "\n";
					}
				}catch(IndexOutOfBoundsException e) {}
				
				try{
					racing = "";
					if(!_racing.isEmpty()) {
						racing += _racing.get(_racing.size() -1 ).getName() + "  " + time.getRacingTime(System.currentTimeMillis() - _racing.get(_racing.size() -1).getStartAsLong()) + "\n";
						if(_racing.size() > 1){
							for(int i = _racing.size() - 2; i >= 0; i--) {
								racing += _racing.get(i).getName() +  "   " + time.getRacingTime(System.currentTimeMillis() - _racing.get(i).getStartAsLong()) + "\n";
							}
						}
					}
				}catch(Exception e) {}
				
				try{
					finished= "";
					if(_finished.size() == 1) {
						finished += _finished.get(_finished.size()-1).getName() + "  " + _finished.get(_finished.size()-1).getRaceTime() + "\n";
					}
					if (_finished.size() > 1){
						finished += _finished.get(_finished.size()-2).getName() + "  " + _finished.get(_finished.size()-2).getRaceTime() + "\n";
						finished += _finished.get(_finished.size()-1).getName() + "  " + _finished.get(_finished.size()-1).getRaceTime() + "\n";
					}
				}catch(IndexOutOfBoundsException e) {}
				
				try{
					if(!_finished.isEmpty()) {
						finished += _finished.get(_finished.size()).getName() + " " + _finished.get(_finished.size()-1).getRaceTime();
					}
				}catch(IndexOutOfBoundsException e) {}
				break;
			}
			case "GRP":
			{
				_waiting = "";		//dont care about people waiting
				try{
					racing = "";
					if(!_racing.isEmpty()) {
						racing += _racing.get(_racing.size() -1 ).getName() + "  " + time.getRacingTime(System.currentTimeMillis() - _racing.get(_racing.size() -1).getStartAsLong()) + "\n";
						if(_racing.size() > 1){
							for(int i = _racing.size() - 2; i >= 0; i--) {
								racing += _racing.get(i).getName() +  "   " + time.getRacingTime(System.currentTimeMillis() - _racing.get(i).getStartAsLong()) + "\n";
							}
						}
					}
				}catch(IndexOutOfBoundsException e) {}
				
				try{
					finished = "";
					if(!_finished.isEmpty()) {
						finished += _finished.getLast().getName() + "  " + _finished.getLast().getRaceTime() + "\n";
					}
				}catch(NoSuchElementException e) {}
			}
			case "PARGRP":
			{
				_waiting = "";
				try{
					racing = "";
					if(!_racing.isEmpty()) {
						racing += _racing.get(_racing.size() -1 ).getName() + "  " + time.getRacingTime(System.currentTimeMillis() - _racing.get(_racing.size() -1).getStartAsLong()) + "\n";
						if(_racing.size() > 1){
							for(int i = _racing.size() - 2; i >= 0; i--) {
								racing += _racing.get(i).getName() +  "   " + time.getRacingTime(System.currentTimeMillis() - _racing.get(i).getStartAsLong()) + "\n";
							}
						}
					}
				}catch(NoSuchElementException e) {}
				
				try{
					finished = "";
					if(!_finished.isEmpty()) {
						finished += "In Lane: " + _finished.getLast().getName() + " " +  _finished.getLast().getRaceTime() + "\n";
					}
				}catch(NoSuchElementException e) {}
				break;
			}
			default:break;
			}
		}
		private String getWaiting() {return _waiting;}
		private String getRacing() {return racing;}
		private String getFinished() {return finished;}
	}
	
	public String displayWaiting(){
		_display.display();
		return _display.getWaiting();
	}
	public String displayRacing(){
		_display.display();
		return _display.getRacing();
	}
	public String displayFinished(){
		_display.display();
		return _display.getFinished();
	}
	/*
	 public int getKey(String bibNum){
		    for(int i = 1; i < 9; i++){
		    	if(_racingMap.get(i).getName().equals(bibNum)){
		    		return i;
		 
		    	}
		    	else{
		    		return -1;
		    	}
		    }
		    
		  }
*/

}