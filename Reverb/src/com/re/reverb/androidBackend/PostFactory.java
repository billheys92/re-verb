package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.network.GsonPost;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostFactory
{
    private static Reverb reverb = Reverb.getInstance();

    public static Post createPost(String text, boolean anonymous) throws InvalidPostException{
        Post post = new Post();

        int posterId;
        try {
            posterId = reverb.getCurrentUserId();
        } catch (NotSignedInException e) {
            throw new InvalidPostException("No user is signed in.");
        }

        Location location = reverb.getCurrentLocation();
        Date now = Calendar.getInstance().getTime();
        TextPostContent content = new TextPostContent(text);

        post.setUserId(posterId);
        post.setPostLocation(location);
        post.setTimeCreated(now);
        post.setContent(content);
        post.setAnonymous(anonymous);

        return post;
    };

    public static Post createPost(GsonPost gsonPost) throws InvalidPostException{
        Post post = new Post();

        post.setUserId(gsonPost.getPoster_id());
        post.setPostId(gsonPost.getMessage_id());
        post.setPostLocation(new Location(gsonPost.getLocation_lat(), gsonPost.getLocation_long()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(gsonPost.getTime_stamp());
            post.setTimeCreated(date);
        } catch (ParseException e) {
            throw new InvalidPostException("Could not parse a time stamp from post");
        }
        TextPostContent content = new TextPostContent(gsonPost.getMessage_body());
        post.setContent(content);
        post.setAnonymous(gsonPost.getAnon_flag() == 1);

        return post;
    }
	

}
