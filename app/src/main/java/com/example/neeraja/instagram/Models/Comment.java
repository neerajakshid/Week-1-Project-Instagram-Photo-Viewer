package com.example.neeraja.instagram.Models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Comment {
    public String commentsText;
    public String CommentBy;
    public String commentsTime;
    public String commentProfilePicURL;

    public static ArrayList<Comment> parseJSON(JSONObject commentsJSONObject) throws JSONException {
        ArrayList<Comment> comments = new ArrayList<>();
            JSONArray commentsJSONArr = commentsJSONObject.getJSONArray("data");
            for (int j = 0; j < commentsJSONArr.length(); j++) {
                JSONObject commentsJSONObj = commentsJSONArr.getJSONObject(j);
                Comment comment = new Comment();
                comment.commentsText = commentsJSONObj.getString("text");
                comment.commentsTime = commentsJSONObj.getString("created_time");
                comment.CommentBy = commentsJSONObj.getJSONObject("from").getString("username");
                if(commentsJSONObj.getJSONObject("from").getString("profile_picture")!= null || commentsJSONObj.getJSONObject("from").getString("profile_picture")!="") {
                    comment.commentProfilePicURL = commentsJSONObj.getJSONObject("from").getString("profile_picture");
                }
                comments.add(comment);
            }
        return comments;
    }

    @Override
    public String toString() {
        return String.format("<strong>%s</strong> (%s): %s",
                CommentBy,
                DateUtils.getRelativeTimeSpanString(Long.parseLong(commentsTime) * 1000),
                commentsText);
    }
}
