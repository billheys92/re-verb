package com.re.reverb.androidBackend.post;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.post.content.StandardPostContent;
import com.re.reverb.androidBackend.post.content.TextPostContent;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostFactory
{
    private static Reverb reverb = Reverb.getInstance();

    public static Post createPost(String text, boolean anonymous) throws InvalidPostException
    {
        int posterId;
        try {
            posterId = reverb.getCurrentUserId();
        } catch (NotSignedInException e) {
            throw new InvalidPostException("No user is signed in.");
        }

        Location location = reverb.getCurrentLocation();
        Date now = Calendar.getInstance().getTime();
        TextPostContent content = new TextPostContent(text);

        return new Post(posterId, 1, reverb.getCurrentLocation(), new Date(), new TextPostContent(text), anonymous);
    }

    public static Post createPost(ReceivePostDto gsonPost) throws InvalidPostException
    {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(gsonPost.getTime_stamp());
            return new Post(gsonPost.getPoster_id(), gsonPost.getMessage_id(), new Location(gsonPost.getLocation_lat(), gsonPost.getLocation_long()), date, new TextPostContent(gsonPost.getMessage_body()), gsonPost.getAnon_flag() == 1);
        } catch (ParseException e) {
            throw new InvalidPostException("Could not parse a time stamp from post");
        }
    }

    public static ParentPost createParentPost(ReceivePostDto gsonPost) throws InvalidPostException
    {
        try
        {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(gsonPost.getTime_stamp());
            return new ParentPost(gsonPost.getRegion_id(),
                    0,
                    new ArrayList<ChildPost>(0),
                    gsonPost.getPoster_id(),
                    gsonPost.getMessage_id(),
                    new Location(gsonPost.getLocation_lat(), gsonPost.getLocation_long()),
                    date,
                    //new TextPostContent(gsonPost.getMessage_body()),
                    new StandardPostContent(gsonPost.getName(), gsonPost.getHandle(), gsonPost.getMessage_body(), gsonPost.getProfile_picture()),
                    gsonPost.getAnon_flag() == 1);
        } catch (ParseException e) {
            throw new InvalidPostException("Could not parse a time stamp from post");
        }
    }
	

}
