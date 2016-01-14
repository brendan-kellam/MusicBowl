package com.main;

import java.io.IOException;

import com.main.frames.MainFrame;
import com.main.net.HttpDownload;
//import com.main.net.HttpDownload;
import com.main.net.Soundcloud;
import com.main.util.frame.*;

/**
 * The Main class to MusicBowl
 * Handles all initialization of classes 
 * @author Brendan Kellam
 * @version 1.0
 */

public class Main extends Frame {

		
	//instance of the Parse.java class
	private Parser parse;
	
	//instance of the Clock.java class
	private Clock clock;
	
	//new Thread for the clock cycle
	private Thread clock_run;
	
	//boolean that describes if the program is running
	private boolean running = false;
	
	//constructor
	public Main(){
		
		//HttpDownload d = new HttpDownload("https://api.soundcloud.com/tracks/84153270/download?client_id=df29622310bbb59a0e69ac76ca819b31", "/Users/brendankellam/Downloads");
		
		MainFrame f = new MainFrame();
		
		
		/*
		try {
			Soundcloud s = new Soundcloud("MusicBowl", "computer28");
		} catch (IOException e) {
			dialogBox("An Errror has occured while atempting to sign into soundcloud. Error code: 4\nclass:Soundcloud.java");
			e.printStackTrace();
		}*/
		
		
		dialogBox("Sign in achievied");
		
		//set the program to running
		this.running = true;
		
		//create a new xml parser
		parse = new Parser();
		parse.interpret();
		
		//create a new clock instance
		clock = new Clock(this, parse);
		start_clock();
		
	}
	
	
	public boolean is_running(){ //returns if the program is running
		return running;
	}
	
	
	public synchronized void start_clock(){ //Start the main timer for the program. (synchronized is used to prevent overlaps with threads)
		
		//Instantiate new thread with the Runnable class of Clock.java
		clock_run = new Thread(clock, "MusicBowl_Thread_Clock");
		
		//Start the timer
		clock_run.start();
	}
	
	public synchronized void stop_clock(){ //Stops the main timer for the program
		//try:catch block used if synchronization errors with the threads occur
		try {
			
			//end the current task being preformed by the Thread
			clock_run.interrupt(); 
			
			//join and end the clock thread
			clock_run.join();
			
		} catch (InterruptedException e) {
			
			//error message displayed to the user
			dialogBox("A fatal error has occured while attempting to end Clock.java thread. Error code: 3\nclass:Main.java");
			
			//print error to console
			e.printStackTrace();
			
			//end program
			System.exit(0);
		}
		
	}

	
	//Static main function. Begging of program.
	public static void main(String[] args) {
		new Main();
	}

}
