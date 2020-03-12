package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddEditAddressEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddEditAddressStatusEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddGuestAddressEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadStateEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.StatesLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Customer;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Region;
import com.ofidy.ofidyshoppingbrowser.Materials.model.State;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class AddressActivity extends BaseActivity {

    //UI fields
    @Bind(R.id.flipper)
    ViewFlipper mFlipper;
    @Bind(R.id.address1)
    EditText mAddress1;
    @Bind(R.id.address1_layout)
    TextInputLayout mAddress1Layout;
    @Bind(R.id.address2)
    EditText mAddress2;
    @Bind(R.id.area)
    EditText mArea;
    @Bind(R.id.city)
    EditText mCity;
    @Bind(R.id.city_layout)
    TextInputLayout mCityLayout;
    @Bind(R.id.state)
    Spinner mState;
    @Bind(R.id.country)
    Spinner mCountry;
    @Bind(R.id.postcode)
    EditText mPostcode;
    @Bind(R.id.postcode_layout)
    TextInputLayout mPostcodeLayout;
    @Bind(R.id.type)
    Spinner mType;
    @Bind(R.id.description)
    EditText mDescription;
    @Bind(R.id.cor)
    RadioGroup mCor;
    @Bind(R.id.del)
    RadioGroup mDel;
    @Bind(R.id.primary)
    CheckBox mPrimary;
    @Bind(R.id.fname)
    EditText fname;
    @Bind(R.id.lname)
    EditText lname;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.phone)
    EditText phone;

    //Address object representing current address
    private Address address;
    private int addressType = 0;
    private String country = "0";
    //Selected state value
    private String state = "nonesel";
    private String email = "";
    private int selectedCuontry = -1;
    private ArrayList<Region> savedRegions;
    private List<State> states;
    private ArrayAdapter stateAdapter;

    private boolean fromCart;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_address);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(bundle.containsKey(BundleKeys.ADDRESS))
                address = bundle.getParcelable(BundleKeys.ADDRESS);
            if(bundle.containsKey(BundleKeys.FROM_ACTIVITY)) {
                fromCart = bundle.getBoolean(BundleKeys.FROM_ACTIVITY);
                total = bundle.getDouble(BundleKeys.CART_TOTAL);
            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (AppState.getInstance(this).getBoolean(AppState.Key.GUEST)) {
            mEmail.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            lname.setVisibility(View.VISIBLE);
            fname.setVisibility(View.VISIBLE);
        }

        savedRegions = OfidyDB.getInstance(this).getRegions();
        if(!savedRegions.isEmpty()){
            List<String> ar = new ArrayList<>(savedRegions.size());
            int i = 0;
            for(Region r : savedRegions) {
                ar.add(r.getName());
                if(address != null && !TextUtils.isEmpty(address.getCountry()) && address.getCountry().equals(r.getId())) {
                    selectedCuontry = i;
                    country = r.getId();
                }
                i++;
            }
            ArrayAdapter adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, ar);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mCountry.setAdapter(adapter);
        }
        else{
            String regs[] = getResources().getStringArray(R.array.region);
            int[] regs_val = getResources().getIntArray(R.array.region_value);
            int i = 0;
            for(String s : regs) {
                savedRegions.add(new Region(String.valueOf(regs_val[i]), s));
                if(address != null && !TextUtils.isEmpty(address.getCountry()) &&
                        address.getCountry().equals(String.valueOf(regs_val[i]))) {
                    selectedCuontry = i;
                    country = String.valueOf(regs_val[i]);
                }
                i++;
            }
        }
        if(selectedCuontry > -1) {
            mCountry.setSelection(selectedCuontry);
            getBus().post(new LoadStateEvent(country));
            stateAdapter.clear();
        }

        List<String> ar = new ArrayList<>();
        stateAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ar);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState.setAdapter(stateAdapter);

        if(address !=  null) {
            loadAddress(address);
            mPrimary.setVisibility(View.GONE);
        }
    }

    /**
     * Method called when country value is selected with spinner, sends value to server to retrieve
     * state values
     *
     * @param spinner Spinner view class
     * @param position value selected, integer
     */
    @OnItemSelected(R.id.country)
    protected void onRegionItemSelected(Spinner spinner, int position) {
        if(!savedRegions.isEmpty()) {
            country = savedRegions.get(position).getId();
        }
        else{
            int[] ar = getResources().getIntArray(R.array.region_value);
            country = String.valueOf(ar[position]);
        }
        getBus().post(new LoadStateEvent(country));
        stateAdapter.clear();

    }

    /**
     * Method called when state is selected with spinner
     *
     * @param spinner Spinner view class
     * @param position value selected, integer
     */
    @OnItemSelected(R.id.state)
    protected void onStateItemSelected(Spinner spinner, int position) {
        if(states != null)
            state = String.valueOf(states.get(position).getId());

    }

    /**
     * Load address values into UI
     *
     * @param address the Address object
     */
    private void loadAddress(Address address){
        if(!TextUtils.isEmpty(address.getAddressLine1()))
            mAddress1.setText(address.getAddressLine1());
        if(!TextUtils.isEmpty(address.getAddressLine2()))
            mAddress2.setText(address.getAddressLine2());
        if(!TextUtils.isEmpty(address.getAddressLine3()))
            mArea.setText(address.getAddressLine3());
        if(!TextUtils.isEmpty(address.getCity()))
            mCity.setText(address.getCity());
        if(!TextUtils.isEmpty(address.getPostcode()))
            mPostcode.setText(address.getPostcode());
        if(!TextUtils.isEmpty(address.getAddressDescription()))
            mDescription.setText(address.getAddressDescription());
        if(address.getAddressType() > 0) {
            mType.setSelection(address.getAddressType());
            addressType = 0;
        }
        if(!TextUtils.isEmpty(address.getIsCorrespondenceAddress()) &&
                address.getIsCorrespondenceAddress().equals("Y"))
            mCor.check(R.id.cor_yes);
        else
            mCor.check(R.id.cor_yes);
        if(!TextUtils.isEmpty(address.getIsDeliveryAddress()) &&
                address.getIsDeliveryAddress().equals("Y"))
            mDel.check(R.id.del_yes);
        else
            mCor.check(R.id.del_no);
    }

    /**
     * Send edited/new to server
     *
     */
    @OnClick(R.id.save)
    protected void sendAddress(){
        String addrLine1 = mAddress1.getText().toString();
        if(TextUtils.isEmpty(addrLine1)){
            mAddress1Layout.setError("You must enter a value for street");
            return;
        }
        String city = mCity.getText().toString();
        if(TextUtils.isEmpty(city)){
            mCityLayout.setError("You must enter a value for city");
            return;
        }
        String addrLine2 = mAddress2.getText().toString();
        String area = mArea.getText().toString();
        String desc = mDescription.getText().toString();
        String postcode = mPostcode.getText().toString();
        if(TextUtils.isEmpty(postcode)){
            mPostcodeLayout.setError("You must enter a value for postcode");
            return;
        }
        addressType = mType.getSelectedItemPosition() + 1;
        String cor;
        int checkedRadioButtonId = mCor.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            Snackbar.make(mCor, "Please indicate if address is for correspondence or not", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            if (checkedRadioButtonId == R.id.cor_yes)
                cor = "Y";
            else
                cor = "N";
        }
        String del;
        checkedRadioButtonId = mDel.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            Snackbar.make(mDel, "Please indicate if address is for delivery or not", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            if (checkedRadioButtonId == R.id.del_yes)
                del = "Y";
            else
                del = "N";
        }
        boolean isPrimary = false;
        if(mPrimary.isChecked())
            isPrimary = true;
        if (AppState.getInstance(this).getBoolean(AppState.Key.GUEST)) {
            email = mEmail.getText().toString();
            String fn = fname.getText().toString();
            if (TextUtils.isEmpty(fn)) {
                Snackbar.make(mCor, "Please enter your first name", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String ln = lname.getText().toString();
            if (TextUtils.isEmpty(ln)) {
                Snackbar.make(mCor, "Please enter your last name", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String p = phone.getText().toString();
            if (TextUtils.isEmpty(p)) {
                Snackbar.make(mCor, "Please enter your phone number", Snackbar.LENGTH_SHORT).show();
                return;
            }
            mFlipper.setDisplayedChild(1);
            getBus().post(new AddGuestAddressEvent(addrLine1, addrLine2, area, city, state, country, desc,
                    postcode, addressType, isPrimary, email, fn, ln, p));
        }
        else {
            mFlipper.setDisplayedChild(1);
            getBus().post(new AddEditAddressEvent(addrLine1, addrLine2, area, city, state, country, desc,
                    postcode, addressType, cor, del, isPrimary, address != null));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        inflateCommonMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(fromCart){
                   /* Intent intent = new Intent(this, CheckoutActivity.class);
                    intent.putExtra(BundleKeys.CART_TOTAL, total);
                    startActivity(intent);*/
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method called when edited/new address is successfully saved on server
     *
     * @param event an event class that carries successful message
     */
    @Subscribe
    public void onAddEditAddressStatusEvent(AddEditAddressStatusEvent event) {
        mFlipper.setDisplayedChild(0);
        if(!TextUtils.isEmpty(email)) {
            Customer c = new Gson().fromJson(UserPrefs.getInstance(this).getString(UserPrefs.Key.BODY), Customer.class);
            if(c != null){
                c.setEmail(email);
                UserPrefs.getInstance(this).setString(UserPrefs.Key.BODY, new Gson().toJson(c));
            }
        }
        Snackbar.make(mDel, event.message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Method called when states value for a country is gotten from the server
     *
     * @param event an event class that error check variable and states value
     */
    @Subscribe
    public void onStatesLoadedEvent(StatesLoadedEvent event) {
        if(event.error)
            Snackbar.make(mDel, event.message, Snackbar.LENGTH_SHORT).show();
        else {
            int selectedState = -1;
            List<String> ar = new ArrayList<>();
            int i = 0;
            for(State r : event.states) {
                ar.add(r.getName());
                if(address != null && !TextUtils.isEmpty(address.getState()) && address.getState().equals(r.getId())) {
                    selectedState = i;
                    state = String.valueOf(r.getId());
                }
                i++;
            }
            stateAdapter.addAll(ar);
            if(selectedState > -1)
                mState.setSelection(selectedState);
            states = event.states;
        }
    }

    @Subscribe
    public void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
        super.onInterntStatusChangedEvent(event);
    }

    /**
     * Called when back button is pressed in activity.
     */
    @Override
    public void onBackPressed() {
        if(fromCart){
           /* Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(BundleKeys.CART_TOTAL, total);
            startActivity(intent);*/
        }
        super.onBackPressed();
    }
}
