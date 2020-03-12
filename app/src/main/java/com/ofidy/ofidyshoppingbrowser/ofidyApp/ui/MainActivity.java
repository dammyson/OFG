package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AskToLoginEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemRemovedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CategoryLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LogoutStatusEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Category;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.NetworkUtils;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.CategoryAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.HomeFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.OrdersFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.WishListFragment;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private CategoryAdapter adapter;

    //Tab titles
    private String TITLES[] = {"Home", "WishList", "Orders"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //PagerAdaper inner class for tabs
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Gets list of categories to add to side menu
        List<Category> recipes = loadCategories();

        mRecyclerView = (RecyclerView) findViewById(R.id.nav_list);
        adapter = new CategoryAdapter(this, recipes);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //To take care of expired session issue and keep local values synced with server
        refreshSession();
    }

    //When onResume is called app state is checked and shown correctly in menu
    public void onResume(){
        super.onResume();
        if(signMenu != null) {
            if (AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) &&
                    !AppState.getInstance(this).getBoolean(AppState.Key.GUEST))
                signMenu.setTitle(R.string.action_sign_out);
            else
                signMenu.setTitle(R.string.action_sign_in);
        }
    }

    @OnClick(R.id.appNig)
    public void goToApp(){
        if (AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) && !UserPrefs.getInstance(MainActivity.this).getString(UserPrefs.Key.SID).isEmpty() ){
            Intent intent = new Intent(this, com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }


    }
    //Called to load categories
    private List<Category> loadCategories(){
        //Load categories saved from server
        List<Category> categories = OfidyDB.getInstance(this).getCategories();
        if(categories == null || categories.isEmpty()) {
            //Loads place holder categories since saved one is not available
            categories = new ArrayList<>();
            String s = "{\"data\":[{\"id\":\"3\",\"name\":\"BOOKS, AUDIOBOOKS & E-BOOKS\",\"desc\"" +
                    ":\"books\",\"logo\":\"fa-book\",\"priority\":\"11\",\"categories\":[{\"id\":\"" +
                    "13\",\"name\":\"KINDLE E-READERS\",\"logo\":\"fa-info-circle\",\"groupId\":\"" +
                    "3\"},{\"id\":\"16\",\"name\":\"BOOKS\",\"logo\":\"fa-book\",\"groupId\":\"" +
                    "3\"}]},{\"id\":\"5\",\"name\":\"MOVIES, TV, MUSIC & GAMES\",\"desc\":\"" +
                    "movies\",\"logo\":\"fa-tv\",\"priority\":\"7\",\"categories\":[{\"id\":\"17\"," +
                    "\"name\":\"MUSIC\",\"logo\":\"fa-music\",\"groupId\":\"5\"},{\"id\":\"26\"," +
                    "\"name\":\"TOYS AND GAMES\",\"logo\":\"fa-gamepad\",\"groupId\":\"5\"}," +
                    "{\"id\":\"137\",\"name\":\"MOVIES\",\"logo\":\"fa-info-circle\",\"groupId\":\"5\"}," +
                    "{\"id\":\"138\",\"name\":\"TV\",\"logo\":\"fa-tv\",\"groupId\":\"5\"},{\"id\":\"1165\",\"name\":\"TOYS, GAMES AND PUZZLES\",\"logo\":\"fa-gamepad\",\"groupId\":\"5\"}]},{\"id\":\"6\",\"name\":\"ELECTRONICS & COMPUTERS\",\"desc\":\"computers\",\"logo\":\"fa-desktop\",\"priority\":\"1\",\"categories\":[{\"id\":\"14\",\"name\":\"TABLETS\",\"logo\":\"fa-tablet\",\"groupId\":\"6\"},{\"id\":\"18\",\"name\":\"ELECTRONICS\",\"logo\":\"fa-info-circle\",\"groupId\":\"6\"},{\"id\":\"140\",\"name\":\"LAPTOPS & COMPUTERS\",\"logo\":\"fa-desktop\",\"groupId\":\"6\"},{\"id\":\"154\",\"name\":\"MOBILE PHONES\",\"logo\":\"fa-mobile-phone\",\"groupId\":\"6\"},{\"id\":\"159\",\"name\":\"COMPUTER PERIPHERALS\",\"logo\":\"fa-keyboard-o\",\"groupId\":\"6\"},{\"id\":\"2168\",\"name\":\"TV\",\"logo\":\"fa-tv\",\"groupId\":\"6\"},{\"id\":\"2169\",\"name\":\"SOUND SYSTEMS\",\"logo\":\"fa-info-circle\",\"groupId\":\"6\"}]},{\"id\":\"7\",\"name\":\"HOME, GARDEN, PETS, GROCERY & DIY\",\"desc\":\"home-garden\",\"logo\":\"fa-home\",\"priority\":\"5\",\"categories\":[{\"id\":\"19\",\"name\":\"PET SUPPLIES\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"},{\"id\":\"141\",\"name\":\"HOME\",\"logo\":\"fa-home\",\"groupId\":\"7\"},{\"id\":\"142\",\"name\":\"GARDEN\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"},{\"id\":\"143\",\"name\":\"DIY\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"},{\"id\":\"149\",\"name\":\"GROCERY\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"},{\"id\":\"1170\",\"name\":\"BED & BATH\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"},{\"id\":\"2160\",\"name\":\"LIQUOR\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"},{\"id\":\"2165\",\"name\":\"KITCHEN\",\"logo\":\"fa-info-circle\",\"groupId\":\"7\"}]},{\"id\":\"8\",\"name\":\"CHILDREN, TODDLER & BABY\",\"desc\":\"children\",\"logo\":\"fa-child\",\"priority\":\"3\",\"categories\":[{\"id\":\"20\",\"name\":\"BABY & TODDLER\",\"logo\":\"fa-info-circle\",\"groupId\":\"8\"},{\"id\":\"144\",\"name\":\"CHILDREN\",\"logo\":\"fa-child\",\"groupId\":\"8\"},{\"id\":\"1161\",\"name\":\"BOYS\",\"logo\":\"fa-child\",\"groupId\":\"8\"},{\"id\":\"1169\",\"name\":\"GIRLS\",\"logo\":\"fa-child\",\"groupId\":\"8\"},{\"id\":\"2166\",\"name\":\"UNIFORMS\",\"logo\":\"fa-info-circle\",\"groupId\":\"8\"}]},{\"id\":\"9\",\"name\":\"SHOES, CLOTHES, JEWELLERY & ACCESSORIES\",\"desc\":\"shoes-clothes\",\"logo\":\"fa-info-circle\",\"priority\":\"2\",\"categories\":[{\"id\":\"21\",\"name\":\"JEWELLERY\",\"logo\":\"fa-info-circle\",\"groupId\":\"9\"},{\"id\":\"146\",\"name\":\"SHOES\",\"logo\":\"fa-info-circle\",\"groupId\":\"9\"},{\"id\":\"147\",\"name\":\"CLOTHES\",\"logo\":\"fa-info-circle\",\"groupId\":\"9\"},{\"id\":\"160\",\"name\":\"ACCESSORIES\",\"logo\":\"fa-shopping-bag\",\"groupId\":\"9\"},{\"id\":\"1162\",\"name\":\"HANDBAGS\",\"logo\":\"fa-shopping-bag\",\"groupId\":\"9\"},{\"id\":\"1168\",\"name\":\"WATCHES\",\"logo\":\"fa-watch\",\"groupId\":\"9\"},{\"id\":\"1172\",\"name\":\"BAGS & WALLETS\",\"logo\":\"fa-briefcase\",\"groupId\":\"9\"},{\"id\":\"1174\",\"name\":\"FABRICS\",\"logo\":\"fa-info-circle\",\"groupId\":\"9\"},{\"id\":\"1176\",\"name\":\"LEATHER ACCESSORIES\",\"logo\":\"fa-info-circle\",\"groupId\":\"9\"},{\"id\":\"1178\",\"name\":\"BOTTOM\",\"logo\":\"fa-info-circle\",\"groupId\":\"9\"},{\"id\":\"1183\",\"name\":\"BAGS\",\"logo\":\"fa-shopping-bag\",\"groupId\":\"9\"}]},{\"id\":\"10\",\"name\":\"SPORT, LEISURE & TRAVEL\",\"desc\":\"sport-leisure\",\"logo\":\"fa-futbol-o\",\"priority\":\"8\",\"categories\":[{\"id\":\"22\",\"name\":\"SPORTS\",\"logo\":\"fa-futbol-o\",\"groupId\":\"10\"},{\"id\":\"148\",\"name\":\"OUTDOORS\",\"logo\":\"fa-tree\",\"groupId\":\"10\"},{\"id\":\"1164\",\"name\":\"SUITCASES\",\"logo\":\"fa-suitcase\",\"groupId\":\"10\"},{\"id\":\"1167\",\"name\":\"LUGGAGE\",\"logo\":\"fa-suitcase\",\"groupId\":\"10\"},{\"id\":\"1184\",\"name\":\"LEISURE\",\"logo\":\"fa-sun-o\",\"groupId\":\"10\"},{\"id\":\"1186\",\"name\":\"OUTDOOR SPORT & LEISURE\",\"logo\":\"fa-futbol-o\",\"groupId\":\"10\"},{\"id\":\"1188\",\"name\":\"TRAVEL\",\"logo\":\"fa-suitcase\",\"groupId\":\"10\"}]},{\"id\":\"11\",\"name\":\"HEALTH & BEAUTY\",\"desc\":\"health-beauty\",\"logo\":\"fa-plus-square\",\"priority\":\"6\",\"categories\":[{\"id\":\"23\",\"name\":\"HEALTH\",\"logo\":\"fa-plus-square\",\"groupId\":\"11\"},{\"id\":\"28\",\"name\":\"PHARMACY\",\"logo\":\"fa-plus\",\"groupId\":\"11\"},{\"id\":\"150\",\"name\":\"BEAUTY\",\"logo\":\"fa-info-circle\",\"groupId\":\"11\"},{\"id\":\"1175\",\"name\":\"SLEEP, BATHING, NAPPIES & HEALTH\",\"logo\":\"fa-bed\",\"groupId\":\"11\"},{\"id\":\"2161\",\"name\":\"PERSONAL\",\"logo\":\"fa-info-circle\",\"groupId\":\"11\"}]},{\"id\":\"12\",\"name\":\"BUSINESS, SCIENCE & INDUSTRY\",\"desc\":\"business-industry\",\"logo\":\"fa-gears\",\"priority\":\"9\",\"categories\":[{\"id\":\"25\",\"name\":\"BUSINESS\",\"logo\":\"fa-briefcase\",\"groupId\":\"12\"},{\"id\":\"152\",\"name\":\"SCIENCE\",\"logo\":\"fa-gears\",\"groupId\":\"12\"},{\"id\":\"153\",\"name\":\"INDUSTRY\",\"logo\":\"fa-info-circle\",\"groupId\":\"12\"},{\"id\":\"1166\",\"name\":\"GREETING CARDS\",\"logo\":\"fa-info-circle\",\"groupId\":\"12\"},{\"id\":\"1171\",\"name\":\"STATIONERY\",\"logo\":\"fa-paperclip\",\"groupId\":\"12\"},{\"id\":\"1173\",\"name\":\"OFFICE EQUIPMENTS\",\"logo\":\"fa-building\",\"groupId\":\"12\"},{\"id\":\"1179\",\"name\":\"COMPUTING & OFFICE\",\"logo\":\"fa-laptop\",\"groupId\":\"12\"},{\"id\":\"2164\",\"name\":\"ART & CRAFT\",\"logo\":\"fa-info-circle\",\"groupId\":\"12\"}]},{\"id\":\"1002\",\"name\":\"CAR & MOTORBIKE\",\"desc\":\"car-moto\",\"logo\":\"fa-car\",\"priority\":\"10\",\"categories\":[{\"id\":\"155\",\"name\":\"CAR PARTS\",\"logo\":\"fa-car\",\"groupId\":\"1002\"},{\"id\":\"156\",\"name\":\"CAR ACCESSORIES\",\"logo\":\"fa-car\",\"groupId\":\"1002\"},{\"id\":\"157\",\"name\":\"MOTORBIKE PARTS\",\"logo\":\"fa-motorcycle\",\"groupId\":\"1002\"},{\"id\":\"158\",\"name\":\"MOTORBIKE ACCESSORIES\",\"logo\":\"fa-motorcycle\",\"groupId\":\"1002\"}]},{\"id\":\"2004\",\"name\":\"MEN, WOMEN AND OCCASSIONS\",\"desc\":\"men-women\",\"logo\":\"fa-users\",\"priority\":\"4\",\"categories\":[{\"id\":\"1160\",\"name\":\"MEN\",\"logo\":\"fa-male\",\"groupId\":\"2004\"},{\"id\":\"1163\",\"name\":\"WOMEN\",\"logo\":\"fa-female\",\"groupId\":\"2004\"},{\"id\":\"2162\",\"name\":\"WEDDING\",\"logo\":\"fa-info-circle\",\"groupId\":\"2004\"},{\"id\":\"2167\",\"name\":\"UNIFORMS\",\"logo\":\"fa-info-circle\",\"groupId\":\"2004\"}]},{\"id\":\"2005\",\"name\":\"AGRICULTURAL AND FARM PRODUCTS\",\"desc\":\"COMINGSOON\",\"logo\":\"fa-info-circle\",\"priority\":null,\"categories\":[]}],\"error\":false}";
            if (!TextUtils.isEmpty(s)) {
                //Change json placeholder categories to class
                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray array = json.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject ob = array.getJSONObject(i);
                        Category c = new Gson().fromJson(ob.toString(), Category.class);
                        JSONArray array2 = ob.getJSONArray("categories");
                        for (int j = 0; j < array2.length(); j++) {
                            String ss = array2.getString(j);
                            Category cc = new Gson().fromJson(ss, Category.class);
                            c.addSubCategory(cc);
                        }
                        categories.add(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return categories;
    }

    //Called when back button is pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            killApp();
        }
    }

    //To kill app efficiently
    private void killApp(){
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        inflateCommonMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_account) {
            startActivity(new Intent(this, AccountActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onShoppingCartLoadedEvent(ShoppingCartLoadedEvent event) {
        super.onShoppingCartLoadedEvent(event);
    }

    @Subscribe
    public void onCartItemAddedEvent(CartItemAddedEvent event) {
        refreshCart();
    }

    @Subscribe
    public void onCartItemRemovedEvent(CartItemRemovedEvent event) {
        refreshCart();
    }

    @Subscribe
    public void onLoginErrorEvent(LoginErrorEvent event) {
        if(!event.wasInitiatedByUser && NetworkUtils.isConnected(this)){
            refreshSession();
        }
    }

    @Subscribe
    public void onAskToLoginEvent(AskToLoginEvent event) {
        super.onAskToLoginEvent(event);
    }

    @Subscribe
    public void onLogoutStatusEvent(LogoutStatusEvent event) {
        super.onLogoutStatusEvent(event);
    }

    @Subscribe
    public void onLoginDoneEvent(LoginDoneEvent event) {
        super.onLoginDoneEvent(event);
    }

    @Subscribe
    public void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
        super.onInterntStatusChangedEvent(event);
    }

    @Subscribe
    public void onCategoryLoadedEventEvent(CategoryLoadedEvent event) {
        List<Category> categories = event.categories;
        if(categories != null && !categories.isEmpty()){
            adapter = new CategoryAdapter(this, categories);
            mRecyclerView.setAdapter(adapter);
            adapter.collapseAllParents();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return WishListFragment.newInstance();
                case 2:
                    return OrdersFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
