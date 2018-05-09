import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.JTextArea;

//Testing files given to us on D2l




//Class can used to utilize simulator to read files and execute commands to
//quickly generate output or test values.


public class TestSimulator {
	
	Simulator s = new Simulator();
	
	
	@Test
	public void testSprint1RUN1() {
		s.readFile("CTS2RUN1.txt", new Chronotimer(new JTextArea()));
		
	}
	
	@Test
	public void testSprint1RUN2() {
		s.readFile("CTS1RUN2.txt", new Chronotimer(new JTextArea()));
		
	}
	@Test
	public void testSprint2() {
		s.readFile("sprint2.txt", new Chronotimer(new JTextArea()));
		
	}
}