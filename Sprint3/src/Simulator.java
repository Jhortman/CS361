import java.util.Scanner;

import javax.swing.JTextArea;

import java.io.File;
import java.io.FileNotFoundException;


public class Simulator extends Time{

	public static void main(String[] args){
		Chronotimer ct = new Chronotimer(new JTextArea());
		simulate(ct);
	}
	// Keyboard input method
	public static void simulate(Chronotimer ct){
		
		String input = "";
		String time;
		 
		Scanner stdIn = new Scanner(System.in);
		
		//I went ahead and appended system time stamps for most of the actions that occur in the chronoTimer
		// just for added clarity. The notable ones I excluded are power and exit. 
		
		System.out.print("Press Enter or give <fileName>.txt: ");
		input = stdIn.nextLine();
		if (!input.contains(".txt")) {						//if no file tag then give commands manually
			
			
			while(!input.equalsIgnoreCase("EXIT")) {
					System.out.print("Enter Command: ");
					input = stdIn.nextLine();
					ct.COMMANDS(input);
				} 
		}
		else {
			
			try {
				Scanner sc = new Scanner(new File(input));
				String tokens[];
				
				while (sc.hasNext()){
					input = sc.nextLine();				
					tokens = input.split("\t");				//split input into two tokens from tab 
					time = tokens[0];						//
					setTime(time);							//store time in time class
				
					if(tokens[1].toUpperCase().equals("EXIT")){		//if parse in exit just break and let program terminate
						break;
					}
					
					ct.COMMANDS(tokens[1]);
				}
				
				sc.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("no such file found");
				}
		
		}
		stdIn.close();
		
		
	}
	
	
	
	
	public void readFile(String fileIn, Chronotimer ct) {
		
		String input = "";
		String time;
		try {
			Scanner sc = new Scanner(new File(fileIn));
			String tokens[];
			
			while (sc.hasNext()){
				input = sc.nextLine();				
				tokens = input.split("\t");				//split input into two tokens from tab 
				time = tokens[0];						//
				setTime(time);							//store time in time class
			
				if(tokens[1].toUpperCase().equals("EXIT")){		//if parse in exit just break and let program terminate
					break;
				}
				
				ct.COMMANDS(tokens[1]);
			}
			
			sc.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("no such file found");
		}
		
	}
	
	
}
		