import org.junit.Test;


import static org.junit.Assert.*;
import javax.swing.JTextArea;

public class Testing {
	private Chronotimer ct = new Chronotimer(new JTextArea());
	private ChronotimerGUI ctGUI;
	
	@Test
	public void testCommands() {
		System.out.println("       -- TEST COMMANDS --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("POWER");
		assertFalse(ct.getPower());
		ct.COMMANDS("RESET");
		ct.COMMANDS("POWER");
		assertTrue(ct.getPower());
		ct.COMMANDS("TIME 01:15:24.0");
		assertEquals("01:15:24.0", ct.getTime().getTimeAsString());
		ct.COMMANDS("TOG 3");
		assertTrue(ct.getChannels()[2].getState());
		ct.COMMANDS("TOG 3");
		assertFalse(ct.getChannels()[2].getState());
		ct.COMMANDS("TOG 825");
		ct.COMMANDS("CONN GATE 1");
		assertEquals("GATE",ct.getChannels()[0].getSensor());
		ct.COMMANDS("CONN NONE 2");								//nothing happens dont change sensor
		assertEquals("NONE",ct.getChannels()[1].getSensor());	//sensor should still be default---> NONE
		
	}
	
	
	
	//test Tme
	@Test
	public void testTIME() {
		System.out.println("       -- TEST TIME --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("TIME 12:01:01.0");
		assertEquals("12:01:01.0",ct.getTime().getTimeAsString());
		
		ct.COMMANDS("TIME 02:10:30.0");
		assertEquals("02:10:30.0",ct.getTime().getTimeAsString());
		
		
	}
	
	
	// test adding 1000 racers to queue
	@Test
	public void testAddingMany(){
		System.out.println("       -- TEST ADDING 1000 RACERS --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		String num = "";
		for(int i = 0;i < 1000; i++){
			num = "" + i ;
			ct.COMMANDS("NUM " + num);
			assertEquals(num ,ct.getRun().getRacers().get(i).getName());
		}
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TOG 2");
		for(int i = 0;i < 1000; i++){
			ct.COMMANDS("TRIG 1");
		}
		for(int i = 0;i < 1000; i++){
			assertEquals("" + i ,ct.getRacing().get(i).getName());
		}
		for(int i = 0;i < 1000; i++){
			ct.COMMANDS("TRIG 2");
		}
		for(int i = 0;i < 1000; i++){
			assertEquals("" + i ,ct.getFinished().get(i).getName());
		}
		ct.COMMANDS("PRINT");
		ct.COMMANDS("ENDRUN");
		ct.COMMANDS("EXPORT 1");
		
		
	}
	
	
	@Test
	public void testGUI() {
		ctGUI = new ChronotimerGUI();
		System.out.println("       -- TEST GUI --       ");
		ctGUI._power.doClick();
		ctGUI._power.doClick();
		ctGUI._power.doClick();												// Turn power on, off, on
		assertTrue(ctGUI.getCT().getPower());						// Test power state
		ctGUI._printerPower.doClick();										// Turn on the printer
		ctGUI._functionMenu.doClick(); ctGUI._eventMenu.doClick(); ctGUI._eventIND.doClick();			// Set event
		ctGUI._functionMenu.doClick(); ctGUI._newRun.doClick();				// Start a run
		ctGUI._swap.doClick();												// Test swapping outside of a race
		assertEquals("IND", ctGUI.getCT().getEvent());			// Test if event was set correctly
		ctGUI._ch1.doClick(); ctGUI._ch2.doClick(); ctGUI._ch3.doClick(); ctGUI._ch4.doClick();	// Activate channels 1-4
		assertTrue(ctGUI.getCT().getChannels()[3].getState());		// Test if one of the selected channels is indeed active
		ctGUI._kp1.doClick(); ctGUI._kp2.doClick(); ctGUI._kp3.doClick();
		ctGUI._functionMenu.doClick(); ctGUI._num.doClick();
		ctGUI._kp4.doClick(); ctGUI._kp5.doClick(); ctGUI._kp6.doClick();
		ctGUI._functionMenu.doClick(); ctGUI._num.doClick();				// Add in two racers
		ctGUI._b1.doClick();
		ctGUI._b3.doClick();												// Start the race
		ctGUI._swap.doClick();												// Test swapping during a race
		ctGUI._functionMenu.doClick(); ctGUI._print.doClick();				// Print the current status of the race
		assertFalse(ctGUI.getCT().getRacing().isEmpty());		// Test for activity in racing queue
		ctGUI._b2.doClick();
		ctGUI._b4.doClick();												// Finish the race
		assertTrue(ctGUI.getCT().getRacing().isEmpty());			// Test for emptiness in racing queue
		ctGUI._endRun.doClick();											// End the race
		ctGUI._kp1.doClick();
		ctGUI._functionMenu.doClick(); ctGUI._export.doClick();				// Export the results
	}

	
	
}