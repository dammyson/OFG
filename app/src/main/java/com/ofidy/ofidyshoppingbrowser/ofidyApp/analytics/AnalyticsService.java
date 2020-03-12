package com.ofidy.ofidyshoppingbrowser.ofidyApp.analytics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.squareup.otto.Bus;

public class AnalyticsService {

    private static final String TAG = AnalyticsService.class.getSimpleName();

    private final Bus mEventBus;

    public AnalyticsService(Bus eventBus) {
        mEventBus = eventBus;
    }

    public void start() {
        getBus().register(this);
        //getBus().post(new LoadGhostVersionEvent(true));
    }

    public void stop() {
        getBus().unregister(this);
    }

//    @Subscribe
//    public void onLoginDoneEvent(LoginDoneEvent event) {
//        if (event.wasInitiatedByUser) {
//            String blogType = getBlogTypeFromUrl(event.username);
//            logLogin(blogType, true);
//
//            // user just logged in, now's a good time to check this
//            //getBus().post(new LoadGhostVersionEvent(true));
//        }
//    }
//
//    @Subscribe
//    public void onLoginErrorEvent(LoginErrorEvent event) {
//        if (event.wasInitiatedByUser) {
//            //String blogType = getBlogTypeFromUrl(event.error);
//            //logLogin(blogType, true);
//        }
//    }

//    @Subscribe
//    public void onGhostVersionLoadedEvent(GhostVersionLoadedEvent event) {
//        logGhostVersion(event.version);
//    }

    private static void logGhostVersion(@Nullable String ghostVersion) {
        if (ghostVersion == null) {
            ghostVersion = "Unknown";
        }
        Crashlytics.log(Log.INFO, TAG, "GHOST VERSION = " + ghostVersion);
        Answers.getInstance().logCustom(new CustomEvent("Ghost Version")
                .putCustomAttribute("version", ghostVersion));
    }

    private static void logLogin(String email, boolean success) {
        String successStr = success ? "SUCCEEDED" : "FAILED";
        Crashlytics.log(Log.INFO, TAG, "LOGIN " + successStr + ", email type = " + email);
        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(email)
                .putSuccess(success));
    }

//    @Subscribe
//    public void onLogoutStatusEvent(LogoutStatusEvent logoutEvent) {
//        if (logoutEvent.succeeded) {
//            Crashlytics.log(Log.INFO, TAG, "LOGOUT SUCCEEDED");
//            Answers.getInstance().logCustom(new CustomEvent("Logout"));
//        }
//    }

    public static void logDbSchemaVersion(@NonNull String dbSchemaVersion) {
        Crashlytics.log(Log.INFO, TAG, "DB SCHEMA VERSION = " + dbSchemaVersion);
        Answers.getInstance().logCustom(new CustomEvent("DB Schema Version")
                .putCustomAttribute("version", dbSchemaVersion));
    }

    private String getBlogTypeFromUrl(@Nullable String blogUrl) {
        if (blogUrl == null) {
            return "Unknown";
        } else if (blogUrl.matches("^.*\\.ghost.io(/.*)?$")) {
            return "Ghost Pro";
        } else {
            return "Self-hosted";
        }
    }


    // post actions
    public static void logNewDraftUploaded() {
        logPostAction("New draft uploaded", null);
    }

    public static void logDraftPublished(String postUrl) {
        logPostAction("Published draft", postUrl);
    }

    public static void logEditsPublished(String postUrl) {
        logPostAction("Published edits to post", postUrl);
    }

    public static void logPostUnpublished() {
        logPostAction("Unpublished post", null);
    }

    public static void logDraftAutoSaved() {
        logPostAction("Auto-saved draft", null);
    }

    public static void logDraftSavedExplicitly() {
        logPostAction("Explicitly saved draft", null);
    }

    public static void logPostSavedInUnknownScenario() {
        logPostAction("Unknown scenario", null);
    }

    public static void logEditsAutoSavedLocally() {
        logPostAction("Auto-saved edits to published post", null);
    }

    public static void logDraftDeleted() {
        logPostAction("Deleted draft", null);
    }

//    @Subscribe
//    public void onFileUploadedEvent(FileUploadedEvent event) {
//        logPostAction("Image uploaded", null);
//    }

    private static void logPostAction(@NonNull String postAction, @Nullable String postUrl) {
        CustomEvent postStatsEvent = new CustomEvent("Post Actions")
                .putCustomAttribute("Scenario", postAction);
        if (postUrl != null) {
            // FIXME this is a huge hack, also Fabric only shows 10 of these per day
            postStatsEvent.putCustomAttribute("URL", postUrl);
        }
        Crashlytics.log(Log.INFO, TAG, "POST ACTION: " + postAction);
        Answers.getInstance().logCustom(postStatsEvent);
    }


    // misc private methods
    private Bus getBus() {
        return mEventBus;
    }

}
