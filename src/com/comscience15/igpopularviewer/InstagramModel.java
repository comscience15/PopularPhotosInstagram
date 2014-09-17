package com.comscience15.igpopularviewer;

public class InstagramModel {
	// This model class contains the attributes that need to hold for displaying on screen
	// This model, we need to create from here following JSON that got from https://api.instagram.com/v1/media/popular?client_id=6e0c896f221747a1a8ca7e6e2e6536e6
	// Ex. username, caption, image_url, height, likes_count
	public String usernmae;
	public String caption;
	public String imageUrl;
	public String imageProfile;
	public int imageHeight;
	public int likesCount;
	public String postedTime;
	public String comment;
	public String commentUser;
}
