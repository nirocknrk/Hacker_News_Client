package hn.nrk.com.hackernewsclient.data.refresher;

public final class UpdateTimeStamp {

    private final long millis;

    private UpdateTimeStamp(long millis) {
        this.millis = millis;
    }

    public static UpdateTimeStamp from(long millis) {
        return new UpdateTimeStamp(millis);
    }

    public static UpdateTimeStamp now() {
        return new UpdateTimeStamp(System.currentTimeMillis());
    }

    public long getMillis() {
        return millis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UpdateTimeStamp sunTimestamp = (UpdateTimeStamp) o;

        return millis == sunTimestamp.millis;
    }

    @Override
    public int hashCode() {
        return (int) (millis ^ (millis >>> 32));
    }

}
