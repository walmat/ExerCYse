package com.example.jakegrace.exercyse;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Matt on 3/7/17.
 */

public class Session {

    private final String[] reqs = {
            "username",
            "id",
            "firstname",
            "lastname",
            "workoutname",
            "type",
            "index"
    };
    public static SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static SharedPreferences getSession() {
        return prefs;
    }
    public String StringGetType(){
        String type = prefs.getString("type","");
        return type;
    }

    public String getUsername() {
        String username = prefs.getString("username", "");
        return username;
    }

    public void setUsername(String username) {
        prefs.edit().putString("username", username).apply();
    }

    public String getID() {
        String id = prefs.getString("id", "");
        return id;
    }

    public void setID(String id) {
        prefs.edit().putString("id", id).apply();
    }

    public String getFullName() {
        String name = prefs.getString("firstname", "") + " " + prefs.getString("lastname", "");
        return name;
    }

    public String getFirstname() {
        String first = prefs.getString("firstname", "");
        return first;
    }

    public void setFirstname(String firstname) {
        prefs.edit().putString("firstname", firstname).apply();
    }
    public void setType(String type) {
        prefs.edit().putString("type", type).apply();
    }

    public String getLastname() {
        String last = prefs.getString("lastname", "");
        return last;
    }

    public String getType() {
        String last = prefs.getString("type", "");
        return last;
    }

    public void setLastname(String lastname) {
        prefs.edit().putString("lastname", lastname).apply();
    }

    public String getWktName(){
        String wkt = prefs.getString("workoutName", "");
        return wkt;
    }

    public void setWktName(String name){
        prefs.edit().putString("workoutName", name).apply();
    }

    public String getIndex() {
        String index = prefs.getString("index", "");
        return index;
    }

    public void setIndex(String index) {
        prefs.edit().putString("index", index).apply();
    }

    public void destroy() {
        for (int i = 0; i < reqs.length; i++) {
            if (prefs.contains(reqs[i])) {
                prefs.edit().remove(reqs[i]);
            }
        }
    }
}