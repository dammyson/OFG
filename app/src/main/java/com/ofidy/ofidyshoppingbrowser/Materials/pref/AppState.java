package com.ofidy.ofidyshoppingbrowser.Materials.pref;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Utility class to persist global app state (like first-run status, etc). Do NOT use this for
 * persisting user preferences, those are managed separately by {@link UserPrefs}.
 */
public class AppState extends Prefs<AppState.Key> {

    private static final String PREFS_FILE_NAME = "app_state";
    private static AppState sInstance = null;

    // keys
    public static class Key extends BaseKey {

        public static final Key LOGGED_IN = new Key("logged_in", Boolean.class, false);
        public static final Key GUEST = new Key("guest", Boolean.class, false);
        public static final Key WELCOMED = new Key("welcome", Boolean.class, false);

        /* package */ <T> Key(String str, Class<T> type, T defaultValue) {
            super(str, type, defaultValue);
        }

    }

    private AppState(@NonNull Context context) {
        super(context.getApplicationContext(), PREFS_FILE_NAME);
    }

    public static AppState getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new AppState(context);
        }

        return sInstance;
    }

}
