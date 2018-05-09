import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import org.junit.Test;

public class TestSprint3 {
	private Chronotimer ct = new Chronotimer(new JTextArea());
	
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
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");
		ct.COMMANDS("TRIG 2");;
		ct.COMMANDS("TRIG 2");
		ct.inputGrpFinish("85");		
		assertEquals("85", ct.getFinished().get(0).getName());			//85 should be in position 1
		ct.inputGrpFinish("165");						//not in race wont do anything
		ct.inputGrpFinish("16");
		ct.inputGrpFinish("300");
		assertEquals("300", ct.getFinished().get(2).getName());			//300 should in be in position 3
		ct.inputGrpFinish("300");						//nothing happens with duplicates already removed from tempFinished queue
		ct.inputGrpFinish("85");
		ct.inputGrpFinish("5");
		ct.inputGrpFinish("91");
		ct.inputGrpFinish("135");
		ct.inputGrpFinish("4");
		assertEquals("4", ct.getFinished().get(6).getName());			//4 should in be in position 7
		ct.inputGrpFinish("21");
		ct.COMMANDS("ENDRUN");							//anyone not inputed at end of run receive DNF
		assertTrue(ct.getRacing().isEmpty());	
		assertTrue(ct.getFinished().isEmpty());			//racing and finished queues should be empty after a run ends
	}
	
	//Swap is only valid for IND
		@Test
		public void testSwap() {
			System.out.println("       -- TEST SWAP --       ");
			ct.COMMANDS("POWER");
			ct.COMMANDS("SWAP");				//can't swap with no run active
			ct.COMMANDS("EVENT IND");
			ct.COMMANDS("NEWRUN");
			ct.COMMANDS("SWAP");				//can't swap with no runners
			ct.COMMANDS("NUM 123");
			ct.COMMANDS("NUM 234");
			ct.COMMANDS("SWAP");				//can't swap with no runners racing
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
			ct.COMMANDS("ENDRUN");				//can't swap with no runners racing
			ct.COMMANDS("EVENT PARIND");
			ct.COMMANDS("NEWRUN");
			ct.COMMANDS("NUM 123");
			ct.COMMANDS("NUM 234");
			ct.COMMANDS("TRIG 1");
			ct.COMMANDS("TRIG 1");
			ct.COMMANDS("SWAP");											//can't swap in any other race other than IND
			assertEquals("123",ct.getRacing().get(0).getName());			//first to finish is still currently 123
			assertEquals("234",ct.getRacing().get(1).getName());
			ct.COMMANDS("PRINT");
			ct.COMMANDS("ENDRUN");				//can't swap with no runners racing
		}
		
}
