package com.example.neeraja.instagram;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.neeraja.instagram.Models.InstagramModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;


public class InstagramActivity extends ActionBarActivity {
    public SwipeRefreshLayout swipeContainer;

    public static final String CLINET_ID = "13efb4982b2849e284e8d64b34345867";
    public static ArrayList<InstagramModel> alPhotos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        // setting logo in the actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        // swipecontainer
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        alPhotos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, alPhotos);
        ListView lvPhotos = (ListView) findViewById (R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos(){
        String url = "https://api.instagram.com/v1/media/popular?client_id="+CLINET_ID;

        AsyncHttpClient httpClient = new AsyncHttpClient();
        //Get REquest
        httpClient.get(url,null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    alPhotos = InstagramModel.parseJSON(response);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                aPhotos.clear();
                aPhotos.addAll(alPhotos);
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false); // signalling refresh is completed
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("OnFailure", "Failed Error: " + throwable.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Show all the comments for the photo
    public void showComments(View view){
        TextView viewAllComments = (TextView)view;
        String mediaId = viewAllComments.getTag().toString();
        Intent intent = new Intent(this, PhotoCommentActivity.class);
        intent.putExtra("mediaId", mediaId);
        startActivity(intent);
    }

    // Show vidoe in full screen
    public void callVideo(View view){
        ImageButton ibPlay = (ImageButton) view;
        String url = ibPlay.getTag().toString();
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("videoUrl", url);
        startActivity(intent);
    }
}
