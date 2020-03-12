package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.gson.Gson;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.CustomerLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadCustomerEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCustomerErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCustomerEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCustomerSuccessEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Currency;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Customer;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    @Bind(R.id.first_name)
    EditText mFirstName;
    @Bind(R.id.last_name)
    EditText mLastName;
    @Bind(R.id.currency)
    Spinner mCurrency;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.username)
    EditText mUsername;
    @Bind(R.id.mobile)
    EditText mMobile;
    @Bind(R.id.telephone)
    EditText mTelephone;
    @Bind(R.id.dob)
    EditText mDOB;

    @Bind(R.id.first_name_layout)
    TextInputLayout mFirstNameLayout;
    @Bind(R.id.last_name_layout)
    TextInputLayout mLastNameLayout;
    @Bind(R.id.mobile_layout)
    TextInputLayout mMobileLayout;
    @Bind(R.id.telephone_layout)
    TextInputLayout mTelephoneLayout;
    @Bind(R.id.gender_male)
    RadioButton mMale;
    @Bind(R.id.gender_female)
    RadioButton mFemale;
    ProgressDialog progress;

    String year = "1900";
    String month = "1";
    String day = "1";
    String gender = "M";
    int selectedCurrency = 0;
    private List<Currency> currencies;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_edit_account, container, false);
        ButterKnife.bind(this, view);
        mMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                gender = "M";
        });
        mFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                gender = "F";
        });
        if(AppState.getInstance(getContext()).getBoolean(AppState.Key.GUEST)){
            mFirstName.setVisibility(View.GONE);
            mLastName.setVisibility(View.GONE);
            mMobile.setVisibility(View.GONE);
            mTelephone.setVisibility(View.GONE);
            mDOB.setVisibility(View.GONE);
            mUsername.setText("guest");
            mUsername.setEnabled(false);
            mMale.setVisibility(View.GONE);
            mFemale.setVisibility(View.GONE);
        }

        getBus().post(new LoadCustomerEvent(true));
        loadCustomerInfo(new Gson().fromJson(UserPrefs.getInstance(getContext()).getString(UserPrefs.Key.BODY), Customer.class));
        return view;
    }

    private void loadCustomerInfo(Customer customer){
        if(!TextUtils.isEmpty(customer.getFirstName()))
            mFirstName.setText(customer.getFirstName());
        if(!TextUtils.isEmpty(customer.getLastName()))
            mLastName.setText(customer.getLastName());
        if(!TextUtils.isEmpty(customer.getMobile()))
            mMobile.setText(customer.getMobile());
        if(!TextUtils.isEmpty(customer.getTelephone()))
            mTelephone.setText(customer.getTelephone());
        if(!TextUtils.isEmpty(customer.getDob())) {
            String[] dob = customer.getDob().split("-");
            year = dob[0];
            month = dob[1];
            day = dob[2];
            mDOB.setText(customer.getDob());
        }
        mEmail.setText(customer.getEmail());
        mUsername.setText(customer.getUsername());
        if(!TextUtils.isEmpty(customer.getGender())){
            if(customer.getGender().equalsIgnoreCase("m") ||customer.getGender().equalsIgnoreCase("male")) {
                mMale.setChecked(true);
                mFemale.setChecked(false);
            }
            else {
                mFemale.setChecked(true);
                mMale.setChecked(false);
            }
        }
        currencies = OfidyDB.getInstance(getContext()).getCurrencies();
        if(currencies != null && !currencies.isEmpty()){
            List<String> li = new ArrayList<>(currencies.size());
            int a = 0;
            for(Currency c : currencies) {
                li.add(c.getName());
                if(c.getCode().equals(UserPrefs.getInstance(getContext()).getString(UserPrefs.Key.CURRENCY)))
                    selectedCurrency = a;
                a++;
            }
            ArrayAdapter adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, li);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mCurrency.setAdapter(adapter);
        }
        else{
            currencies = new ArrayList<>();
            String[] curs = getResources().getStringArray(R.array.currency);
            String[] curs_val = getResources().getStringArray(R.array.currency_value);
            int i = 0;
            for(String s: curs){
                currencies.add(new Currency("0", s, curs_val[i]));
                if(curs_val[i].equals(UserPrefs.getInstance(getContext()).getString(UserPrefs.Key.CURRENCY)))
                    selectedCurrency = i;
                i++;
            }
        }
        mCurrency.setSelection(selectedCurrency);
    }

    @OnClick(R.id.update_account)
    protected void onUpdateAccount(){
        String fname = mFirstName.getText().toString();
        String lname = mLastName.getText().toString();
        String mobile = mMobile.getText().toString();
        String telephone = mTelephone.getText().toString();
        String email = mEmail.getText().toString();
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setGender(gender);
        customer.setDob(year+"-"+month+"-"+day);
        int cur = mCurrency.getSelectedItemPosition();
        if(currencies != null && !currencies.isEmpty()){
            customer.setCurrency(currencies.get(cur).getCode());
        }
        else{
            customer.setCurrency(UserPrefs.getInstance(getContext()).getString(UserPrefs.Key.CURRENCY));
        }
        if(!TextUtils.isEmpty(fname))
            customer.setFirstName(fname);
        else{
            mFirstNameLayout.setError("First name cannot be empty");
            return;
        }
        if(!TextUtils.isEmpty(lname))
            customer.setLastName(lname);
        else{
            mLastNameLayout.setError("Last name cannot be empty");
            return;
        }
        if(!TextUtils.isEmpty(telephone) && !TextUtils.isEmpty(mobile)) {
            customer.setMobile(mobile);
            customer.setTelephone(telephone);
        }
        else{
            mMobileLayout.setError("Mobile and telephone fields cannot be both empty");
            return;
        }
        progress = ProgressDialog.show(getContext(), "Updating....",
                "Please wait", true);
        getBus().post(new UpdateCustomerEvent(customer));
    }

    @OnClick(R.id.dob)
    protected void onDateFieldClicked(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                R.style.AppTheme, datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select date of birth");
        datePicker.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = String.valueOf(selectedYear);
            month = String.valueOf(selectedMonth + 1);
            day = String.valueOf(selectedDay);
            mDOB.setText(day + "-" + month + "-" + year);

        }
    };

    @Subscribe
    public void onCustomerLoadedEvent(CustomerLoadedEvent event) {
        if(event.user != null){
            loadCustomerInfo(event.user);
        }
    }

    @Subscribe
    public void onUpdateCustomerEventError(UpdateCustomerErrorEvent event) {
        progress.dismiss();
        if(!TextUtils.isEmpty(event.message)){
            Snackbar.make(mFirstNameLayout, event.message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onUpdateCustomerEvent(UpdateCustomerSuccessEvent event) {
        progress.dismiss();
        Snackbar.make(mFirstNameLayout, "Customer info updated", Snackbar.LENGTH_SHORT).show();
    }
}
