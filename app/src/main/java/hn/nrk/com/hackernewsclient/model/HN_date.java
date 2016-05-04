package hn.nrk.com.hackernewsclient.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Niroshan on 5/3/2016.
 */
public class HN_date {
    private static final int MILLIS_IN_A_SEC = 1000;
    private static final int TWO_DAYS = 2;
    private static final int SEC_IN_A_MIN = 60;
    private static final int MIN_IN_AN_HOUR = 60;
    private static final int HOUR_IN_A_DAY = 24;
    private static final int MILLIS_IN_A_MIN = HN_date.MILLIS_IN_A_SEC * SEC_IN_A_MIN;
    private static final int MILLIS_IN_AN_HOUR = MILLIS_IN_A_MIN * MIN_IN_AN_HOUR;
    private static final int MILLIS_IN_A_DAY = MILLIS_IN_AN_HOUR * HOUR_IN_A_DAY;
    private static final int MILLIS_IN_TWO_DAYS = MILLIS_IN_A_DAY * TWO_DAYS;

    private final Calendar calendar;

    public HN_date(Calendar calendar) {
        this.calendar = calendar;
    }

    public static HN_date now() {
        return from(new Date());
    }

    public static HN_date from(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new HN_date(calendar);
    }

    public HN_date twoDaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.calendar.getTimeInMillis() - MILLIS_IN_TWO_DAYS);
        return new HN_date(calendar);
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

}
