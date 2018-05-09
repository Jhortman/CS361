import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import org.junit.Test;

public class TestSprint2 {
	private Chronotimer ct = new Chronotimer(new JTextArea());
	
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
		assertEquals("942",ct.getFinished().get(0).getName());	//first to finish was racer 942
		assertEquals("237",ct.getRacing().get(0).getName());	//next racer to finish is racer 237
		assertFalse(ct.getRacing().isEmpty());					//should still have racers racing
		ct.COMMANDS("DNF");
		assertTrue(ct.getRacing().isEmpty());					//should be no more racers racing
		ct.COMMANDS("ENDRUN");
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
		ct.COMMANDS("EXPORT 4");			//nothing to export
	}
}
