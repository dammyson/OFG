package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import com.ofidy.ofidyshoppingbrowser.Materials.utils.ThemeUtils;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.preference.PreferenceManager;

import javax.inject.Inject;

public abstract class ThemableSettingsActivity extends AppCompatPreferenceActivity {

    private int mTheme;

    @Inject
    PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Ofidy.getAppComponent().inject(this);
        mTheme = mPreferenceManager.getUseTheme();

        // set the theme
        if (mTheme == 0) {
            setTheme(R.style.Theme_SettingsTheme);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(ThemeUtils.getPrimaryColor(this)));
        } else if (mTheme == 1) {
            setTheme(R.style.Theme_SettingsTheme_Dark);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(ThemeUtils.getPrimaryColorDark(this)));
        } else if (mTheme == 2) {
            setTheme(R.style.Theme_SettingsTheme_Black);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(ThemeUtils.getPrimaryColorDark(this)));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPreferenceManager.getUseTheme() != mTheme) {
            restart();
        }
    }

    private void restart() {
        recreate();
    }
}
