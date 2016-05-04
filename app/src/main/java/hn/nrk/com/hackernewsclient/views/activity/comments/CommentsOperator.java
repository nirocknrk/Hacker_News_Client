package hn.nrk.com.hackernewsclient.views.activity.comments;

import android.content.Intent;
import android.view.MenuItem;


import com.novoda.notils.exception.DeveloperError;

import hn.nrk.com.hackernewsclient.R;
import hn.nrk.com.hackernewsclient.data.DataPersister;
import hn.nrk.com.hackernewsclient.data.Provider;
import hn.nrk.com.hackernewsclient.injection.Inject;
import hn.nrk.com.hackernewsclient.model.HNStory;
import hn.nrk.com.hackernewsclient.model.Respose;
import hn.nrk.com.hackernewsclient.views.HNActivity;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class CommentsOperator {

    private final HNActivity activity;
    private final HNStory story;
    private DataPersister persister;
    private Subscription subscription;

    public CommentsOperator(HNActivity activity) {
        this.activity = activity;
        this.story = getStory();
        this.persister = Inject.dataPersister();
    }

    private HNStory getStory() {
        if (activity.getIntent().getExtras().containsKey(CommentsActivity.ARG_STORY)) {
            return (HNStory) activity.getIntent().getExtras().getSerializable(CommentsActivity.ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    public void onResume() {
        trackAnalytics(R.string.analytics_page_comments);
    }

    public void onArticleSelected() {
        trackAnalytics(R.string.analytics_event_view_story_comments);
        activity.navigate().toInnerBrowser(story);
        activity.finish();
    }

    public void onBookmarkUnselected() {
        trackAnalytics(R.string.analytics_event_remove_bookmark_comments);
        persister.removeBookmark(story);
    }

    public void onBookmarkSelected() {
        trackAnalytics(R.string.analytics_event_add_bookmark_comments);
        persister.addBookmark(story);
    }

    public void onShareArticle() {
        trackAnalytics(R.string.analytics_event_share_story);
        Intent chooserIntent = Intent.createChooser(
                story.createShareIntent(), null);
        activity.startActivity(chooserIntent);
    }

    private void trackAnalytics(int actionResource) {
        Inject.usageAnalytics().trackShareEvent(
                activity.getString(actionResource),
                story);
    }

    public void retrieveComments() {
        Provider provider = Inject.provider();
        subscription = provider
                .observeComments(getStory().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Respose>() {
                    @Override
                    public void onCompleted() {
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Inject.crashAnalytics().logSomethingWentWrong("DataRepository: getCommentsFrom " + getStory().getId(), e);
                    }

                    @Override
                    public void onNext(Respose response) {

                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_article:
                onArticleSelected();
                return true;
            case R.id.action_bookmark:
                if (item.isChecked()) {
                    onBookmarkUnselected();
                } else {
                    onBookmarkSelected();
                }
                return true;
            case android.R.id.home:
                activity.finish();
                return true;
            case R.id.action_share:
                onShareArticle();
                return true;
            default:
                return false;
        }
    }

    public void onRefresh(boolean online) {
        if (online) {
            retrieveComments();
        }
    }

    public void onPostCreate(boolean online) {
        onRefresh(online);
    }
}
