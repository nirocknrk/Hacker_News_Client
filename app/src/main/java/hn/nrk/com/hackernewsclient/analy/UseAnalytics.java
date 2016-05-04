package hn.nrk.com.hackernewsclient.analy;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import hn.nrk.com.hackernewsclient.BuildConfig;
import hn.nrk.com.hackernewsclient.R;
import hn.nrk.com.hackernewsclient.model.HNStory;


public class UseAnalytics {

    private Tracker analyticsTracker;

    private boolean isActive() {
        return !BuildConfig.DEBUG;
    }

    public void initTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
//        analyticsTracker = analytics.newTracker(R.xml.global_tracker);
    }

    public void trackPage(String page) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.setScreenName(page);
            analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void trackStory(String page, HNStory story) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.setScreenName(page + ": " + story.getId());
            analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void trackEvent(String action) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction(action)
                    .build());
        }
    }

    public void trackNavigateEvent(String action, HNStory story) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Navigate")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

    public void trackShareEvent(String action, HNStory story) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Share")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

    public void trackVoteEvent(String action, HNStory story) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Vote")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

    public void trackBookmarkEvent(String action, HNStory story) {
        if (isActive() && analyticsTracker != null) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Bookmark")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

}
