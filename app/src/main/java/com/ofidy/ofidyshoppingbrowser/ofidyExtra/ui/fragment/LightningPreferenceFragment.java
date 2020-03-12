package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;


import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Simplify {@link PreferenceManager} inject in all the PreferenceFragments
 *
 * @author Stefano Pacifici
 * @date 2015/09/16
 */
public class LightningPreferenceFragment extends PreferenceFragment {

    @Inject
    PreferenceManager mPreferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ofidy.getAppComponent().inject(this);
    }
}
