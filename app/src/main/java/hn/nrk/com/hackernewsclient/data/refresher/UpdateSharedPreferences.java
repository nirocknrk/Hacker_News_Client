package hn.nrk.com.hackernewsclient.data.refresher;

import android.content.Context;
import android.content.SharedPreferences;


import hn.nrk.com.hackernewsclient.BuildConfig;
import hn.nrk.com.hackernewsclient.HNClientApp;
import hn.nrk.com.hackernewsclient.model.HNStory;

public class UpdateSharedPreferences {

    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".REFRESH_PREFERENCES";

    private static final String KEY_REFRESH_TIME_TOP_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_TOP_STORY";
    private static final String KEY_REFRESH_TIME_NEW_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_NEW_STORY";
    private static final String KEY_REFRESH_TIME_BEST_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_BEST_STORY";
    private static final String KEY_REFRESH_TIME_SHOW_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_SHOW_STORY";
    private static final String KEY_REFRESH_TIME_ASK_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_ASK_STORY";
    private static final String KEY_REFRESH_TIME_JOB_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_JOB_STORY";

    private final SharedPreferences preferences;

    private UpdateSharedPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static UpdateSharedPreferences newInstance() {
        SharedPreferences preferences = HNClientApp.context().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new UpdateSharedPreferences(preferences);
    }

    public void saveRefreshTick(HNStory.FILTER FILTER) {
        switch (FILTER) {
            case top_story:
                preferences.edit().putLong(KEY_REFRESH_TIME_TOP_STORY, UpdateTimeStamp.now().getMillis()).apply();
                break;
            case new_story:
                preferences.edit().putLong(KEY_REFRESH_TIME_NEW_STORY, UpdateTimeStamp.now().getMillis()).apply();
                break;
            case best_story:
                preferences.edit().putLong(KEY_REFRESH_TIME_BEST_STORY, UpdateTimeStamp.now().getMillis()).apply();
                break;
            case show:
                preferences.edit().putLong(KEY_REFRESH_TIME_SHOW_STORY, UpdateTimeStamp.now().getMillis()).apply();
                break;
            case ask:
                preferences.edit().putLong(KEY_REFRESH_TIME_ASK_STORY, UpdateTimeStamp.now().getMillis()).apply();
                break;
            case jobs:
                preferences.edit().putLong(KEY_REFRESH_TIME_JOB_STORY, UpdateTimeStamp.now().getMillis()).apply();
                break;
        }

    }

    public UpdateTimeStamp getLastRefresh(HNStory.FILTER FILTER) {
        switch (FILTER) {
            case top_story:
                return UpdateTimeStamp.from(preferences.getLong(KEY_REFRESH_TIME_TOP_STORY, 0));
            case new_story:
                return UpdateTimeStamp.from(preferences.getLong(KEY_REFRESH_TIME_NEW_STORY, 0));
            case best_story:
                return UpdateTimeStamp.from(preferences.getLong(KEY_REFRESH_TIME_BEST_STORY, 0));
            case show:
                return UpdateTimeStamp.from(preferences.getLong(KEY_REFRESH_TIME_SHOW_STORY, 0));
            case ask:
                return UpdateTimeStamp.from(preferences.getLong(KEY_REFRESH_TIME_ASK_STORY, 0));
            case jobs:
                return UpdateTimeStamp.from(preferences.getLong(KEY_REFRESH_TIME_JOB_STORY, 0));
            default:
                return UpdateTimeStamp.now();
        }
    }

}
