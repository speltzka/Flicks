package com.example.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flicks.models.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //constants
    //the base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    //the parameter name for the API Key
    public final static String API_KEY_PARAM = "api_key";

    //tag for logging this activity
    public final static String TAG = "MovieMainActivity";


    //instance fields
    AsyncHttpClient client;

    //Base url for loading images
    String imageBaseUrl;
    //the poster size to use when fetching images, part of the URL
    String posterSize;
    // the list of currently playing movies
    ArrayList<com.example.flicks.models.Movie> movies;
    //the recyclerview
    RecyclerView rvMovies;
    //the adapter wired to the recyclerView
    MovieAdapter3 adapter;
    //image config
    com.example.flicks.models.Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();
        //initialize the adapter - movies array cannot be reinitialized after this point
        adapter = new MovieAdapter3(movies);
        //resolve recyclerview
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        //connect layout manager and adapter
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
        //get the configuration on app creation
        getConfiguration();
    }

    //get the list of currently playing movies
    private void getNowPlaying(){
        //create the URL
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        com.example.flicks.models.Movie movie = new com.example.flicks.models.Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                }
                    catch (JSONException e) {
                        logError("Failed to parse current movies", e, true);
                    }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing Endpoint", throwable, true);
            }
        });
    }


    //get the configuration from the API
    private void getConfiguration() {
        //create the URL
        String url = API_BASE_URL + "/configuration";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API Key, always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed getting configuration", throwable, true);
                 }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Config config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",  config.getImageBaseUrl(), config.getPosterSize()));
                    //pass config object to adapter
                    adapter.setConfig(config);
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

                getNowPlaying();

            }
        });

    }

    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid a silent error
        if (alertUser) {
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

    }
}