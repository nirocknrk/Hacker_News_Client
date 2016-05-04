package hn.nrk.com.hackernewsclient.data;

import android.content.ContentResolver;
import android.content.ContentValues;



import java.util.List;
import java.util.Vector;

import hn.nrk.com.hackernewsclient.data.data_connectivity.FeedContract;
import hn.nrk.com.hackernewsclient.model.HNStory;
import hn.nrk.com.hackernewsclient.model.HN_date;

public class DataPersister {

    private final ContentResolver contentResolver;

    public DataPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public int persistStories(List<ContentValues> topStories) {

        String timestampTwoDaysAgo = String.valueOf(HN_date.now().twoDaysAgo().getTimeInMillis());
        contentResolver.delete(FeedContract.StoryEntry.CONTENT_STORY_URI,
                FeedContract.StoryEntry.TIMESTAMP + " <= ?",
                new String[]{timestampTwoDaysAgo});

        ContentValues[] cvArray = new ContentValues[topStories.size()];
        topStories.toArray(cvArray);

        return contentResolver.bulkInsert(FeedContract.StoryEntry.CONTENT_STORY_URI, cvArray);
    }

    public int persistComments(Vector<ContentValues> commentsVector, Long storyId) {
        contentResolver.delete(FeedContract.CommentsEntry.CONTENT_COMMENTS_URI,
                FeedContract.CommentsEntry.ITEM_ID + " = ?",
                new String[]{storyId.toString()});

        ContentValues[] cvArray = new ContentValues[commentsVector.size()];
        commentsVector.toArray(cvArray);
        return contentResolver.bulkInsert(FeedContract.CommentsEntry.CONTENT_COMMENTS_URI, cvArray);
    }

    public void addBookmark(HNStory story) {
        ContentValues bookmarkValues = new ContentValues();

        bookmarkValues.put(FeedContract.BookmarkEntry.ITEM_ID, story.getId());
        bookmarkValues.put(FeedContract.BookmarkEntry.BY, story.getSubmitter());
        bookmarkValues.put(FeedContract.BookmarkEntry.TYPE, story.getType());
        bookmarkValues.put(FeedContract.BookmarkEntry.URL, story.getUrl());
        bookmarkValues.put(FeedContract.BookmarkEntry.TITLE, story.getTitle());
        bookmarkValues.put(FeedContract.BookmarkEntry.TIMESTAMP, System.currentTimeMillis());
        bookmarkValues.put(FeedContract.BookmarkEntry.FILTER, story.getFilter());

        contentResolver.insert(FeedContract.BookmarkEntry.CONTENT_BOOKMARKS_URI, bookmarkValues);

        ContentValues storyValues = new ContentValues();
        storyValues.put(FeedContract.StoryEntry.BOOKMARK, FeedContract.TRUE_BOOLEAN);

        contentResolver.update(FeedContract.StoryEntry.CONTENT_STORY_URI,
                storyValues,
                FeedContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});
    }

    public void removeBookmark(HNStory story) {
        contentResolver.delete(FeedContract.BookmarkEntry.CONTENT_BOOKMARKS_URI,
                FeedContract.BookmarkEntry.ITEM_ID + " = ?",
                new String[]{story.getId().toString()});

        ContentValues storyValues = new ContentValues();
        storyValues.put(FeedContract.StoryEntry.BOOKMARK, FeedContract.FALSE_BOOLEAN);

        contentResolver.update(FeedContract.StoryEntry.CONTENT_STORY_URI,
                storyValues,
                FeedContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});
    }

    public void markStoryAsRead(HNStory story) {
        ContentValues bookmarkValues = new ContentValues();

        bookmarkValues.put(FeedContract.StoryEntry.READ, FeedContract.TRUE_BOOLEAN);

        contentResolver.update(FeedContract.StoryEntry.CONTENT_STORY_URI,
                bookmarkValues,
                FeedContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});

    }

    public void addVote(HNStory story) {
        ContentValues bookmarkValues = new ContentValues();

        bookmarkValues.put(FeedContract.StoryEntry.VOTED, FeedContract.TRUE_BOOLEAN);

        contentResolver.update(FeedContract.StoryEntry.CONTENT_STORY_URI,
                bookmarkValues,
                FeedContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});
    }
}
