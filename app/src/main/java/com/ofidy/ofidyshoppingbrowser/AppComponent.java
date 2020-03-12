package com.ofidy.ofidyshoppingbrowser;

import com.ofidy.ofidyshoppingbrowser.Materials.utils.AdBlock;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.ProxyUtils;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.constant.StartPage;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.download.LightningDownloadListener;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.search.SuggestionsAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.BrowserActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.ReadingActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.TabsManager;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.ThemableBrowserActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.ThemableSettingsActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.browser.BrowserPresenter;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.dialog.LightningDialogBuilder;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.BookmarkSettingsFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.BookmarksFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.DebugSettingsFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.LightningPreferenceFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.PrivacySettingsFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.TabsFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.view.LightningView;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.view.LightningWebClient;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(BrowserActivity activity);

    void inject(BookmarksFragment fragment);

    void inject(BookmarkSettingsFragment fragment);

    void inject(LightningDialogBuilder builder);

    void inject(TabsFragment fragment);

    void inject(LightningView lightningView);

    void inject(ThemableBrowserActivity activity);

    void inject(LightningPreferenceFragment fragment);

    void inject(Ofidy app);

    void inject(ProxyUtils proxyUtils);

    void inject(ReadingActivity activity);

    void inject(LightningWebClient webClient);

    void inject(ThemableSettingsActivity activity);

    void inject(AdBlock adBlock);

    void inject(LightningDownloadListener listener);

    void inject(PrivacySettingsFragment fragment);

    void inject(StartPage startPage);

    void inject(BrowserPresenter presenter);

    void inject(TabsManager manager);

    void inject(DebugSettingsFragment fragment);

    void inject(SuggestionsAdapter suggestionsAdapter);

}
