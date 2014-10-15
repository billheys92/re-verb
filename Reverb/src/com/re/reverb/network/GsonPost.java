package com.re.reverb.network;

/**
 * Created by Colin on 10/7/2014.
 */
public class GsonPost {

    int poster_id;
    String message;
    int anon;
    float lat;
    float longi;
    String time;
    String region;
    int spam;
    int vote;
    String repost;
    String reply;

    GsonPost(int poster_id,
            String message,
            int anon,
            float lat,
            float longi,
            String time,
            String region,
            int spam,
            int vote,
            String repost,
            String reply)
    {
        this.poster_id = poster_id;
        this.message = message;
        this.anon = anon;
        this.lat = lat;
        this.longi = longi;
        this.time = time;
        this.region = region;
        this.spam = spam;
        this.vote = vote;
        this.repost = repost;
        this.reply = reply;
    }
}
