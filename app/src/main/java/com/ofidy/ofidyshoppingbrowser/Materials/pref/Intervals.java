package com.ofidy.ofidyshoppingbrowser.Materials.pref;

/**
 * Created by Ari on 16/11/2016.
 */

public class Intervals {

    private static final long SEC = 1000;
    private static final long MIN = 60 * SEC;
    private static final long HOUR = 60 * MIN;
    private static final long DAY = 24 * HOUR;

    public static final long SESSION_INTERVAL = 2 * MIN;
    public static final long CATEGORY_RELOAD_INTERVAL = 2 * DAY;
}
