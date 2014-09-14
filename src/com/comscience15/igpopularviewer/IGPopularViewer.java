package com.comscience15.igpopularviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class IGPopularViewer extends Activity {
	public static final String CLIENT_ID ="6e0c896f221747a1a8ca7e6e2e6536e6";
	public static String URL = "https://api.instagram.com/v1/media/popular?client_id=";
	public static String DEBUG_TAG = "DEBUG";
	private ArrayList<InstagramModel> igModel;
	private InstagramModelAdapter igModelAdapter;
	PullToRefreshListView lvPTRPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_igpopular_viewer);
		fecthPopularPhotos();
	}

	private void fecthPopularPhotos() {
		// Initialize arraylist so can add items into it
		igModel = new ArrayList<InstagramModel>();
		
		// Create adapter to bind it to the data in arraylist
		igModelAdapter = new InstagramModelAdapter(this, igModel);
		
		// Populate data into listview
//		ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		   // using PullToRefreshListView instead
		lvPTRPhotos = (PullToRefreshListView) findViewById(R.id.lvPhotos);
		
		// Set adapter to listview
//		lvPhotos.setAdapter(igModelAdapter);
		lvPTRPhotos.setAdapter(igModelAdapter);
		
		// https://api.instagram.com/v1/media/popular?client_id=6e0c896f221747a1a8ca7e6e2e6536e6
		// {"data" => [x] => "images" => "standard_resolution" => "url"} 
		// Setup popular url endpoint
		URL += CLIENT_ID;
		
		// Create the network client
		AsyncHttpClient client = new AsyncHttpClient();

		// Trigger the network request
		// Using JSON format which will get from URL 
		// inside "client" create as an anonymous class
		client.get(URL, new JsonHttpResponseHandler(){
			// define success and failure callbacks
			// this is asynchronous network request which is happening at the backend not on the main thread
			// Handle the successful response (popular photos in JSON)
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// fired once the successful response back
				// response is == popular photos in JSON
				// url, height, username, caption
				// {"data" => [x] => "images" => "standarad_resolution" => "url"}
				// {"data" => [x] => "images" => "standarad_resolution" => "height"}
				// {"data" => [x] => "user" => "username"}
				// {"data" => [x] => "caption" => "text"}
				Log.i(DEBUG_TAG, response.toString());
				JSONArray arrayPhotos = null;
				try {
					// Clear arraylist everytime putting new data into arraylist
					igModel.clear();
					arrayPhotos = response.getJSONArray("data");
					for (int i = 0; i < arrayPhotos.length(); i++){
						JSONObject objectPhotos = arrayPhotos.getJSONObject(i);
						InstagramModel model = new InstagramModel();
						model.usernmae = objectPhotos.getJSONObject("user").getString("username");
						// caption can be null so avoid exception of null value
						if (objectPhotos.getJSONObject("caption") != null) {
							model.caption = objectPhotos.getJSONObject("caption").getString("text");
							model.postedTime = objectPhotos.getJSONObject("caption").getString("created_time");
						}else {
							model.caption = null;
							model.postedTime = null;
						}
						model.imageUrl = objectPhotos.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
						model.imageProfile = objectPhotos.getJSONObject("user").getString("profile_picture");
						if (objectPhotos.getJSONObject("likes") != null) {
							model.likesCount = objectPhotos.getJSONObject("likes").getString("count");
						}
						
//						JSONArray commentArr = new JSONArray(objectPhotos.getJSONObject("comments").getJSONObject("data"));
//						for (int j = 0; j < 2; j++){
//							JSONObject commentObj = commentArr.getJSONObject(j);
//							model.commentUser = commentObj.getJSONObject("from").getString("username");
//							model.comment = commentObj.getString("text");
//						}
						
						Log.i(DEBUG_TAG, model.toString());
						igModel.add(model);
					}
					// call onRefreshComplete to signify refresh has finished
					lvPTRPhotos.onRefreshComplete();
					// Notified adapter that it should populate new changes into listview
					igModelAdapter.notifyDataSetChanged();
				} catch (JSONException e){
					// fire if things fail, JSON parsing is invalid
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.igpopular_viewer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
