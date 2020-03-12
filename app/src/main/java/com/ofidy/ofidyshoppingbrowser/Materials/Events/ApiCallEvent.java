package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public interface ApiCallEvent {

    /**
     * Indicate to the receiver that a network call will fail (network errors, site down) and
     * therefore should NOT be attempted
     */
    void loadCachedData();

}
