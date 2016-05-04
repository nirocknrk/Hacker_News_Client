package hn.nrk.com.hackernewsclient.data;

import android.content.Context;
import android.content.SharedPreferences;

import hn.nrk.com.hackernewsclient.BuildConfig;
import hn.nrk.com.hackernewsclient.HNClientApp;

/**
 * Created by Niroshan on 5/3/2016.
 */
public class HNSharedPreferences {
    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".LOGIN_PREFERENCES";

    public static SharedPreferences newInstance() {
        SharedPreferences preferences = HNClientApp.context().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences;
    }

    public boolean isLoggedIn(){
        return false;
    }
}
