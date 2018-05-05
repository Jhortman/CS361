import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // create a context to get the request to display the results
        server.createContext("/results", new DisplayHandler());
        
        // create a context on where to store our css file for when our html file needs it
        server.createContext("/results/style.css", new CSSHandler());

        // create a context to get the request for the POST
        server.createContext("/sendresults",new PostHandler());
        server.setExecutor(null); // creates a default executor

        // get it going
        System.out.println("Starting Server...");
        server.start();
        
        dir = new Directory();
    }

    static class CSSHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String update = "";                  
            update = dir.getCSS();			//read style.css and write it out to /displayresults/style.css which will be used by the html at /displayresults
            // write out the response
            t.sendResponseHeaders(200, update.length());
            OutputStream os = t.getResponseBody();
            os.write(update.getBytes());
            os.close();
        }
    }
    
    static class DisplayHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String update = "";
                         
            //get html string generated dynamically with our directory contents and write it out to /displayresults
            update = dir.toHTML();
            // write out the response
            t.sendResponseHeaders(200, update.length());
            OutputStream os = t.getResponseBody();
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
            String postResponse = "ROGER MESSAGE RECEIVED";
            
            System.out.println("response: " + sharedResponse);
            
           	dir.add(sharedResponse);	//add to directory on server if command is add
            dir.getRealNames();
           
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
    