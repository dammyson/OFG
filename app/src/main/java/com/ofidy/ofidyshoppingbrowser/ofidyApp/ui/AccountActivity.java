package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadCustomerEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LogoutEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Customer;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.AddressFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment.ProfileFragment;
import com.squareup.otto.Subscribe;

import butterknife.Bind;

import static com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider.getBus;

public class AccountActivity extends BaseActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private String TITLES[] = {"Profile", "Address"};
    private Customer customer;

    @Bind(R.id.profile_image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_account);
        if(!AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN)){
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra(BundleKeys.FROM_ACTIVITY, true);
            startActivity(i);
            finish();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        customer = new Gson().fromJson(UserPrefs.getInstance(this).getString(UserPrefs.Key.BODY), Customer.class);
        if(customer == null){
            if(AppState.getInstance(this).getBoolean(AppState.Key.GUEST)){
                customer = new Customer();
                customer.setCurrency(UserPrefs.getInstance(this).getString(UserPrefs.Key.CURRENCY));
                customer.setId(UserPrefs.getInstance(this).getString(UserPrefs.Key.ID));
                customer.setSid(UserPrefs.getInstance(this).getString(UserPrefs.Key.SID));
            }
            else {
                getBus().post(new LogoutEvent(true));
                finish();
            }
        }
        getBus().post(new LoadCustomerEvent(true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        inflateCommonMenu(menu);
        return true;
    }

    /**
     * A placeholder fragment containing a views for account page.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            switch (i){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_address, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_orders, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_edit_account, container, false);
            }
            return rootView;
        }
    }

    /**
     * A pageradapter for switching tabs.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    return AddressFragment.newInstance();
                case 2:
                    return PlaceholderFragment.newInstance(position);
                default:
                    return ProfileFragment.newInstance();
            }
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

    @Subscribe
    public void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
        super.onInterntStatusChangedEvent(event);
    }

}
