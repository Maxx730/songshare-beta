package com.songshare.songshare;

import org.json.JSONException;
import org.json.JSONObject;

public class Track {
    private int _id,likes;
    private boolean hasLiked;
    private String title,artist,spot_id,art,shared_by;

    public Track(JSONObject data) {
        try {
            title = data.getString("title");
            artist = data.getString("artist");
            shared_by = data.getString("sharer");
            _id = data.getInt("_id");
            likes = data.getInt("likes");

            if(data.getInt("hasLiked") > 0) {
                hasLiked = true;
            } else {
                hasLiked = false;
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public boolean getHasLiked() { return this.hasLiked; }
    public int getLikes() { return this.likes; }
    public int getId() {return this._id; }
    public String getTitle() {
        return this.title;
    }
    public String getArtist(){ return this.artist; }
    public String getSpotId() {
        return this.spot_id;
    }
    public String getArt() {
        return this.art;
    }
    public String getSharer() { return this.shared_by; }
}