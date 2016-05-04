package hn.nrk.com.hackernewsclient.news;

import android.content.Intent;
import android.view.View;

import hn.nrk.com.hackernewsclient.HNClientApp;
import hn.nrk.com.hackernewsclient.model.HNStory;

/**
 * Created by Niroshan on 5/3/2016.
 */
public interface NewsListener {

    void onShareClicked(Intent shareIntent);

    void onCommentsClicked(View v, HNStory story);

    void onCommentsClicked(HNStory story);

    void onContentClicked(HNStory story);

    void onExternalLinkClicked(HNStory story);

    void onBookmarkAdded(HNStory story);

    void onBookmarkRemoved(HNStory story);

    void onStoryVoteClicked(HNStory story);
}
