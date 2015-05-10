package com.example.neeraja.instagram;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neeraja.instagram.Models.InstagramModel;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramModel> {
    public static final String VIDEO_TYPE = "video";
    public InstagramPhotosAdapter(Context context, List<InstagramModel> objects) {
        super(context, R.layout.item_photo, objects);
    }

    private static class ViewHolder {
        TextView tvUserName;
        TextView tvTimeofPost;
        TextView tvCaption;
        TextView tvCommentsCount;
        TextView tvLikesCount;
        ImageView ivProfilePicture;
        ImageView ivPhoto;
        TextView tvComments;
        TextView tvViewAllComments;
        ImageButton ibPlay;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramModel photo = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvTimeofPost = (TextView) convertView.findViewById(R.id.tvTimeofPost);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
            viewHolder.ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
            viewHolder.tvComments = (TextView) convertView.findViewById(R.id.tvComments);
            viewHolder.tvViewAllComments = (TextView) convertView.findViewById(R.id.tvViewAllComments);
            viewHolder.ibPlay = (ImageButton) convertView.findViewById(R.id.ibPlay);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvUserName.setText(photo.username);
        // converting time to relative time span
        String relativeDate = DateUtils.getRelativeTimeSpanString(Long.parseLong(photo.timeOfPost) *1000,System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME).toString();
        viewHolder.tvTimeofPost.setText(relativeDate);
        String formattedText = "<font color='#FF33B5E5'><b>"+photo.username+": </b></font>"+photo.caption;
        viewHolder.tvCaption.setText(Html.fromHtml(formattedText));
       // FORMAT_ABBREV_RELATIVE
        viewHolder.tvLikesCount.setText(photo.likeCount+ "  Likes");
        viewHolder.tvCommentsCount.setText(photo.CommentCount+ " Comments");
        String comments ="";
        // Display latest two comments
        if(photo.comments.size() > 1){
            comments = photo.comments.get(photo.comments.size() - 1).toString();
            comments += "<br/>";
            comments += photo.comments.get(photo.comments.size() - 2).toString();
        } else if (photo.comments.size() == 1) {
            comments = photo.comments.get(photo.comments.size() - 1).toString();
        } else {
            comments = "No Comments";
        }

        // View all comments for the photo
        viewHolder.tvComments.setText(Html.fromHtml(comments));
        if(photo.CommentCount != 0 && photo.CommentCount > 2) {
            String viewAllComments = "<i>View all " + photo.CommentCount + " Comments </i>";
            viewHolder.tvViewAllComments.setText(Html.fromHtml(viewAllComments));
        }
        viewHolder.tvViewAllComments.setTag(photo.media_id);
        if (photo.type.equals(VIDEO_TYPE)){
            viewHolder.ibPlay.setVisibility(View.VISIBLE);
            viewHolder.ibPlay.setTag(photo.videoUrl);
        } else {
            viewHolder.ibPlay.setVisibility(View.INVISIBLE);
        }
        //clear the imageView before setting
        viewHolder.ivProfilePicture.setImageResource(0);
        viewHolder.ivPhoto.setImageResource(0);


        // For Rounded image in profile picture
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        //load the image using Picasa from image url
        Picasso.with(getContext())
                .load(photo.profilePictureUrl)
                .fit()
                .transform(transformation)
                .placeholder(R.mipmap.default_pic)
                .into(viewHolder.ivProfilePicture);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.mipmap.instagram_icon).into(viewHolder.ivPhoto);
        return convertView;
    }
}
