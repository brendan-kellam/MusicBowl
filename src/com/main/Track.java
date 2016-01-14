package com.main;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class handles the creation of a new Track. 
 * Each new song has given parameters. (see: iTunes Music Library.xml)
 * @author Brendan Kellam
 */

public class Track {

	//the id for a given track
	private int track_id;
	
	//the name for a given track
	private String title;
	
	//the name of artist for given track (if any)
	private String artist;
	
	//the number of times this track has been skipped originally
	private int original_skip;
	
	//the number of times this track has been played originally
	private int original_count;
	
	//the number of times this track has been skipped for this session
	private int skip_count = 0;
	
	//the number of times this track has been played for this session
	private int play_count = 0;
	
	//the last time this track was played
	private String play_date;
	
	//the worth of this track (see equation/graph for details)
	private double worth;
	
	
	//constructor. Accepts: The track id, name, play count and play date
	public Track(int track_id, String title, String artist, int original_skip, int original_count, String play_date){
		
		//Instantiate each parameter 
		this.track_id = track_id;
		this.title = title;
		this.artist = artist;
		this.original_skip = original_skip;
		this.original_count = original_count;
		this.play_date = play_date;
		
		compute_worth();
	}
	
	//compute the worth of the track
	public void compute_worth(){
		double cof = (1.0 / (skip_count + 1.0) ) / 2.0;
		worth = cof * Math.sqrt( (play_count+0.1) ) - 0.15811388301;
		worth = round(worth, 7);
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	//get the track worth
	public double get_worth(){ return worth; }
	
	//get the track id
	public int get_track_id(){ return track_id; }
	
	//get the title of this track
	public String get_title(){ return title; }
	
	//return the artist of this Track
	public String get_artist(){ return artist; }
	
	
	//set and get the original SKIP for this given track
	public int get_original_skip(){ return original_skip; }
	public void set_original_skip(int original_skip){ this.original_skip = original_skip; }
	
	//set and get the original COUNT for this given track
	public int get_original_count(){ return original_count; }
	public void set_original_count(int original_count){ this.original_count = original_count; }
	
	
	//set and get the total listen times for this given track
	public int get_skip_count(){ return skip_count; }
	public void set_skip_count(int skip_count){ this.skip_count = skip_count; }
	
	//set and get the total listen times for this given track
	public int get_play_count(){ return play_count; }
	public void set_play_count(int play_count){ this.play_count = play_count; }
	
	//get the last time this track was played
	public String get_play_date(){ return play_date; }
		
}
