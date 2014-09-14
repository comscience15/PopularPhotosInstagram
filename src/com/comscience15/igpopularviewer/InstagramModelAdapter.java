package com.comscience15.igpopularviewer;

import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InstagramModelAdapter extends ArrayAdapter<InstagramModel> {
	public String resultInDate;
	
	// created a constructor
	public InstagramModelAdapter(Context context, List<InstagramModel> photos) {
		super(context, android.R.layout.simple_list_item_1, photos);
	}

	// takes a data item at a position, converts it to a row in the listview
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Take the data source at position (i.e 0)
		// get data items
		InstagramModel igModel = getItem(position);
		
		// check if we are using a recycled view
		if (convertView == null) {
			// if not using recycled view, create it
			// LayoutInflater.from(<arg1>, <arg2>, <arg3>)
			//  getContext() == returning the activity instance within the adapter
			//  R.layout.item_photo == to which XML template we want to inflate
			//  parent == listView itself
			//  false == not to attach yet, will attach when ready
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);	
		}
		
		// Lookup the subview within the template
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		TextView tvUN	= (TextView) convertView.findViewById(R.id.tvUN);
		TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
		TextView tvPostedTime = (TextView) convertView.findViewById(R.id.tvPostedTime);
//		TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		ImageView imgProfile = (ImageView) convertView.findViewById(R.id.imgUN);
		
		// populate the subviews (textfield, imageview) with the correct data
		tvCaption.setText(igModel.usernmae.toUpperCase() + "    " + igModel.caption);
		tvUN.setText(igModel.usernmae);
//		tvPostedTime.setText(igModel.postedTime);
		tvPostedTime.setText(setToDays(igModel.postedTime));
//		tvComment.setText(igModel.commentUser + " " + igModel.comment);
		tvLikesCount.setText(igModel.likesCount + " likes");
		// set image height before loading
//		imgPhoto.getLayoutParams().height = igModel.imageHeight;
		// reset images from recycled view
		imgPhoto.setImageResource(0);
		// ask for photo to be added to imageview based on the photo url
		// BACKGROUND: send a network request to url, download image bytes, convert into bitmap, resizing image, insert bitmap into imageview
		//  now using "picasso" library
		Picasso.with(getContext()).load(igModel.imageUrl).into(imgPhoto);
		Picasso.with(getContext()).load(igModel.imageProfile).into(imgProfile);
		
		// return the view for that data item
		return convertView;
	}

	private String setToDays(String postedTime) {
		// Change to day unit
		long time = Long.valueOf(postedTime).longValue();
		Log.i("TIME", time+"");
		return (String) DateUtils.getRelativeTimeSpanString(time*1000, time*1000, time*1000);
	}
}
