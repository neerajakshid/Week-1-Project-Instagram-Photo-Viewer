package com.example.neeraja.instagram.Models;

import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstagramModel {
    public String username;
    public String caption;
    public String imageUrl;
    public String videoUrl;
    public String imageHeight;
    public String imageWidth;
    public String type;
    public String profilePictureUrl;
    public String timeOfPost;
    public int CommentCount;
    public int likeCount;
    public String media_id;
    public ArrayList<Comment> comments;

    public static ArrayList<InstagramModel> parseJSON(JSONObject response) throws JSONException {
        ArrayList<InstagramModel> alPhotos = new ArrayList<>();
        JSONArray photoJSONArr = response.getJSONArray("data");
        for (int i = 0; i < photoJSONArr.length(); i++) {
            JSONObject photoJSONObj = photoJSONArr.getJSONObject(i);
            InstagramModel photo = new InstagramModel();
            if (photoJSONObj.optJSONObject("user") != null) {
                photo.username = photoJSONObj.getJSONObject("user").getString("username");
                photo.profilePictureUrl = photoJSONObj.getJSONObject("user").getString("profile_picture");
            } else {
                Log.v("InstagramActivity", "Invalid User");
                photo.username = "";
            }
            if (photoJSONObj.optJSONObject("caption") != null) {
                photo.caption = photoJSONObj.getJSONObject("caption").getString("text");
            } else {
                Log.v("InstagramActivity", "Invalid Caption");
                photo.caption = "";
            }
            if (photoJSONObj.optJSONObject("images") != null) {
                photo.imageUrl = photoJSONObj.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                photo.imageHeight = photoJSONObj.getJSONObject("images").getJSONObject("standard_resolution").getString("height");
                photo.imageWidth = photoJSONObj.getJSONObject("images").getJSONObject("standard_resolution").getString("width");
            } else {
                Log.v("InstagramActivity", "Invalid Image");
            }
            if (photoJSONObj.optJSONObject("videos") != null) {
                photo.videoUrl = photoJSONObj.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
            } else {
                Log.v("InstagramActivity", "Invalid Video URL");
            }
            photo.type = photoJSONObj.getString("type");
            photo.media_id = photoJSONObj.getString("id");
            photo.timeOfPost = photoJSONObj.getString("created_time");
            if (photoJSONObj.optJSONObject("comments") != null) {
               photo.CommentCount = photoJSONObj.getJSONObject("comments").getInt("count");
               photo.comments = Comment.parseJSON(photoJSONObj.getJSONObject("comments"));
            } else {
                photo.CommentCount=0;
            }
            if (photoJSONObj.optJSONObject("likes") != null) {
                photo.likeCount = photoJSONObj.getJSONObject("likes").getInt("count");
            } else {
                Log.v("InstagramActivity", "You are the first person");
                photo.likeCount = 0;
            }

            // add the photo to the arraylist
            alPhotos.add(photo);
        }
        return alPhotos;
    }
}

