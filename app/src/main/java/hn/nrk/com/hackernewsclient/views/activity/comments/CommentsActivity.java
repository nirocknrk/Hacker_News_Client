package hn.nrk.com.hackernewsclient.views.activity.comments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;

import hn.nrk.com.hackernewsclient.BuildConfig;
import hn.nrk.com.hackernewsclient.R;
import hn.nrk.com.hackernewsclient.views.HNActivity;

public class CommentsActivity extends HNActivity implements SwipeRefreshLayout.OnRefreshListener {


    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_COMMENT_STORY";

    private CommentsPresenter commentsPresenter;
    private CommentsOperator commentsOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsPresenter = new CommentsPresenter(this, this);
        commentsPresenter.onCreate();

        commentsOperator = new CommentsOperator(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            //commentsPresenter.onPostCreate(isOnline());
            //commentsOperator.onPostCreate(isOnline());
        }
    }

    @Override
    public void onBackPressed() {
        commentsPresenter.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentsOperator.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //commentsPresenter.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return commentsOperator.onOptionsItemSelected(item) && commentsPresenter.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        commentsPresenter.onRefresh(isOnline());
        //commentsOperator.onRefresh(isOnline());
    }
}
