package com.main.net;

//imports
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import com.main.util.frame.Frame;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;
import org.json.*;

import de.voidplus.soundcloud.*;

/**
 * Handles interacting with Soundcloud.
 * Uses the java-api-wrapper-1.20-all.jar package
 * {@link https://github.com/soundcloud/java-api-wrapper}
 * {@link https://github.com/nok/soundcloud-java-library}
 * @author brendankellam
 */

public class Soundcloud extends Frame{
	
	//The Client ID and Client secret for the MusicBowl program (see: developers.soundcloud.com)
	private final String CLIENT_ID = "df29622310bbb59a0e69ac76ca819b31";
	private final String CLIENT_SECRET = "823c2313869b06ab1673db0ef15835e3";

	//user name to users soundcloud
	private String user;
	
	//password to users soundcloud
	private String pass;
	
	//creates a new instance of the java soundcloud wrapper api
	private ApiWrapper wrapper;
	
	//constructor. Accepts: The user name and password of the user's soundcloud account. Throws exception in case of fatal error
	public Soundcloud(String user, String pass) throws IOException{
		
		SoundCloud soundcloud = new SoundCloud(
			    CLIENT_ID,
			    CLIENT_SECRET,
			    user,
			    pass
			);
	

		ArrayList<Track> result = soundcloud.findTrack("Songs of Innocence");
		if(result!=null){
		    System.out.println("Tracks: "+result.size());
		    for(Track track:result){
		    	
		        System.out.println(track.isDownloadable() + "  " + track.getTitle() + " " + track.getDownloadUrl());
		    	if(track.isDownloadable()){
		    	
		    		int id = track.getId();
		    		String d = "https://api.soundcloud.com/tracks/" + id + "/download?client_id=df29622310bbb59a0e69ac76ca819b31";
		    		//System.out.println(track.getTitle() + "  " + d);
		    		//System.out.println(track.getFavoritingsCount());
		    	}
		    	
		    }
		}
		
		/*
		//initialize login variables
		this.user = user;
		this.pass = pass;
		
		//initialize the soundcloud wrapper
		init_wrapper();

		
		HttpResponse resp = wrapper.get(Request.to("tracks")
				);
		
		
		try {
			JSONObject response = new JSONObject( Http.formatJSON( Http.getString( wrapper.get( Request.to("tracks") ) ) ) );
			
			JSONArray arr = response.getJSONArray("{");
			for (int i = 0; i < arr.length(); i++)
			{
				String genre = arr.getJSONObject(i).getString("genre");			    
				System.out.println(genre);
			    
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		*/
		
		//IDEA: Maybe the track(s) that is picked by the algorithm takes into account the number of likes a given track has
		
	}

	
	private void init_wrapper() throws IOException{
		
		//initialize the wrapper. Passes the client id and secret, along with two null values
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, null);
				
		//login into soundcloud using users information. Set oauth token to non expiring
		wrapper.login(user, pass, Token.SCOPE_NON_EXPIRING);
	
	}

}
