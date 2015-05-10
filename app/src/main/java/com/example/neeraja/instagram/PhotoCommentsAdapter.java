package com.example.neeraja.instagram;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neeraja.instagram.Models.Comment;
import com.example.neeraja.instagram.Models.InstagramModel;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;


public class PhotoCommentsAdapter extends ArrayAdapter <Comment> {

    public PhotoCommentsAdapter(Context context, ArrayList<Comment> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        TextView tvCommentsUserName;
        TextView tvCommentsTimeofPost;
        ImageView ivCommentsProfilePicture;
        TextView tvMultipleComments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            viewHolder.tvCommentsUserName = (TextView) convertView.findViewById(R.id.tvCommentsUserName);
            viewHolder.tvCommentsTimeofPost = (TextView) convertView.findViewById(R.id.tvCommentsTimeofPost);
            viewHolder.tvMultipleComments = (TextView) convertView.findViewById(R.id.tvMultipleComments);
            viewHolder.ivCommentsProfilePicture = (ImageView) convertView.findViewById(R.id.ivCommentsProfilePicture);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCommentsUserName.setText(comment.CommentBy);
        // converting time to relative time span
        String relativeDate = DateUtils.getRelativeTimeSpanString(Long.parseLong(comment.commentsTime) * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME).toString();
        viewHolder.tvCommentsTimeofPost.setText(relativeDate);
        viewHolder.tvMultipleComments.setText(comment.commentsText);

        //clear the imageView before setting
        viewHolder.ivCommentsProfilePicture.setImageResource(0);

        //load the image using Picasa from image url

        // For Rounded image in profile picture
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(comment.commentProfilePicURL)
                .fit()
                .transform(transformation)
                .placeholder(R.mipmap.default_pic)
                .into(viewHolder.ivCommentsProfilePicture);
        return convertView;
    }
}
