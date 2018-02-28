import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Simulator {

	// Keyboard input method
	public static void main(String[] args) throws IOException {
		
	//	if (args[1].equals("")) {
		boolean a = true;
		if (a) {
			Scanner stdIn = new Scanner(System.in);
			Chronotimer ct = new Chronotimer();
			ct.start(stdIn);
			stdIn.close();
		} else {
			
			try (Scanner sc = new Scanner(new File(args[0]))) {
				simulate(sc);
				
			} catch (FileNotFoundException e) {
				System.out.println(args[0] + " - no such file found");
			}
		}
	}
	
	
	// Text file reading method
	public static void simulate(Scanner sc) throws IOException{
		Chronotimer ct = new Chronotimer();
		String newLine;
		String[] tokens;
		String[] tokens2;
		String time;
		String command;
		
		while (sc.hasNextLine())
		{
			newLine = sc.nextLine();
			tokens = newLine.split("\t");			
			tokens2 = tokens[1].split(" ");
			
			time = tokens[0];
			command = tokens2[0];
			if (tokens2[1] != null) { String command2 = tokens2[1]; }
			if (tokens2[2] != null) { String command3 = tokens2[2]; }
			
			ct.newTime(time);
			
			switch (command) {
				case "POWER":
				case "EXIT":
					sc.close();
					System.exit(0);
				case "RESET":
				case "TIME":
				case "DNF":
				case "CANCEL":
				case "TOG":
				case "TRIG":
				case "START":
				case "FINISH":
				default:
					System.out.println("Command not recognized");
			}

		}
		
		System.out.println("!! End of file reached without executing an EXIT command !!");
		sc.close();
		System.exit(0);
	}
}