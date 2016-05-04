package hn.nrk.com.hackernewsclient.news;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;

import com.novoda.notils.exception.DeveloperError;

import hn.nrk.com.hackernewsclient.data.data_connectivity.FeedContract;
import hn.nrk.com.hackernewsclient.model.HNStory;
import hn.nrk.com.hackernewsclient.views.ViewDelegate;

public class TopNewsFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 1001;

    public static TopNewsFragment from(QUERY query) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("query", query);
        TopNewsFragment fragment = new TopNewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    public HNStory.FILTER getType() {
        QUERY query = (QUERY) getArguments().get("query");
        switch (query) {
            case top:
                return HNStory.FILTER.top_story;
            case newest:
                return HNStory.FILTER.new_story;
            case best:
                return HNStory.FILTER.top_story;
            default:
                new DeveloperError("Bad Query type");
                return null;
        }
    }

    protected String getOrder() {
        QUERY query = (QUERY) getArguments().get("query");
        switch (query) {
            case top:
                return FeedContract.StoryEntry.RANK + " ASC" +
                        ", " + FeedContract.StoryEntry.TIMESTAMP + " DESC";
            case newest:
                return FeedContract.StoryEntry.TIME_AGO + " DESC";
            case best:
                return FeedContract.StoryEntry.SCORE + " DESC" +
                        ", " + FeedContract.StoryEntry.TIMESTAMP + " DESC";
            default:
                new DeveloperError("Bad Query type");
                return null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = FeedContract.StoryEntry.buildStoriesUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                FeedContract.StoryEntry.STORY_COLUMNS,
                FeedContract.StoryEntry.TYPE + " = ?",
                new String[]{HNStory.TYPE.story.name()},
                getOrder());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        storiesAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }



    public enum QUERY {
        top,
        newest,
        best
    }

}
