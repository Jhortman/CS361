import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JTextArea;

public class Testing {
	private Chronotimer ct = new Chronotimer(new JTextArea());
	private ChronotimerGUI ctGUI;
	
	@Test
	public void testRaceTypeIND() {
		System.out.println("       -- TEST IND --       ");
		ct.COMMANDS("POWER");
		assertTrue(ct.getPower());
		assertNull(ct._event);
		ct.COMMANDS("EVENT IND");
		assertEquals("IND", ct._event);
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("NUM 135");
		ct.COMMANDS("TRIG 1");
		assertFalse(ct._racing.isEmpty());
		ct.COMMANDS("TRIG 2");
		assertTrue(ct._racing.isEmpty());
		ct.COMMANDS("ENDRUN");
		assertNotNull(ct._event);
		assertTrue(ct._channels[1]._state);
	}
	
	@Test
	public void testRaceTypePARIND() {
		System.out.println("       -- TEST PARIND --       ");
		ct.COMMANDS("POWER");
		assertEquals(null, ct._event);
		ct.COMMANDS("EVENT PARIND");
		ct.COMMANDS("NEWRUN");
		assertTrue(ct._racing.isEmpty());
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TOG 3");
		ct.COMMANDS("TOG 4");
		assertTrue(ct._channels[1]._state);
		assertTrue(ct._channels[2]._state);
		ct.COMMANDS("NUM 942");
		ct.COMMANDS("NUM 237");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 2");
		assertFalse(ct._racing.isEmpty());
		ct.COMMANDS("DNF");
		assertTrue(ct._racing.isEmpty());
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
		assertEquals("DNF", ct.getCurrRacer().getRaceTime());		// Test DNF
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
		ct.COMMANDS("TOG 1"); ct.COMMANDS("TOG 2"); ct.COMMANDS("TOG 3"); ct.COMMANDS("TOG 4");
		ct.COMMANDS("TOG 5"); ct.COMMANDS("TOG 6"); ct.COMMANDS("TOG 7"); ct.COMMANDS("TOG 8");
		ct.COMMANDS("CONN PAD 1"); ct.COMMANDS("CONN PAD 2"); ct.COMMANDS("CONN PAD 3"); ct.COMMANDS("CONN PAD 4");
		ct.COMMANDS("CONN PAD 5"); ct.COMMANDS("CONN PAD 6"); ct.COMMANDS("CONN PAD 7"); ct.COMMANDS("CONN PAD 8");
		
		// <8 racers
		ct.COMMANDS("NEWRUN");
		
		ct.COMMANDS("NUM 11");
		ct.COMMANDS("NUM 22");
		ct.COMMANDS("NUM 33");
		ct.COMMANDS("NUM 44");
		ct.COMMANDS("NUM 55");
		ct.COMMANDS("TRIG 1");
		
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 4");
		ct.COMMANDS("TRIG 5");
		ct.COMMANDS("TRIG 6");
		ct.COMMANDS("TRIG 7");
		ct.COMMANDS("TRIG 8");
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

		// >8 racers
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 11");
		ct.COMMANDS("NUM 22");
		ct.COMMANDS("NUM 33");
		ct.COMMANDS("NUM 44");
		ct.COMMANDS("NUM 55");
		ct.COMMANDS("NUM 66");
		ct.COMMANDS("NUM 77");
		ct.COMMANDS("NUM 88");
		ct.COMMANDS("NUM 99");
		ct.COMMANDS("NUM 111");
		ct.COMMANDS("NUM 222");
		ct.COMMANDS("TRIG 1");
		
		ct.COMMANDS("TRIG 5");
		ct.COMMANDS("TRIG 8");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 7");
		ct.COMMANDS("TRIG 3");
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 6");
		ct.COMMANDS("TRIG 4");
		ct.COMMANDS("PRINT");
		
		ct.COMMANDS("ENDRUN");
	}
	
	@Test
	public void testCommands() {
		System.out.println("       -- TEST COMMANDS --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("POWER");
		assertFalse(ct.power);
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
	public void testStorage() {
		System.out.println("       -- TEST STORAGE --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("EXPORT");
	}
	
	@Test
	public void testGUI() {
		System.out.println("       -- TEST GUI --       ");
		ctGUI = new ChronotimerGUI();
		ctGUI._power.doClick();
		ctGUI._power.doClick();
		ctGUI._power.doClick();												// Turn power on, off, on
		assertTrue(ctGUI._c.power);						// Test power state
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

	/*
	@Test
	public void testGrp() {
		
		
		//tests done to mimick sprint3.txt
		//ct.COMMANDS("power");
		
		
		//check time
		//ct.COMMANDS("TIME 12:01:30.0");
		//assertEquals("12:01:30.0", Time.getTimeAsString());
		
		//check if anyruns are active
		
		//assertEquals(null, ct._curRun );
		
		
		
		
	
	}
	*/
}