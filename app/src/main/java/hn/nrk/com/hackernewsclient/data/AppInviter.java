package hn.nrk.com.hackernewsclient.data;

import hn.nrk.com.hackernewsclient.BuildConfig;
import hn.nrk.com.hackernewsclient.data.refresher.AppInviteSharedPreferences;

/**
 * Created by Niroshan on 5/3/2016.
 */
public class AppInviter {
    private AppInviteSharedPreferences appInviteSharedPreferences;

    public AppInviter() {
        this.appInviteSharedPreferences = AppInviteSharedPreferences.newInstance();
    }

    public boolean shouldShow() {
        return !BuildConfig.DEBUG && meetsCriteria();
    }

    private boolean meetsCriteria() {
        return appInviteSharedPreferences.isFirstTime() || appInviteSharedPreferences.shouldBeReminded();
    }
}
