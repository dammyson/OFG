package com.ofidy.ofidyshoppingbrowser.ofidyExtra.search;

import android.support.annotation.NonNull;


import com.ofidy.ofidyshoppingbrowser.ofidyExtra.database.HistoryItem;

import java.util.List;

interface SuggestionsResult {

    /**
     * Called when the search suggestions have
     * been retrieved from the server.
     *
     * @param searchResults the results, a valid
     *                      list of results. May
     *                      be empty.
     */
    void resultReceived(@NonNull List<HistoryItem> searchResults);

}
