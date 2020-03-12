package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import com.crashlytics.android.Crashlytics;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.AskToLoginEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadEnvironmentEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadShoppingCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LogoutEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LogoutStatusEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.Intervals;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.NetworkUtils;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.ThemeUtils;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.BaseFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.CartFragment;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

import static com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider.getBus;

/**
 * Base activity class called by other activities, has some common methods used by other activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final int LIST_DISPLAY = 0;
    public static final int TEXT_DISPLAY = 1;
    public static final int PROGRESS_DISPLAY = 2;

    private static final String TAG = "BaseActivity";
    public static final int SHOP_TYPE_PRODUCT = 1;
    public static final int CATALOG_TYPE_PRODUCT = 2;
    protected boolean searchShowing;
    protected MenuItem searchMenu;
    protected MenuItem cartMenu;
    protected MenuItem signMenu;
    protected Toolbar toolbar;
    protected int badgeCount;
    protected EditText searchText;
    protected ImageButton searchButton;
    protected boolean connected;

    /**
     * Returns bus provider object for sending and receiving events
     */
   protected Bus getBus() {
        return BusProvider.getBus();
    }

    protected Picasso getPicasso() {
        return Ofidy.getInstance().getPicasso();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onCreate()");
        getBus().register(this);
        connected = NetworkUtils.isConnected(this);
    }

    /**
     * Called by other activities to inflate layout.
     */
    protected void setLayout(int layoutResID) {
        if(!NetworkUtils.isConnected(this))
            setTheme(R.style.NoInternetTheme);
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });
        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> doSearch());
    }

    @Override
    protected void onStart() {
        super.onStart();
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartMenu = null;
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onDestroy()");
        getBus().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onTrimMemory()");
    }

    /**
     * Performs actions when common menu items are clicked, extended by other activities.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_search:
                View v = findViewById(R.id.search_bar);
                v.setVisibility(searchShowing ? View.GONE : View.VISIBLE);
                if(searchMenu != null)
                    searchMenu.setIcon(searchShowing ? R.drawable.ic_search_white_24dp : R.drawable.ic_clear_white_24dp);
                searchShowing = !searchShowing;
                return true;
            case R.id.menu_cart:
                CartFragment.newInstance().show(getSupportFragmentManager(), "Cart");
                return true;
            case R.id.menu_sign:
                if(!AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) ||
                        AppState.getInstance(this).getBoolean(AppState.Key.GUEST)){
                    System.out.println();
                    Intent i = new Intent(this, LoginActivity.class);
                    i.putExtra(BundleKeys.FROM_ACTIVITY, true);

                    startActivity(i);
                }
                else{
                    final AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("Sign out?")
                            .setMessage(getString(R.string.you_sure))
                            .setPositiveButton(R.string.dont_logout, (dialog, which) -> dialog.dismiss())
                            .setNegativeButton(R.string.logout, (dialog, which) -> {
                                dialog.dismiss();
                                getBus().post(new LogoutEvent(true));
                            })
                            .create();
                    alertDialog.show();
                    //getBus().post(new LogoutEvent(true));
                }
                return true;
            case R.id.menu_dollar:
                Ofidy.getInstance().setCurrency("USD");
                getBus().post(new LoadShoppingCartEvent(true));
                return true;
            case R.id.menu_pounds:
                Ofidy.getInstance().setCurrency("GBP");
                getBus().post(new LoadShoppingCartEvent(true));
                return true;
            case R.id.menu_naira:
                Ofidy.getInstance().setCurrency("NGN");
                getBus().post(new LoadShoppingCartEvent(true));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called to open search activities.
     */
    protected void doSearch(){
        String q = searchText.getText().toString().trim();
        if(!TextUtils.isEmpty(q) && getClass() != SearchActivity.class){
            Intent i = new Intent(this, SearchActivity.class);
            i.putExtra(BundleKeys.SEARCH_QUERY, q);
            startActivity(i);
        }
    }

    /**
     * Called to inflate common menu items called by multiple activities.
     */
    protected void inflateCommonMenu(Menu menu){
        searchMenu = menu.findItem(R.id.menu_search);
        cartMenu = menu.findItem(R.id.menu_cart);
        if(cartMenu != null) {
            badgeCount = (int) OfidyDB.getInstance(this).getCartItemsSize();
            if (badgeCount > 0) {
                BadgeStyle style = new BadgeStyle(BadgeStyle.Style.DEFAULT, R.layout.menu_action_item_badge, Color.parseColor("#FFFFFF"), Color.parseColor("#05B20A"), Color.parseColor("#000000"));
                ActionItemBadge.update(this, cartMenu, getResources().getDrawable(R.drawable.ic_shopping_cart_white_24dp),
                        style, badgeCount);
            } else {
                ActionItemBadge.hide(cartMenu);
            }

        }
        signMenu = menu.findItem(R.id.menu_sign);
        if(signMenu != null){
            if(AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) &&
                    !AppState.getInstance(this).getBoolean(AppState.Key.GUEST))
                signMenu.setTitle(R.string.action_sign_out);
            else
                signMenu.setTitle(R.string.action_sign_in);
        }
    }

    /**
     * Called when back button is pressed in activity.
     */
    @Override
    public void onBackPressed() {
       Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onBackPressed()");
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (!(f instanceof BaseFragment)) {
                    continue;  // vanilla fragments don't have onBackPressed
                }

                BaseFragment bf = (BaseFragment) f;
                if (bf.onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    /**
     * Called after successful login to reflect current state in menu.
     */
    protected void onLoginDoneEvent(LoginDoneEvent event) {
        if(signMenu != null){
            if(AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) &&
                    !AppState.getInstance(this).getBoolean(AppState.Key.GUEST))
                signMenu.setTitle(R.string.action_sign_out);
            else
                signMenu.setTitle(R.string.action_sign_in);
        }
        invalidateOptionsMenu();
    }

    /**
     * Called when shopping cart is successfully loaded from server.
     */
    public void onShoppingCartLoadedEvent(ShoppingCartLoadedEvent event) {
        try {
            badgeCount = event.cartItems.size();
            if (cartMenu != null) {
                if (badgeCount > 0) {
                    ActionItemBadge.update(cartMenu, badgeCount);
                } else {
                    ActionItemBadge.hide(cartMenu);
                }
            }
            invalidateOptionsMenu();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Update badge on cart to show correct number of items in cart.
     */
    public void refreshCart() {
        try {
            badgeCount = (int) OfidyDB.getInstance(this).getCartItemsSize();
            if (cartMenu != null) {
                if (badgeCount > 0) {
                    ActionItemBadge.update(cartMenu, badgeCount);
                } else {
                    ActionItemBadge.hide(cartMenu);
                }
            }
            invalidateOptionsMenu();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Called every time already logged in user opens app to refresh session.
     */

    protected void refreshSession(){
        if(NetworkUtils.isConnected(this)){
            long v = System.currentTimeMillis() - UserPrefs.getInstance(this).getLong(UserPrefs.Key.LAST_LOGIN);
            if(v >= Intervals.SESSION_INTERVAL && AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN)){
                if(!AppState.getInstance(this).getBoolean(AppState.Key.GUEST))
                    getBus().post(new LoginStartEvent(UserPrefs.getInstance(this).getString(UserPrefs.Key.EMAIL),
                            UserPrefs.getInstance(this).getString(UserPrefs.Key.PASSWORD), false));
            }
            v = System.currentTimeMillis() - UserPrefs.getInstance(this).getLong(UserPrefs.Key.LAST_UPDATE);
            if(v >= Intervals.CATEGORY_RELOAD_INTERVAL)
                getBus().post(new LoadEnvironmentEvent(true));
        }
    }

    /**
     * Called when user logs out to change menu item to reflect state.
     */
    protected void onLogoutStatusEvent(LogoutStatusEvent event) {
        if(signMenu != null){
            if(AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) ||
                    AppState.getInstance(this).getBoolean(AppState.Key.GUEST))
                signMenu.setTitle(R.string.action_sign_out);
            else
                signMenu.setTitle(R.string.action_sign_in);
        }
        Snackbar.make(toolbar, "Logout successful", Snackbar.LENGTH_SHORT).show();
        invalidateOptionsMenu();
    }

    /**
     * Called to change theme when internet status changes.
     */
    protected void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
        int theme = ThemeUtils.THEME_DEFAULT;
        if(!event.connected)
            theme = ThemeUtils.THEME_GREY;
        if(event.connected != connected) {
            connected = event.connected;
            ThemeUtils.changeToTheme(this, theme);
        }
    }

    /**
     * Called when a non logged in user wants to perform action for only logged in users
     */
    public void onAskToLoginEvent(AskToLoginEvent event) {
        if(event.forced){
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Sign in?")
                    .setMessage("You have to sign in to perform task")
                    .setPositiveButton("Login", (dialog, which) -> {
                        dialog.dismiss();
                        Intent i = new Intent(this, LoginActivity.class);
                        i.putExtra(BundleKeys.FROM_ACTIVITY, true);
                        startActivity(i);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create();
            alertDialog.show();
        }
    }

}
