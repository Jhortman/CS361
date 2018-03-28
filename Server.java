/**
 * Simple HTTP handler off using provided template in lab8 
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    // a shared area where we get the POST data and then use it in the other handler
    static String sharedResponse = "";
    static boolean gotMessageFlag = false;
    static Directory dir;

    public static void main(String[] args) throws Exception {

        // set up a simple HTTP server on our local host
        HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);

        // create a context to get the request to display the results
        server.createContext("/displayresults", new DisplayHandler());

        // create a context to get the request for the POST
        server.createContext("/sendresults",new PostHandler());
        server.setExecutor(null); // creates a default executor

        // get it going
        System.out.println("Starting Server...");
        server.start();
        dir = new Directory();
    }

    static class DisplayHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            String response = "Begin of response\n";
			/*Gson g = new Gson();
			// set up the header
            System.out.println(response);
			try {
				if (!sharedResponse.isEmpty()) {
					System.out.println(response);
					ArrayList<Employee> fromJson = g.fromJson(sharedResponse,
							new TypeToken<Collection<Employee>>() {
							}.getType());

					System.out.println(response);
					response += "Before sort\n";
					for (Employee e : fromJson) {
						response += e + "\n";
					}
					Collections.sort(fromJson);
					response += "\nAfter sort\n";
					for (Employee e : fromJson) {
						response += e + "\n";
					}
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
			
            response += "End of response\n";
             */
            
            
            
            String update = "";
            
            for(Employee emp  : dir.getDir()){
            	update += emp.toString() + "\n";
            }
            
            System.out.println(response);
           
            // write out the response
            t.sendResponseHeaders(200, update.length());
            OutputStream os = t.getResponseBody();
            //os.write(response.getBytes());
            os.write(update.getBytes());
            os.close();
        }
    }

    static class PostHandler implements HttpHandler {
        public void handle(HttpExchange transmission) throws IOException {

            //  shared data that is used with other handlers
            sharedResponse = "";
            
              
            
            

            // set up a stream to read the body of the request
            InputStream inputStr = transmission.getRequestBody();

            // set up a stream to write out the body of the response
            OutputStream outputStream = transmission.getResponseBody();

            // string to hold the result of reading in the request
            StringBuilder sb = new StringBuilder();

            // read the characters from the request byte by byte and build up the sharedResponse
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }

            // create our response String to use in other handler
            sharedResponse = sharedResponse+sb.toString();

            // respond to the POST with ROGER
            String postResponse = "ROGER JSON RECEIVED";

            System.out.println("response: " + sharedResponse);
            
            String tokens[];
            tokens = sharedResponse.split(" ");
            
            String command = "";
            
            command = tokens[0];  // contains add
            System.out.println(command);
            sharedResponse = tokens[1];
            System.out.println(tokens[1]);
            System.out.println(sharedResponse);
            
            if(command.equals("ADD")){
            	
               	dir.add(sharedResponse);
            }
            //todo 
           else if(command.equals("PRINT")){
               	dir.print();
            }
            
            else if(command.equals("CLEAR")){
            	
               	dir.clear();
            }
            //Desktop dt = Desktop.getDesktop();
            //dt.open(new File("raceresults.html"));

            // assume that stuff works all the time
            transmission.sendResponseHeaders(300, postResponse.length());

            // write it and return it
            outputStream.write(postResponse.getBytes());

            outputStream.close();
        }
    }

}