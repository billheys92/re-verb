package com.re.reverb.androidBackend.post;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.post.content.StandardPostContent;
import com.re.reverb.androidBackend.post.content.TextPostContent;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

        return new Post(posterId, 1, reverb.getCurrentLocation(), new Date(), null, new TextPostContent(text), anonymous);
    }

    public static Post createPost(ReceivePostDto gsonPost) throws InvalidPostException
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(gsonPost.getTime_stamp());
            return new Post(gsonPost.getPoster_id(), gsonPost.getMessage_id(), new Location(gsonPost.getLocation_lat(), gsonPost.getLocation_long()), date, null, new TextPostContent(gsonPost.getMessage_body()), gsonPost.getAnon_flag() == 1);
        } catch (ParseException e) {
            throw new InvalidPostException("Could not parse a time stamp from post");
        }
    }

    public static ParentPost createParentPost(ReceivePostDto gsonPost) throws InvalidPostException
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date createTime = sdf.parse(gsonPost.getCreate_time());
            Date updateTime = null;
            if(gsonPost.getUpdate_time() != null){
                SimpleDateFormat sdfupdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                sdfupdate.setTimeZone(TimeZone.getTimeZone("UTC"));
                updateTime = sdfupdate.parse(gsonPost.getUpdate_time());
            }
            return new ParentPost(gsonPost.getRegion_id(),
                    gsonPost.getNum_replies(),
                    gsonPost.getNum_reposts(),
                    new ArrayList<ChildPost>(0),
                    gsonPost.getPoster_id(),
                    gsonPost.getMessage_id(),
                    new Location(gsonPost.getLocation_lat(), gsonPost.getLocation_long()),
                    createTime,
                    updateTime,
                    //new TextPostContent(gsonPost.getMessage_body()),
                    new StandardPostContent(gsonPost.getName(), gsonPost.getHandle(), gsonPost.getMessage_body(), gsonPost.getUp_vote(), 1, gsonPost.getProfile_picture(), gsonPost.getPicture_name()),
                    gsonPost.getAnon_flag() == 1);
        } catch (ParseException e) {
            throw new InvalidPostException("Could not parse a time stamp from Parent post");
        }
    }

    public static ChildPost createChildPost(ReceivePostDto gsonPost) throws InvalidPostException
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date createTime = sdf.parse(gsonPost.getCreate_time());
            Date updateTime = null;
            if(gsonPost.getUpdate_time() != null){
                SimpleDateFormat sdfupdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                sdfupdate.setTimeZone(TimeZone.getTimeZone("UTC"));
                updateTime = sdfupdate.parse(gsonPost.getUpdate_time());
            }
            return new ChildPost(gsonPost.getReply_link(),
                    gsonPost.getPoster_id(),
                    gsonPost.getMessage_id(),
                    new Location(gsonPost.getLocation_lat(), gsonPost.getLocation_long()),
                    createTime,
                    updateTime,
                    new StandardPostContent(gsonPost.getName(), gsonPost.getHandle(), gsonPost.getMessage_body(), gsonPost.getUp_vote(), 1, gsonPost.getProfile_picture(), gsonPost.getPicture_name()),
                    gsonPost.getAnon_flag() == 1);
        } catch (ParseException e) {
            throw new InvalidPostException("Could not parse a time stamp from child post");
        }
    }
	

}
