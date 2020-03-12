package com.ofidy.ofidyshoppingbrowser.Materials.pref;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Utility class to persist user preferences. Do NOT use this for persisting application state, that
 * is managed separately by {@link AppState}.
 */
public class UserPrefs extends Prefs<UserPrefs.Key> {

    private static final String PREFS_FILE_NAME = "user_prefs";
    private static UserPrefs sInstance = null;

    // keys
    public static class Key extends BaseKey {
        public static final Key MODE = new Key("mode", String.class, "");
        public static final Key SID = new Key("sessionId", String.class, "");
        public static final Key ID = new Key("customerId", String.class, "");
        public static final Key PASSWORD = new Key("password", String.class, "");
        public static final Key EMAIL = new Key("email", String.class, "");
        public static final Key CURRENCY = new Key("currency", String.class, "NGN");
        public static final Key BODY = new Key("body", String.class, "");
        public static final Key FIRST_NAME = new Key("first_name", String.class, "");
        public static final Key LAST_NAME = new Key("last_name", String.class, "");
        public static final Key UNAME = new Key("u_name", String.class, "");

        public static final Key LAST_LOGIN = new Key("last_login", Long.class, 0L);
        public static final Key LAST_UPDATE = new Key("last_update", Long.class, 0L);

        /* package */ <T> Key(String str, Class<T> type, T defaultValue) {
            super(str, type, defaultValue);
        }

    }

    private UserPrefs(@NonNull Context context) {
        super(context.getApplicationContext(), PREFS_FILE_NAME);
    }

    public static UserPrefs getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new UserPrefs(context);
        }

        return sInstance;
    }

}
