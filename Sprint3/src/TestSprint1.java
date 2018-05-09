import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import org.junit.Test;

public class TestSprint1 {

	private Chronotimer ct = new Chronotimer(new JTextArea());
	
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
	public void testNUM() {
		System.out.println("       -- TEST NUM --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NUM 123");								//should not be able to add racers right now
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 234");	
		ct.COMMANDS("NUM 999990");	
		assertEquals(1,ct.getRun().getRacers().size());			//size should only be 1
		assertEquals("234",ct.getRun().getRacers().get(0).getName());		//if size == 1 then only racer in there should be 234

	}
	
	@Test 
	public void testTOG(){
		System.out.println("       -- TEST TOG --       ");
		ct.COMMANDS("POWER");							//on initial system power up channels should be disarmed
		for(Channel c: ct.getChannels()) {			
			assertFalse(c.getState());					//check if channels are disarmed
		}
		ct.COMMANDS("POWER");							//POWER OFF
		for(int i = 1; i <= 8 ;i++){					
			ct.COMMANDS("TOG " + i); 					//try to TOG channels (power is off state should not change)
		}
		ct.COMMANDS("POWER");							//POWER ON
		
		for(Channel c: ct.getChannels()) {				
			assertFalse(c.getState());					//check if channels are still disarmed 
		}
		
		for(int i = 1; i <= 8 ;i++){					
			ct.COMMANDS("TOG " + i); 					//try to TOG channels (power is on, state should change)
		}
		
		for(Channel c: ct.getChannels()) {				
			assertTrue(c.getState());					//check if channels are now armed
		}
		
	}
	
	@Test 
	public void testTRIG(){
		System.out.println("       -- TEST TRIG --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("EVENT PARIND");
		assertEquals("PARIND",ct.getEvent());
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("TOG 1");				
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TOG 3");
		ct.COMMANDS("TOG 4");	
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("TOG 1");				//channel 1 now off
		ct.COMMANDS("TRIG 1");				//nothing should happen
		assertTrue(ct.getRacing().isEmpty());	//no one should be racing
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("TRIG 1");	
		assertFalse(ct.getRacing().isEmpty());		//123 should be racing
		ct.COMMANDS("TRIG 3");						//234 should be racing
		ct.COMMANDS("TRIG 2");						//123 should have finished
		assertEquals("123",ct.getFinished().getFirst().getName()); //123 should now have a recorded finish time and therefore RaceTime
		ct.COMMANDS("TOG 4");
		ct.COMMANDS("TRIG 4");						//attempt to finish 234 but ch 4 is disarmed now
		assertEquals("234",ct.getRacing().getFirst().getName()); //234 should still be racing
		ct.COMMANDS("TOG 4");
		ct.COMMANDS("TRIG 4");
		assertEquals("234",ct.getFinished().get(1).getName());	//234 should now have finished
		assertEquals(2,ct.getFinished().size());			//2 people should be in finished queue
		
	}
	
	@Test 
	public void testSTART(){
		System.out.println("       -- TEST START --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("START");					// no runners added to race yet
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TOG 3");					//channel 3 armed
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("START");					//channel 1 not armed---> nothing happens
		assertTrue(ct.getRacing().isEmpty());	//no one should be racing
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("START");
		assertEquals(ct.getTime().toHMSString(System.currentTimeMillis()),ct.getRacing().getFirst().getStart());			//current system time should now be 123's startTime
		assertEquals("123",ct.getRacing().getFirst().getName()); //123 should now have a recorded start time and be racing
		ct.COMMANDS("START");
		assertEquals("234",ct.getRacing().get(1).getName()); //234 should now have a recorded start time and be racing
		
	}
	
	@Test 
	public void testFINISH(){
		System.out.println("       -- TEST FINISH --       ");
		ct.COMMANDS("POWER");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("START");					// no runners added to race yet
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("NUM 123");
		ct.COMMANDS("NUM 234");
		ct.COMMANDS("START");					//channel 1 not armed---> nothing happens
		assertTrue(ct.getRacing().isEmpty());	//no one should be racing
		ct.COMMANDS("TOG 1");
		ct.COMMANDS("START");
		assertEquals("123",ct.getRacing().getFirst().getName()); //123 should now have a recorded start time and be racing
		ct.COMMANDS("START");
		assertEquals("234",ct.getRacing().get(1).getName()); //123 should now have a recorded start time and be racing
		ct.COMMANDS("FINISH");
		ct.COMMANDS("FINISH");								//channel 2 is off nothing should happen
		ct.COMMANDS("TOG 4");													
		ct.COMMANDS("FINISH");								//finish doesn't care about channel 4
		assertTrue(ct.getFinished().isEmpty());				//no one should have finished yet
		ct.COMMANDS("TOG 2");
		ct.COMMANDS("FINISH");
		assertEquals(ct.getTime().toHMSString(System.currentTimeMillis()),ct.getFinished().getFirst().getFinish());			//current system time should now be 123's FinishTime
		assertEquals("123",ct.getFinished().getFirst().getName()); //123 should now have a recorded finish time and therefore RaceTime
		ct.COMMANDS("FINISH");
		assertEquals("234",ct.getFinished().get(1).getName()); //234 should now have a recorded finish time and be finished
	}
	
	
}
