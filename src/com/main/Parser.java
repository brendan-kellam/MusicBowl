package com.main;

//imports
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.main.util.frame.*;
import com.main.util.xml.*;

/**
 * Handles parsing the Itunes main xml file
 * @author Brendan Kellam
 */


public class Parser extends Frame{
	
	//ManipXML object
	private ManipXML iTunes_xml = new ManipXML();
	
	//the path of the Itunes xml file
	private String path;
	
	//a ArrayList that holds all tracks from the iTunes library
	public List<Track> tracks = new ArrayList<Track>();
	

	
	//constructor. Accepts: the path of the iTunes xml file
	public Parser(){
		
		//initialize the xml
		init();		
	}
	
	private void init(){ //initializes the iTunes_xml file
		
		//sets the default path of where the Itunes xml is usually found
		String local_path = "/Users/" + System.getProperty("user.name") + "/Music/Itunes"; 
				
		//Open a new navigation window for the user
		File xml_file = fileNavigator(local_path, "Navigate to: iTunes Music Library.xml"); 
		
		//if the user hits cancel, the program will simply end
		if(xml_file == null) System.exit(0);
		
		//set path to the location of the file
		path = xml_file.getPath(); 
		
		System.out.println(path);

		
		//open the non-local xml file (see: false)
		iTunes_xml.open(path, false);		
	}
	
	public Track total_tracks(){ //returns the track that was played the most 
		
		//creates a new Track.java instance, assigning it to the first Track within the tracks ArrayList
		Track most_played_track = tracks.get(0);
		
		//loops through each track 
		for(Track track : tracks){
			
			//get the play count for a given track
			int play_count = track.get_play_count();
			
			//if the number of times the current track has been played is greater than the most played track, continue
			if(play_count > most_played_track.get_play_count()){
				
				//set the most played track to the current track
				most_played_track = track;
			}
		}
		
		//return the most played track
		return most_played_track;
	}
	
	public String total_tracks_by_artist(){
		String chosen_artist = null;
		
		//Hashtable<String, ArrayList<Track>> artist = new Hashtable<String, ArrayList<Track>>();
		Hashtable<String, Double> artist = new Hashtable<String, Double>();
		//Hashtable<String, ArrayList<Object>> artist = new Hashtable<String, ArrayList<Object>>();
		
		List<String> artist_names = new ArrayList<String>();
		
		for(Track track : tracks){
			
			if(track.get_artist() == ""){
				continue;
			}
			
			String key = track.get_artist();

			if(artist.containsKey( track.get_artist() )){
					
				double worth = artist.get(key);
				worth += track.get_worth();
				artist.put(key, worth);
				continue;
			}
			
			artist.put(key, track.get_worth());
			artist_names.add(key);
			
		}

		double max = 0.0;
		
		Iterator<Entry<String, Double>> iter = artist.entrySet().iterator();
		
		while(iter.hasNext()){
			
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)iter.next();
			
			double current_max =  (Double) pair.getValue();
			if(current_max > max){
				max = current_max;
				chosen_artist = (String) pair.getKey();
			}
		}
		
		double d = artist.get("U2");
		System.out.println(d);
		
	
		return chosen_artist;
	}
	
	public void update(){ //updates the cached tracks with new obtained data from the iTunes xml
		
		//refresh the xml so updated data can be received
		iTunes_xml.refresh(); 
		
		boolean is_new_song = false;
		
		//get the tracks from the iTunes directory
		List<NodeList> tracks = get_tracks();
		
		//loop through each track 
		for(NodeList track : tracks){
			
			int track_id = Integer.parseInt(get_value(track, "Track ID")); //pulls the Track ID
			int play_count = Integer.parseInt(get_value(track, "Play Count")); //pulls the number of times the track has been played to date
			int skip_count = Integer.parseInt(get_value(track, "Skip Count")); //pulls the number of times the track has been skipped to date
			
			//boolean for if a new song is detected
			is_new_song = true;
			
			//loop through all cached tracks (I.E all Tracks that have already been stored in the tracks ArrayList). Has target to allow for reference
			cached_tracks:for(Track current_track : this.tracks){
				
				//if a cached track equals one found in the xml file, then continue
				if(current_track.get_track_id() == track_id){
					
					//determines if the current track was played during the time interval
					boolean has_played = (current_track.get_original_count() + current_track.get_play_count()) != play_count;
					
					//determines if the current track was skipped during the time interval
					boolean has_skipped = (current_track.get_original_skip() + current_track.get_skip_count()) != skip_count;
					
					//holds the state if the worth of a given track needs to be re-computed
					boolean compute_worth = false;
					
					//if the track was played, continue
					if(has_played){
						
						//calculate the new total for the play count and then applies to current track
						int count = play_count - current_track.get_original_count();
						current_track.set_play_count(count);
						compute_worth = true;
						
						//System.out.println("listened to " + current_track.get_title() + " times: " + current_track.get_play_count());

					}
					
					if(has_skipped){
						
						//calculate the new total for the skip count and then applies to current track
						int count = skip_count - current_track.get_original_skip();
						current_track.set_skip_count(count);
						compute_worth = true;
						
						//System.out.println(current_track.get_title() + " was skipped: " + current_track.get_skip_count() + " times");
					}
					
					//if compute worth is true, re-compute the current track worth
					if(compute_worth) current_track.compute_worth();
					
					
					//Since the id of the cached track equals a corresponding track in the xml, no new song was found
					is_new_song = false;
					
					//break the cached tracks loop. Track has already been identified and found
					break cached_tracks;
				}
			}
			
			//if a new song is detected, continue
			if(is_new_song){

				//add the track to the cached tracks ArrayList
				add_track(track);
				
				Track newTrack = this.tracks.get(this.tracks.size()-1);
				System.out.println("new track added: " + newTrack.get_title() + " with id of " + newTrack.get_track_id());
			}
			
		}
		
		for(Track track : this.tracks){
			System.out.println(track.get_title() + " was listened to " + track.get_play_count() + " times, and skipped " + track.get_skip_count() + " times");
			System.out.println("Worth: " + track.get_worth());
		}
		
	}
	
	public void interpret(){ //interprets the data found in the iTunes_xml file
		
		//get the tracks from the xml
		List<NodeList> tracks = get_tracks();
		
		//loops through each track founded in the iTunes directory
		for(NodeList track : tracks){
			
			//add the track found in the iTunes directory to the cached ArrayList
			add_track(track);
		}
		
	}
	
	private void add_track(NodeList track){ //pulls data from iTunes xml and creates a new Track.java object
		
		//gets the necessary data from each track to allow for a new Track.java object to be created 
		
		int track_id = Integer.parseInt(get_value(track, "Track ID")); //pulls the Track ID
		
		String name = get_value(track, "Name"); //grabs the name of each track
		
		String artist = get_value(track, "Artist"); //grabs the Artist of each track
		if(artist == "0") artist = "";
		
		int original_skip = Integer.parseInt(get_value(track, "Skip Count")); //pulls the number of times said track has been skipped to date
		
		int original_count = Integer.parseInt(get_value(track, "Play Count")); //pulls the number of times the track has been played to date
		
		String play_date = get_value(track, "Play Date UTC"); //pulls the last time the track was played
		
		
		//create a new Track with the data pulled from the iTunes directory
		Track created_track = new Track(track_id, name, artist, original_skip, original_count, play_date);
		
		//add the track to a ArrayList that holds all tracks
		this.tracks.add(created_track);
	}
	
	
	private List<NodeList> get_tracks(){ //returns all tracks located in the tracks directory of iTunes
		
		NodeList track_list = get_children_from_xml("dict", true, null, null).get(0); //pulls the list of tracks located in the iTunes xml file
		
		//if the no track data exists, then a error has occurred
		if(track_list == null){
			dialogBox("A fatal error has occured while reading the iTunes xml file. Error code: 0\nclass:Parser.java");
			System.exit(0);
		}
				
		//pulls from the xml each track found in the iTunes directory and sets the result to a ArrayList of NodeList's
		List<NodeList> tracks = get_children_from_xml("dict", true, null, track_list);
		
		return tracks; //return all tracks found
	}

	private String get_value(NodeList directory, String key){ //reads a key-value pair unique to the iTunes xml file and returns the value of said pair.
		//Accepts: the directory where the key-value pair is found (NodeList), the key (String)
		
		//try-catch block employed in the case of a fatal xml error
		try{
			
			//loops through the directory
			for(int i = 0; i < directory.getLength(); i++){
				
				//gets each node of the directory
				Node node = directory.item(i);
				
				//checks if the node text content is equal to the key 
				if(node.getTextContent().equals(key)){
					
					//return the next sibling (the value) of the pair
					return node.getNextSibling().getTextContent();
				}
			
			}
		
		//catch block
		}catch(Exception fatalXMlErr){
			
			//display error message
			dialogBox("A fatal error has occured while reading iTunes directory tracks. Error code: 2\nclass:Parser.java");
			
			//exit the program
			System.exit(0);
			
		}
		
		//return 0 if the key-value pair does not exist
		return "0";
	}
	
	private List<NodeList> get_children_from_xml(String tag, boolean initial, Node refrence, NodeList pre_list){ //reads through iTunes xml and returns a arraylist of parent nodes
		//Accepts: the tag that is being searched (String) , if the node is found within the root node (boolean) , the parameter node (Node), a predetermined nodeList (NodeList)
		
		//create a new list of NodeList. Will store each parent node found with the specified tag
		List<NodeList> parent_nodes = new ArrayList<NodeList>();
		
		//try:catch block employed for safety
		try{
			
			//creates a null NodeList
			NodeList data = null;
			
			
			//if the tag is found within the root node
			if(initial){
				//Grabs the data from the xml under the initial <dict> tag. (see: iTunes Music Library.xml)
				data = iTunes_xml.returnChildNodes("dict");
			
			}else{
				//sets the data to the nodes found under the reference node.
				data = refrence.getChildNodes();
			}
			
			//if a already existing nodelist is given
			if(pre_list != null) data = pre_list;
		
			//Loops through the xml data
			for(int i = 0; i < data.getLength(); i++){
				
				//examine each node of the data
				Node node = data.item(i);
				
				//if the node is equal to the desired tag, return the list of nodes that are children of said node
				if(node.getNodeName().equals(tag)){
					parent_nodes.add(node.getChildNodes());
				}
					
			}
			
		//if a error has occurred, output a message to the user
		}catch(Exception fatalXMLErr){
			dialogBox("A fatal error has occured while reading the iTunes xml file. Error code: 1\nclass:Parser.java");
			
			//exit the program message
			System.exit(0);
		}
		
		//return the arrayList
		return parent_nodes;
	}
	
}
