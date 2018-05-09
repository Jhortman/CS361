import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import org.junit.Test;

public class TestSprint4 {
	private Chronotimer ct = new Chronotimer(new JTextArea());
	
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
		assertEquals("789",ct.getFinished().get(0).getName());			//racer 789 in lane 7 finished first and should be in position 1 in our finished list of racers
		
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
		
		// <8 racers
		ct.COMMANDS("EVENT PARGRP");
		ct.COMMANDS("NEWRUN");
		ct.COMMANDS("NUM 11");
		ct.COMMANDS("NUM 22");
		ct.COMMANDS("NUM 33");
		ct.COMMANDS("NUM 44");
		ct.COMMANDS("NUM 55");
		ct.COMMANDS("NUM 66");
		ct.COMMANDS("TRIG 1");
		assertTrue(ct.getRun().getRacers().isEmpty());	//all in queue to race should have been popped
		assertEquals(6,ct.getRacing().size());			//6 racers racing
		assertEquals(6,ct.getRaceMap().size());			//6 racers in lanes, after channel trig 1 all should be racing
		assertEquals("11",ct.getRaceMap().get(ct.getChannels()[0].getNum()).getName());			//using channel number access associated lane's racer in this case racer should be 11 in lane 1
		assertEquals("44",ct.getRaceMap().get(ct.getChannels()[3].getNum()).getName());			//check whos in lane 4
		
		
		//trigger all channels even if lane is empty
		ct.COMMANDS("TRIG 5");
		assertEquals("55",ct.getFinished().getFirst().getName());			//55 in lane 5 wins 1st				
		ct.COMMANDS("TRIG 8");												//no one in lane 8
		assertEquals(1,ct.getFinished().size());							//size should still be 0
		ct.COMMANDS("TRIG 2");
				
		ct.COMMANDS("TRIG 7");
		ct.COMMANDS("TRIG 3");
		assertEquals("33",ct.getFinished().get(2).getName());			//33 in lane 3 wins 3rd		
		ct.COMMANDS("TRIG 1");
		ct.COMMANDS("TRIG 6");
		ct.COMMANDS("TRIG 4");
		assertEquals("44",ct.getFinished().getLast().getName());			//44 in lane 4 wins 6th Place (Last Place)	
		ct.COMMANDS("PRINT");
		assertEquals(0,ct.getRaceMap().size());		//raceMap should be empty as all racers have finished
				
		ct.COMMANDS("ENDRUN");
		
		// =8 racers
		ct.COMMANDS("EVENT PARGRP");
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

		
	}
	
	
}
