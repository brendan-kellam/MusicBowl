package com.main;

//imports
import com.main.util.frame.Frame;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles all time keeping for the program
 * @author Brendan Kellam
 */

public class Clock extends Frame implements Runnable {

	//Sets the period between updates (IE. when all Tracks are tallied and interpreted) UNITS: hours
	private int period = 24;
	
	//New instance of a Timer for refreshing the xml
	private Timer update_timer = new Timer();
	
	//The time between track refreshes (3 mins)
	private long refresh_period = 180000;
	
	//Instance of the Parser.java class
	private Parser parse;
	
	//Instance of the Main.java class
	private Main main;
	
	//constructor
	public Clock(Main main, Parser parse){
		
		//instantiate the main object
		this.main = main;
		
		//instantiate the parse object
		this.parse = parse;
		
		//start the update cycle 
		update();
	}
	
	
	private void update(){ //Updates the xml track information, re-totaling the total times played and other data
		
		//create a new timer schedule
		update_timer.schedule(new TimerTask(){
			
			//create a new run object
			public void run() {
				
				//if the program is still running, allow for update
				if(main.is_running()){
					
					System.out.println("Update");
					
					//update the information from the xml
					parse.update();
					
					//re-start updating process, recursively called
					update();
				}
			}
		
		//sets the update time to the refresh_period (3 mins)
		}, 30000);
		
	}

	
	public void run(){	//the run method for Clock. New instances of the Runnable will call this function
		
		//try:catch block for if the Thread is interrupted/ended mid process
		try{
			
			//convert the cycle period from hours to milliseconds
			long period_millis = period * 60 * 60 * 1000;
			
			//the clock cycle will run as long as the program is still running (see: Main.java)
			while(main.is_running()){

				//sleep this Thread for the cycle period
				sleep(60000);
				
				//gets the most played track polled over the period of time (see: period in Clock.java)
				Track track = parse.total_tracks(); 
				
				System.out.println("Most popular artist: " + parse.total_tracks_by_artist());
				
				//System.out.println("The track " + track.get_title() + " was played a total of " + track.get_play_count() + " times.");
				//TODO: Add the twitter functionality
				//TODO: Test program with real iTunes library
			}
			
		//if the thread has been interrupted, exit out of the Runnable function (see: Main.java)
		}catch(InterruptedException e){
			return;
		}
	}
	
	//allows to hold the current Thread from functioning for a certain period of time
	private synchronized void sleep(long length) throws InterruptedException{
			Thread.sleep(length);
	}
		
}
