package hn.nrk.com.hackernewsclient.model;

import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;

import java.io.Serializable;

import hn.nrk.com.hackernewsclient.data.data_connectivity.FeedContract;

/**
 * Created by Niroshan on 5/3/2016.
 */
public class HNStory  implements Serializable {
    public static String COMMENT_URL_BASE = "https://news.ycombinator.com/item?id=";
    public static String NEXT_URL_BASE = "https://news.ycombinator.com/";
    public static String ASK_URL_BASE = "item?id=";

    private final Long internalId;
    private final String by;
    private final Long id;
    private final Long timeAgo;
    private final int score;
    private final String title;
    private final String url;
    private final int comments;
    private final String type;
    private final Long timestamp;
    private final int rank;
    private final int read;
    private int bookmark;
    private int voted;
    private String filter;

    public HNStory(Long internalId, String by, Long id, String type, Long timeAgo, int score, String title, String url, int comments, Long timestamp, int rank, int bookmark, int read, int voted, String filter) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.timeAgo = timeAgo;
        this.type = type;
        this.score = score;
        this.title = title;
        this.url = url;
        this.comments = comments;
        this.timestamp = timestamp;
        this.rank = rank;
        this.bookmark = bookmark;
        this.read = read;
        this.voted = voted;
        this.filter = filter;
    }

    public static HNStory from(Cursor cursor) {
        Long internalId = cursor.getLong(FeedContract.StoryEntry.COLUMN_ID);
        Long id = cursor.getLong(FeedContract.StoryEntry.COLUMN_ITEM_ID);
        String type = cursor.getString(FeedContract.StoryEntry.COLUMN_TYPE);
        String by = cursor.getString(FeedContract.StoryEntry.COLUMN_BY);
        int comments = cursor.getInt(FeedContract.StoryEntry.COLUMN_COMMENTS);
        String url = cursor.getString(FeedContract.StoryEntry.COLUMN_URL);
        int score = cursor.getInt(FeedContract.StoryEntry.COLUMN_SCORE);
        String title = cursor.getString(FeedContract.StoryEntry.COLUMN_TITLE);
        Long time = cursor.getLong(FeedContract.StoryEntry.COLUMN_TIME_AGO);
        Long timestamp = cursor.getLong(FeedContract.StoryEntry.COLUMN_TIMESTAMP);
        int rank = cursor.getInt(FeedContract.StoryEntry.COLUMN_RANK);
        int bookmark = cursor.getInt(FeedContract.StoryEntry.COLUMN_BOOKMARK);
        int read = cursor.getInt(FeedContract.StoryEntry.COLUMN_READ);
        int voted = cursor.getInt(FeedContract.StoryEntry.COLUMN_VOTED);
        String filter = cursor.getString(FeedContract.StoryEntry.COLUMN_FILTER);

        return new HNStory(internalId, by, id, type, time, score, title, url, comments, timestamp, rank, bookmark, read, voted, filter);
    }

    public static HNStory fromBookmark(Cursor cursor) {
        Long internalId = cursor.getLong(FeedContract.BookmarkEntry.COLUMN_ID);
        Long id = cursor.getLong(FeedContract.BookmarkEntry.COLUMN_ITEM_ID);
        String type = cursor.getString(FeedContract.BookmarkEntry.COLUMN_TYPE);
        String by = cursor.getString(FeedContract.BookmarkEntry.COLUMN_BY);
        String url = cursor.getString(FeedContract.BookmarkEntry.COLUMN_URL);
        String title = cursor.getString(FeedContract.BookmarkEntry.COLUMN_TITLE);
        Long timestamp = cursor.getLong(FeedContract.BookmarkEntry.COLUMN_TIMESTAMP);
        String filter = cursor.getString(FeedContract.BookmarkEntry.COLUMN_FILTER);
        int bookmark = FeedContract.TRUE_BOOLEAN;

        return new HNStory(internalId, by, id, type, (long) 0, 0, title, url, 0, timestamp, 0, bookmark, 0, 0, filter);
    }

    public String getSubmitter() {
        return by;
    }

    private boolean isSubmitterEmpty() {
        return TextUtils.isEmpty(by);
    }

    public boolean isStoryAJob() {
        return isSubmitterEmpty();
    }

    public Long getId() {
        return id;
    }

    public Long getTimeAgo() {
        return timeAgo;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public int getComments() {
        return comments;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getCommentsUrl() {
        return COMMENT_URL_BASE + getId();
    }

    public String getFilter() {
        return filter;
    }

    public int getRank() {
        return rank;
    }

    public boolean isHackerNewsLocalItem() {
        if (getFilter().equals(FILTER.ask.name())) {
            return true;
        }

        if ((url == null) || url.isEmpty()) {
            return true;
        } else {
            if (url.startsWith(ASK_URL_BASE)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isBookmark() {
        return bookmark == FeedContract.TRUE_BOOLEAN;
    }

    public boolean isRead() {
        return read == FeedContract.TRUE_BOOLEAN;
    }

    public boolean isVoted() {
        return voted == FeedContract.TRUE_BOOLEAN;
    }

    public boolean isJob() {
        return getType().equals("job");
    }

    public void toggleBookmark() {
        if (bookmark == FeedContract.FALSE_BOOLEAN) {
            bookmark = FeedContract.TRUE_BOOLEAN;
        } else {
            bookmark = FeedContract.FALSE_BOOLEAN;
        }
    }

    public Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    public enum FILTER {
        top_story,
        new_story,
        best_story,
        show,
        ask,
        jobs
    }

    public enum TYPE {
        story,
        ask,
        poll
    }
}
