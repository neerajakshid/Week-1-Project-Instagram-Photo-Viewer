package com.example.neeraja.instagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.neeraja.instagram.Models.Comment;
import com.example.neeraja.instagram.Models.InstagramModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoCommentActivity extends ActionBarActivity{
    public SwipeRefreshLayout swipeContainer;
    public static final String CLINET_ID = "13efb4982b2849e284e8d64b34345867";
    public static String MEDIA_ID;
    public static ArrayList<Comment> alComments;
    private PhotoCommentsAdapter commentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // setting logo in the actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        // swipecontainer
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.commentsSwipeContainer);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchComments();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //get data from the intent
        MEDIA_ID = getIntent().getExtras().getString("mediaId");
        alComments = new ArrayList<>();
        commentsAdapter = new PhotoCommentsAdapter(this, alComments);
        ListView lvComments = (ListView) findViewById (R.id.lvComments);
        lvComments.setAdapter(commentsAdapter);
        fetchComments();
       }
    public void fetchComments(){
        String url = "https://api.instagram.com/v1/media/"+MEDIA_ID+"/comments?client_id="+CLINET_ID;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        //Get REquest
        httpClient.get(url,null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    alComments = Comment.parseJSON(response);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                commentsAdapter.clear();
                commentsAdapter.addAll(alComments);
                commentsAdapter.notifyDataSetChanged();
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
}
