package com.main.net;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONTokener;

/**
 * Utility that downloads from http addresses
 * 
 * @see
 * {@link http://www.mkyong.com/java/java-httpurlconnection-follow-redirect-example/}
 * {@link  http://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url}
 * @author brendankellam
 * @version 1.1
 */
public class HttpDownload implements Runnable{

	//The URL to the download link
	URL url;

	//the destination of where the file will go
	String dest;
	
	//buffer size
	private static final int BUFFER_SIZE = 4096;
	
	//the downloader thread
	Thread download;

	
	//Constructor. Accepts: the url to the desired download, the destination of the download
	public HttpDownload(String url, String destination){
		
		//initialize the variables
		init(url, destination);

		//create a new thread inside this that will commence the download
		download = new Thread(this, "HttpDownload");
		
		//start the download
		download.start();
	}
	
	private void init(String url, String destination){ //initializes the inputed information before file download. Accepts: url of download, destination of download
		
		//try:catch block for safety from malformed urls
		try {
			//initialize the url of download link
			this.url = new URL(url);
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		//initialize the file destination
		this.dest = destination;		
	}
	
	
	public void run() { //run method, will essentially run the download
		download();
	}
	
	public void download(){ //downloads the file from the http address

		//try:catch block used for safety with errors while reading files
		try {
			
			//create a new connection to the host
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						
			//set a timeout of 5s
			connection.setReadTimeout(5000);
			
			//get the response from the host
			int status = connection.getResponseCode();
			
			//if the response is not a 401 OK, continue
			if (status != HttpURLConnection.HTTP_OK) {
				
				//checks if the response is a 3xx return, i.e a redirect 
				if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER){
					
					//if the url is a redirection, get the new re-directed url
					String redirect_url = connection.getHeaderField("Location");

					//re-open the connnection with the new url
					connection = (HttpURLConnection) new URL(redirect_url).openConnection();
				}
			}
			
			// opens input stream from the connection
			InputStream input_stream = connection.getInputStream();
			
			//gets the disposition string (i.e: the http response to a file download. Looks like: attachment;filename="(filename)"
			String disposition = connection.getHeaderField("Content-Disposition");
			
			//create a new String that will store the filename of the download
			String file_name = "";
			
			//if the disposition string is not null, continue
			if(disposition != null){
			
				//get the index where "filename=" is located at
				int index = disposition.indexOf("filename=");
				
				//if there is a filename
				if(index > 0){
					
					//append to the file_name string by removing first and last characters from disposition
					file_name = disposition.substring(index + "filename=\"".length(), disposition.length()-1);
				}
			
			}
			
			//edit the destination to add the file name
			dest = dest + "/" + file_name;
			
			//create a new output stream allowing file to be writen
			FileOutputStream output_stream = new FileOutputStream(dest);
			
			//total number of bytes read of the file
			int bytesRead = -1;
			
			//create a new buffer to hold the file 
			byte[] buffer = new byte[BUFFER_SIZE];
			
			//loops through the buffer, reading each byte
			while ( (bytesRead = input_stream.read(buffer)) != -1) {
				
				//writes the data with a offset of 0
				output_stream.write(buffer, 0, bytesRead);
			}
			
			//close both input and output streams 
			input_stream.close();
			output_stream.close();
			
			System.out.println("file downloaded");
			
			//stop the download thread
			end_download();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public synchronized void end_download(){ //stops the download thread
		
		//end the current task being preformed by the Thread
		download.interrupt();
		
	}

}


/*
public class HttpDownload {

	   public void getHTML(String urlToRead) {
		   
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line + "\n";
	         }
	         rd.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      System.out.println(result);
	      
		   
	   }

	   public static void main(String args[])
	   {
	     HttpDownload c = new HttpDownload();
	     String request = "GET /v1/vehicles/?user_key=9e64c08a89aa589abc0c88ca0e819947&make=chevrolet&model=aveo&year=2014 HTTP/1.1"
	    		 + "\nHost: api.wheel-size.com"
	    		 + "\nAccept: *";
	     c.getHTML(request);
	   }
	 
}
*/
