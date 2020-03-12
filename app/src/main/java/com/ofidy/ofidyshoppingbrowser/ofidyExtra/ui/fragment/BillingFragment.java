package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddEditAddressStatusEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddressLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.AddressActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.BundleKeys;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.CheckoutActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by Damilola on 10/18/2018.
 */

public class BillingFragment extends BaseFragment {

    @Bind(R.id.billing_address)
    Spinner mBillingAddress;
    @Bind(R.id.shipping_address)
    Spinner mShippingAddress;
    @Bind(R.id.shipping_method)
    Spinner mShippingMethod;
    @Bind(R.id.shipping_method_flipper)
    ViewFlipper mShippingMethodFlipper;
    @Bind(R.id.shipping_comment)
    EditText mShippingComment;

    List<Address> addresses;

    public BillingFragment() {
        // Required empty public constructor
    }

    public static BillingFragment newInstance() {
        BillingFragment fragment = new BillingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_billing, container, false);
        ButterKnife.bind(this, view);
        addresses = OfidyDB.getInstance(getContext()).getAddresses();
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select an Address");
        for(Address address : addresses){
            StringBuilder sb = new StringBuilder();
            if(!TextUtils.isEmpty(address.getAddressLine1()))
                sb.append(address.getAddressLine1());
            if(!TextUtils.isEmpty(address.getAddressLine2()))
                sb.append(", ").append(address.getAddressLine2());
            if(!TextUtils.isEmpty(address.getCity()))
                sb.append(address.getCity());
            if(!TextUtils.isEmpty(address.getCountry()))
                sb.append(", ").append(address.getCountry());
            spinnerArray.add(sb.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBillingAddress.setAdapter(adapter);
        mBillingAddress.setSelection(((CheckoutActivity)getActivity()).billingAddressSelected);
        mShippingAddress.setAdapter(adapter);
        mShippingAddress.setSelection(((CheckoutActivity)getActivity()).shippingAddressSelected);
        if(((CheckoutActivity)getActivity()).shippingMethodValues != null){
            adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, ((CheckoutActivity)getActivity()).shippingMethodValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mShippingMethod.setAdapter(adapter);
            mShippingMethod.setSelection(((CheckoutActivity)getActivity()).shippingMethodSelected);
        }
        if(!TextUtils.isEmpty(((CheckoutActivity)getActivity()).shippingComment))
            mShippingComment.setText(((CheckoutActivity)getActivity()).shippingComment);
        mShippingComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((CheckoutActivity)getActivity()).shippingComment = editable.toString();
            }
        });
        return view;
    }

    private void addAddress(){
        Intent i = new Intent(getContext(), AddressActivity.class);
        i.putExtra(BundleKeys.FROM_ACTIVITY, true);
        i.putExtra(BundleKeys.CART_TOTAL, ((CheckoutActivity)getActivity()).total);
        startActivity(i);
        getActivity().finish();
    }

    @OnClick(R.id.next)
    protected void onNextClicked(){
        ((CheckoutActivity)getActivity()).changeTab(1);
    }

    @OnClick(R.id.add_billing_address)
    protected void onAddBillingAddressClicked(){
        addAddress();
    }

    @OnClick(R.id.add_shipping_address)
    protected void onAddShippingAddressClicked(){
        addAddress();
    }

    @OnItemSelected(R.id.shipping_address)
    protected void shippingAddressItemSelected(Spinner spinner, int position) {
        ((CheckoutActivity)getActivity()).shippingAddressSelected = position;
        if(position > 0){
            Address a = addresses.get(position - 1);
            ((CheckoutActivity)getActivity()).shippingAddressId = a.getId();
            mShippingMethodFlipper.setDisplayedChild(2);
            getBus().post(new GetOrderCostEvent(a.getId()));
        }
        ((CheckoutActivity)getActivity()).shippingAddressId = "";
    }

    @OnItemSelected(R.id.billing_address)
    protected void billingAddressItemSelected(Spinner spinner, int position) {
        ((CheckoutActivity)getActivity()).billingAddressSelected = position;
        if(position > 0){
            Address a = addresses.get(position - 1);
            ((CheckoutActivity)getActivity()).billingAddressId = a.getId();
        }
        else
            ((CheckoutActivity)getActivity()).billingAddressId = "";
    }

    @OnItemSelected(R.id.shipping_method)
    protected void shippingMethodItemSelected(Spinner spinner, int position) {
        ((CheckoutActivity)getActivity()).shippingMethodSelected = position;
    }

    @Subscribe
    public void onGetOrderCostDoneEvent(final GetOrderCostDoneEvent event) {
        //((CheckoutActivity)getActivity()).shippingMethodValues = event.values;
        mShippingMethodFlipper.setDisplayedChild(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, event.values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mShippingMethod.setAdapter(adapter);
    }

    @Subscribe
    public void onLoadCustomerAddressEvent(final AddressLoadedEvent event){
        addresses = event.addresses;
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select an Address");
        for(Address address : addresses){
            StringBuilder sb = new StringBuilder();
            if(!TextUtils.isEmpty(address.getAddressLine1()))
                sb.append(address.getAddressLine1());
            if(!TextUtils.isEmpty(address.getAddressLine2()))
                sb.append(", ").append(address.getAddressLine2());
            if(!TextUtils.isEmpty(address.getCity()))
                sb.append(address.getCity());
            if(!TextUtils.isEmpty(address.getCountry()))
                sb.append(", ").append(address.getCountry());
            spinnerArray.add(sb.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item,spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mShippingAddress.setAdapter(adapter);


    }

    @Subscribe
    public void onGetOrderCostErrorEvent(final GetOrderCostErrorEvent event) {
        mShippingMethodFlipper.setDisplayedChild(1);
        mShippingMethod.setAdapter(null);
    }

    @Subscribe
    public void onAddressLoadedEvent(AddressLoadedEvent event) {
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select an Address");
        for(Address address : event.addresses){
            StringBuilder sb = new StringBuilder();
            if(!TextUtils.isEmpty(address.getAddressLine1()))
                sb.append(address.getAddressLine1());
            if(!TextUtils.isEmpty(address.getAddressLine2()))
                sb.append(", ").append(address.getAddressLine2());
            if(!TextUtils.isEmpty(address.getCity()))
                sb.append(address.getCity());
            if(!TextUtils.isEmpty(address.getCountry()))
                sb.append(", ").append(address.getCountry());
            spinnerArray.add(sb.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBillingAddress.setAdapter(adapter);
        mBillingAddress.setSelection(((CheckoutActivity)getActivity()).billingAddressSelected);
        mShippingAddress.setAdapter(adapter);
        mShippingAddress.setSelection(((CheckoutActivity)getActivity()).shippingAddressSelected);
    }

    @Subscribe
    public void onAddEditAddressStatusEvent(AddEditAddressStatusEvent event) {
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select an Address");
        for(Address address : OfidyDB.getInstance(getContext()).getAddresses()){
            StringBuilder sb = new StringBuilder();
            if(!TextUtils.isEmpty(address.getAddressLine1()))
                sb.append(address.getAddressLine1());
            if(!TextUtils.isEmpty(address.getAddressLine2()))
                sb.append(", ").append(address.getAddressLine2());
            if(!TextUtils.isEmpty(address.getCity()))
                sb.append(address.getCity());
            if(!TextUtils.isEmpty(address.getCountry()))
                sb.append(", ").append(address.getCountry());
            spinnerArray.add(sb.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBillingAddress.setAdapter(adapter);
        mBillingAddress.setSelection(((CheckoutActivity)getActivity()).billingAddressSelected);
        mShippingAddress.setAdapter(adapter);
        mShippingAddress.setSelection(((CheckoutActivity)getActivity()).shippingAddressSelected);
    }

}
