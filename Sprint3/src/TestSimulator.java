import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.JTextArea;


//Class used to utilize simulator to read file and then quickly execute commands and 
//generate output.
//can also be used to construct files that input values that try to crash the system.


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

	//sprint3.txt doesnt test much as we are switching to gui at this point 
	//same as sprint2.txt for the most part
	//TODO write file to properly test sprint 3
	@Test
	public void testSprint3() {
		s.readFile("sprint3.txt", new Chronotimer(new JTextArea()));
		
	}	
	@Test
	public void testSprint4() {
		//s.readFile("CTS1RUN2.txt", new Chronotimer(cg.getPrinterTextArea()));
		//TODO write file to test sprint4 using simulator
		
	}
}
