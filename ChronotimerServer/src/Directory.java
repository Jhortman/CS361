import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
public class Directory {

	private ArrayList<Storage> dir;

	public Directory() {
		dir = new ArrayList<>();
	}
	public ArrayList<Storage> getDir(){
		return dir;
	}
	
	//method for adding JSON string into directory
	public void add(String JSON) {
		try{
			Gson gson=new GsonBuilder().create();

			Storage s = gson.fromJson(JSON,Storage.class);
			dir.add(s);
		}catch(JsonSyntaxException ex){
			System.out.println("Error in syntax occured.");
		}
			
		}
	
	//build up our html string to get ready to write it to the browser at /displayresults
	public String toHTML() {
		
		String html = "";
		
		html += "<!DOCTYPE html>\n"
				+ "<html>\n<head>\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"results/style.css\">\n"		
				+ "</head>\n"
				+ "<body>\n<h2> Run " + dir.get(dir.size()-1).getRun().getRunNum() + " Event " + dir.get(dir.size()-1).getRun().getCurEvent() + "</h2>\n"
				+ "<table>\n<caption></caption>\n<tr>\n"
				+ "<th>Place</th>\n<th>Number</th>\n<th>Name</th>\n<th>Time</th></tr>\n";
			
		//filling our html table with racing results
		for (int i = 0; i < dir.get(dir.size() -1).getFinish().size();i++) {
			html += "<tr>\n"
				 + "<td>" + (i+1) + "</td>\n" 
				 + "<td>" + dir.get(dir.size() -1).getFinish().get(i).getName() + "</td>\n"
				 + "<td>" + dir.get(dir.size() -1).getFinish().get(i).getRealName() + "</td>\n"
				 + "<td>" + dir.get(dir.size() -1).getFinish().get(i).getRaceTime() + "</td>\n"
				 + "</tr>\n";
		}
		
		html += "</table>\n</body>\n</html>";
		
		
		return html;
		
	}
	
	//compiling our style.css file into one big string to hand over to the server to stash it at /results/style.css 
	//in order for our html code to find and access it
	public String getCSS() {
		String css = "";
		
		try {
			Scanner sc = new Scanner(new File("style.css"));  
			
			while (sc.hasNext()){
				css += sc.nextLine() + "\n";		
			}
			
			sc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
		return css;
	}
	public void clear() {
		dir.clear();
	}
	//read from file to get real names assign to bib numbers
	//format of file is assumed to be of such(separated by tab char):
	//123	Dale Snowben
	//234	ed flanders
	public void getRealNames() {
		HashMap<Integer,String> nameMap = new HashMap<Integer,String>();
		String input = "";
		String tokens[];
		try {
			Scanner sc = new Scanner(new File("racers.txt"));  //try to read our racer.txt file to assign real names to bibNum which we inconveniently call name everywhere else :)
			while (sc.hasNext()){
				input = sc.nextLine();				
				tokens = input.split("\t");				//split racers.txt into two tokens by tab character
				nameMap.put(Integer.parseInt(tokens[0].trim()), tokens[1]);								
			}
			sc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
		
		for(Racer r: dir.get(dir.size()-1).getFinish()) {		//go through most recently stored run in storage for grabbing racers
			if(nameMap.containsKey(Integer.parseInt(r.getName()))) {
				r.setRealName(nameMap.get(Integer.parseInt(r.getName())));			//if key is found in hashmap retrieve the real name associated with that bibNum that was stored as keys in the map and assign it to the racer.
			}
			else {
				r.setRealName("");
			}
		}
		
		
	}
	
}