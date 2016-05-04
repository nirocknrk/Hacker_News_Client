package hn.nrk.com.hackernewsclient.data;

public class FieldValidator {

    public boolean isValid(String input) {
        return isNotNull(input) && isNotBlank(input);
    }

    private boolean isNotNull(String input) {
        return input != null;
    }

    private boolean isNotBlank(String input) {
        return input.length() > 0;
    }

}
