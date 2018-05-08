import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JTextArea;

public class Testing {
	private Chronotimer ct = new Chronotimer(new JTextArea());
	//private ChronotimerGUI ctGUI;
	
	@Test
	public void testRaceTypeIND() {
		System.out.println("       -- TEST IND --       ");
		ct.COMMANDS("POWER");
		assertTrue(ct.getPower());
		assertNull(ct.getEvent());
		ct.COMMANDS("EVENT IND");
		assertEquals("IND", ct.getEvent());	//check event in ct
		ct.COMMANDS("NEWRUN");
		assertEquals("IND", ct.getRun().getCurEvent());	//check event of run after newrun IND is created
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("NUM 135");
		ct.COMMANDS("TRIG 1");
		assertFalse(ct.getRacing().isEmpty());
		ct.COMMANDS("TRIG 2");
		assertNotNull(ct.getEvent());
		ct.COMMANDS("ENDRUN");				
		assertNull(ct.getEvent());				//after endrun current run ends and should now be null
		assertTrue(ct.getChannels()[1].getState());	//channel 2 should still be active 
	}
	
	@Test
	public void testRaceTypePARIND() {
		System.out.println("       -- TEST PARIND --       ");
		ct.COMMANDS("POWER");
		assertEquals(null, ct.getEvent());
		ct.COMMANDS("EVENT PARIND");
		ct.COMMANDS("NEWRUN");
		assertNotNull(ct.getEvent());
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TOG 3");
		ct.COMMANDS("TOG 4");
		assertTrue(ct.getChannels()[1].getState());
		assertTrue(ct.getChannels()[2].getState());
		ct.COMMANDS("NUM 942");
		ct.COMMANDS("NUM 237");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 2");
		assertFalse(ct.getRacing().isEmpty());		//should still have racers racing
		ct.COMMANDS("DNF");
		assertTrue(ct.getRacing().isEmpty());		//should be no more racers racing
		ct.COMMANDS("ENDRUN");
	}
	
	@Test
	public void testRaceTypeGRP() {
		System.out.println("       -- TEST GRP --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("EVENT GRP");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("NUM 5");
		ct.COMMANDS("NUM 85");
		ct.COMMANDS("NUM 21");
		ct.COMMANDS("NUM 16");
		ct.COMMANDS("NUM 300");
		ct.COMMANDS("NUM 91");
		ct.COMMANDS("NUM 4");
		ct.COMMANDS("NUM 135");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("DNF");
		assertEquals("DNF", ct.getCurRacer().getRaceTime());		// Test DNF
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("DNF");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("ENDRUN");
	}
	
	@Test
	public void testRaceTypePARGRP() {
		System.out.println("       -- TEST PARGRP --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("EVENT PARGRP");
		//test all channels off
		for(Channel c: ct.getChannels()) {
			assertFalse(c.getState());
		}
		
		ct.COMMANDS("TOG 1"); ct.COMMANDS("TOG 2"); ct.COMMANDS("TOG 3"); ct.COMMANDS("TOG 4");
		ct.COMMANDS("TOG 5"); ct.COMMANDS("TOG 6"); ct.COMMANDS("TOG 7"); ct.COMMANDS("TOG 8");
		
		//test all channels on 
		for(Channel c: ct.getChannels()) {
			assertTrue(c.getState());
		}
		
		ct.COMMANDS("CONN PAD 1"); ct.COMMANDS("CONN PAD 2"); ct.COMMANDS("CONN PAD 3"); ct.COMMANDS("CONN PAD 4");
		ct.COMMANDS("CONN PAD 5"); ct.COMMANDS("CONN PAD 6"); ct.COMMANDS("CONN PAD 7"); ct.COMMANDS("CONN PAD 8");
		
		//test to see if all pads are connected
		for(Channel c: ct.getChannels()) {
			assertEquals("PAD",c.getSensor());
		}
		
		// >8 racers
		ct.COMMANDS("NEWRUN");
		
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("NUM 345");
		ct.COMMANDS("NUM 456");
		ct.COMMANDS("NUM 567");
		ct.COMMANDS("NUM 678");
		ct.COMMANDS("NUM 789");
		ct.COMMANDS("NUM 111");
		ct.COMMANDS("NUM 123");	// wont be added to queue
		ct.COMMANDS("NUM 222");	//additional racers after 8 will be ignored
		ct.COMMANDS("NUM 333");
		ct.COMMANDS("TRIG 1");	
		
		assertEquals("345" ,ct.getRaceMap().get(3).getName());			//checking lane 3 which should contain racer 345
		
		assertEquals(10, ct.getRacing().size());			// size of racers racing should be 10 as everything gets same start
		
		//random ordering of triggers to signify different finishes in the lanes
		//also some repeat triggers on channels, which won't affect any other racers still racing
		ct.COMMANDS("TRIG 7");
		assertEquals("789",ct.getFinished().get(0).getName());			//racer 789 in lane 7 finished first and should be in position in our finished list of racers
		
		ct.COMMANDS("TRIG 6");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 4");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 8");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 1");
		assertEquals("123",ct.getFinished().getLast().getName());				//123 in lane 1 finished last and will be last in our finished list 
		ct.COMMANDS("PRINT");
		
		
		
		
		ct.COMMANDS("ENDRUN");
		
		// =8 racers
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 11");
		ct.COMMANDS("NUM 22");
		ct.COMMANDS("NUM 33");
		ct.COMMANDS("NUM 44");
		ct.COMMANDS("NUM 55");
		ct.COMMANDS("NUM 66");
		ct.COMMANDS("NUM 77");
		ct.COMMANDS("NUM 88");
		ct.COMMANDS("TRIG 1");
		
		ct.COMMANDS("TRIG 5");
		ct.COMMANDS("TRIG 8");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 7");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 6");
		ct.COMMANDS("TRIG 4");
		ct.COMMANDS("ENDRUN");

		// <8 racers
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 11");
		ct.COMMANDS("NUM 22");
		ct.COMMANDS("NUM 33");
		ct.COMMANDS("NUM 44");
		ct.COMMANDS("NUM 55");
		ct.COMMANDS("NUM 66");
		ct.COMMANDS("TRIG 1");
		
		//trigger all channels even if lane is empty
		ct.COMMANDS("TRIG 5");
		ct.COMMANDS("TRIG 8");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 7");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 6");
		ct.COMMANDS("TRIG 4");
		ct.COMMANDS("PRINT");
		assertEquals(0,ct.getRaceMap().size());		//raceMap should be empty as all racers have finished
		
		ct.COMMANDS("ENDRUN");
	}
	
	@Test
	public void testCommands() {
		System.out.println("       -- TEST COMMANDS --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("POWER");
		assertFalse(ct.getPower());
		ct.COMMANDS("EXIT");
		ct.COMMANDS("RESET");
		ct.COMMANDS("TIME 1:15:24");
		ct.COMMANDS("TIME abcdefg");
		ct.COMMANDS("TOG 3");
		ct.COMMANDS("TOG 3");
		ct.COMMANDS("TOG 825");
		ct.COMMANDS("CONN GATE 1");
		ct.COMMANDS("CONN 2 PAD");
	}
	
	@Test
	public void testExport() {
		System.out.println("       -- TEST STORAGE --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("NUM 345");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");	
		ct.COMMANDS("PRINT");
		ct.COMMANDS("ENDRUN");				//bib 345 should receive DNF since ENDRUN is called while 345 is still racing
		ct.COMMANDS("EXPORT 1");			//export run 1 to file in JSON format
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("NUM 345");
		ct.COMMANDS("TOG 3");
		ct.COMMANDS("TOG 4");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 4");	
		ct.COMMANDS("TRIG 2");	
		ct.COMMANDS("ENDRUN");				
		ct.COMMANDS("EXPORT 2");			//export run 2 to file in JSON format
		ct.COMMANDS("EXPORT 4");
	}
	
	@Test
	public void testDNF() {
		System.out.println("       -- TEST DNF --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("DNF");												// give racer 123 DNF 
		assertEquals("DNF",ct.getFinished().get(0).getRaceTime());		// racer 123 should have DNF
		assertEquals("123",ct.getFinished().get(0).getName());
		ct.COMMANDS("DNF");												// give racer 234 DNF 
		assertEquals("DNF",ct.getFinished().get(1).getRaceTime());		// racer 234 should have DNF
		assertEquals("234",ct.getFinished().get(1).getName());
	}
	
	//cancel will put the racer who is next to finish back into queue, and first in line to start racing again
	@Test
	public void testCancel() {
		System.out.println("       -- TEST CANCEL --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 1");
		assertEquals("123",ct.getRacing().get(0).getName());			//first to finish is currently 123
		assertEquals("234",ct.getRacing().get(1).getName());
		ct.COMMANDS("CANCEL");
		assertEquals("234",ct.getRacing().get(0).getName());			//first to finish is currently 234
		assertEquals("123",ct.getRun().getRacers().get(0).getName());			//123 is back at start of queue
		ct.COMMANDS("TRIG 1");						//123 starts again
		ct.COMMANDS("TRIG 2");						//234 is first to finish
		ct.COMMANDS("TRIG 2");
		assertEquals("234",ct.getFinished().get(0).getName());			//234 finishes first
		assertEquals("123",ct.getFinished().get(1).getName());
		
	}
	
	//Swap is only valid for IND
	@Test
	public void testSwap() {
		System.out.println("       -- TEST SWAP --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 1");
		assertEquals("123",ct.getRacing().get(0).getName());			//first to finish is currently 123
		assertEquals("234",ct.getRacing().get(1).getName());
		ct.COMMANDS("SWAP");
		assertEquals("234",ct.getRacing().get(0).getName());			//first to finish is now currently 234
		assertEquals("123",ct.getRacing().get(1).getName());
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("PRINT");
		
	}
	@Test
	public void testReset() {
		System.out.println("       -- TEST RESET --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TOG 6");
		ct.COMMANDS("TOG 7");
		ct.COMMANDS("CONN PAD 1");
		ct.COMMANDS("CONN EYE 3");
		ct.COMMANDS("CONN GATE 6");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("RESET");
		assertTrue(ct.getPower());
		assertNull(ct.getRun());
		assertNull(ct.getEvent());
		assertTrue(ct.getRacing().isEmpty());
		assertTrue(ct.getFinished().isEmpty());
		assertTrue(ct.getRaceMap().isEmpty());
		assertEquals(System.currentTimeMillis(),ct.getTime().getTime());			//on reset: time should be equal to system time
		
		for(Channel c: ct.getChannels()) {
			assertFalse(c.getState());
			assertEquals("NONE",c.getSensor());
		}
		
		
	}
	@Test
	public void testTIME() {
		System.out.println("       -- TEST TIME --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("TIME 12:01:01.0");
		assertEquals("12:01:01.0",ct.getTime().getTimeAsString());
		
		ct.COMMANDS("TIME 02:10:30.0");
		assertEquals("02:10:30.0",ct.getTime().getTimeAsString());
		
	}
	
	@Test
	public void testNUM() {
		
	}
	
	/*
	 * Testing gui is essentially same thing as testing the chronotimer more or less 
	 * otherwise we would have to change everything to protected or add getters for each button
	 * 
	 * 
	
	@Test
	public void testGUI() {
		System.out.println("       -- TEST GUI --       ");
		ctGUI = new ChronotimerGUI();
		ctGUI._power.doClick();
		ctGUI._power.doClick();
		ctGUI._power.doClick();												// Turn power on, off, on
		assertTrue(_c.getpower());						// Test power state
		ctGUI._printerPower.doClick();										// Turn on the printer
		ctGUI._functionMenu.doClick(); ctGUI._eventMenu.doClick(); ctGUI._eventIND.doClick();			// Set event
		ctGUI._functionMenu.doClick(); ctGUI._newRun.doClick();				// Start a run
		ctGUI._swap.doClick();												// Test swapping outside of a race
		assertEquals("IND", ctGUI._c._event);			// Test if event was set correctly
		ctGUI._ch1.doClick(); ctGUI._ch2.doClick(); ctGUI._ch3.doClick(); ctGUI._ch4.doClick();	// Activate channels 1-4
		assertTrue(ctGUI._c._channels[3]._state);		// Test if one of the selected channels is indeed active
		ctGUI._kp1.doClick(); ctGUI._kp2.doClick(); ctGUI._kp3.doClick();
		ctGUI._functionMenu.doClick(); ctGUI._num.doClick();
		ctGUI._kp4.doClick(); ctGUI._kp5.doClick(); ctGUI._kp6.doClick();
		ctGUI._functionMenu.doClick(); ctGUI._num.doClick();				// Add in two racers
		ctGUI._b1.doClick();
		ctGUI._b3.doClick();												// Start the race
		ctGUI._swap.doClick();												// Test swapping during a race
		ctGUI._functionMenu.doClick(); ctGUI._print.doClick();				// Print the current status of the race
		assertFalse(ctGUI._c._racing.isEmpty());		// Test for activity in racing queue
		ctGUI._b2.doClick();
		ctGUI._b4.doClick();												// Finish the race
		assertTrue(ctGUI._c._racing.isEmpty());			// Test for emptiness in racing queue
		ctGUI._endRun.doClick();											// End the race
		ctGUI._functionMenu.doClick(); ctGUI._export.doClick();				// Export the results
	}

	*/
	
}