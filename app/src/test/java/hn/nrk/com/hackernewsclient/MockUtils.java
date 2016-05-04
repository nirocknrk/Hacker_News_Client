package hn.nrk.com.hackernewsclient;


import java.util.Date;
import java.util.Random;
import java.util.UUID;

import hn.nrk.com.hackernewsclient.model.HNStory;

public class MockUtils {

    public static Long generateRandomLong() {
        return new Random().nextLong();
    }

    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    public static int generateRandomInt() {
        return new Random().nextInt(80 - 65) + 65;
    }

    public static HNStory createStory() {
        Long internalId = generateRandomLong();
        String by = "malmstein";
        Long id = generateRandomLong();
        String type = "top_story";
        Long time = new Date().getTime();
        int score = 100;
        String title = generateRandomString();
        String url = "http://www.hackernews.com";
        int comments = 20;
        Long timestamp = System.currentTimeMillis();
        int rank = 10;
        int bookmark = 0;
        int read = 0;
        int voted = 0;
        String filter = "top_story";

        return new HNStory(internalId, by, id, type, time, score, title, url,
                         comments, timestamp, rank, bookmark, read,
                         voted, filter
        );
    }

}
