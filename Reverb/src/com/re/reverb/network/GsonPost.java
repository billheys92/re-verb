package com.re.reverb.network;

import com.re.reverb.androidBackend.Post;

import java.text.SimpleDateFormat;

/**
 * Created by Colin on 10/7/2014.
 */
public class GsonPost {

    int Message_id;
    int Poster_id;
    String Message_body;
    int Anon_flag;
    double Location_lat;
    double Location_long;
    String Time_stamp;
    int Region_id;
    int Spam;
    int Up_vote;
    int Down_vote;
    int Repost_link;
    int Reply_link;

    GsonPost(int Message_id,
             int Poster_id,
            String Message_body,
            int Anon_flag,
            float Location_lat,
            float Location_long,
            String Time_stamp,
            int Region_id,
            int spam,
            int Up_vote,
            int Down_vote,
            int Repost_link,
            int Reply_link)
    {
        this.Message_id = Message_id;
        this.Poster_id = Poster_id;
        this.Message_body = Message_body;
        this.Anon_flag = Anon_flag;
        this.Location_lat = Location_lat;
        this.Location_long = Location_long;
        this.Time_stamp = Time_stamp;
        this.Region_id = Region_id;
        this.Spam = spam;
        this.Up_vote = Up_vote;
        this.Down_vote = Down_vote;
        this.Repost_link = Repost_link;
        this.Reply_link = Reply_link;
    }

    public GsonPost(Post post) {
        this.Message_id = post.getPostId();
        this.Poster_id = post.getUserId();
        this.Message_body = post.getContent().getMessageString();
        this.Anon_flag = post.getAnonymous() ? 1 : 0;
        this.Location_lat = post.getPostLocation().getLatitude();
        this.Location_long = post.getPostLocation().getLongitude();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.Time_stamp = sdf.format(post.getTimeCreated());
        this.Region_id = 0;
        this.Spam = 0;
        this.Up_vote = 0;
        this.Down_vote = 0;
        this.Repost_link = 0;
        this.Reply_link = 0;
    }

    public int getMessage_id() {
        return Message_id;
    }

    public int getPoster_id() {
        return Poster_id;
    }

    public String getMessage_body() {
        return Message_body;
    }

    public int getAnon_flag() {
        return Anon_flag;
    }

    public double getLocation_lat() {
        return Location_lat;
    }

    public double getLocation_long() {
        return Location_long;
    }

    public String getTime_stamp() {
        return Time_stamp;
    }

    public int getRegion_id() {
        return Region_id;
    }

    public int getSpam() {
        return Spam;
    }

    public int getUp_vote() {
        return Up_vote;
    }

    public int getDown_vote() {
        return Down_vote;
    }

    public int getRepost_link() {
        return Repost_link;
    }

    public int getReply_link() {
        return Reply_link;
    }
}
