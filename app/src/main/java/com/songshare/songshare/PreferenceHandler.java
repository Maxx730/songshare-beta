package com.songshare.songshare;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler {
    private SharedPreferences prefs;

    public PreferenceHandler(Context con) {
        prefs = con.getSharedPreferences("songshare-prefs",Context.MODE_PRIVATE);
    }

    public String getPreference(String name) {
        return this.prefs.getString(name,"No " + name);
    }

    //Add a new preference to the DB
    public void setPreference(String name,String value) {
        SharedPreferences.Editor edit = this.prefs.edit();

        edit.putString(name,value);

        edit.apply();
    }

    //Delete a saved pref based on name.
    public void deletePreference(String name) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(name);
        edit.commit();
    }

    public boolean prefExists(String name) {
        return prefs.contains(name);
    }
}
