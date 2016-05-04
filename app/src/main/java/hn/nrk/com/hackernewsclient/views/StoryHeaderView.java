package hn.nrk.com.hackernewsclient.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.novoda.notils.caster.Views;

import hn.nrk.com.hackernewsclient.R;
import hn.nrk.com.hackernewsclient.model.HNStory;
import hn.nrk.com.hackernewsclient.model.TimeAgo;

public class StoryHeaderView extends LinearLayout {

    private TextView text;
    private TextView author;
    private TextView when;
    private TextView comments;

    public StoryHeaderView(Context context) {
        super(context);
    }

    public StoryHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.view_story_header, this, true);

        text = Views.findById(this, R.id.story_title);
        author = Views.findById(this, R.id.story_by);
        when = Views.findById(this, R.id.story_when);
        comments = Views.findById(this, R.id.story_comments);
    }

    public void updateWith(HNStory story) {
        TimeAgo timeAgo = new TimeAgo(getContext().getResources());
        text.setText(Html.fromHtml(story.getTitle()));
        author.setText(getResources().getString(R.string.story_by, story.getSubmitter()));
        when.setText(timeAgo.timeAgo(story.getTimeAgo()));
        comments.setText(getResources().getQuantityString(R.plurals.story_comments, story.getComments(), story.getComments()));
    }

    public interface Listener {
        void onReplyAction();
    }

}
