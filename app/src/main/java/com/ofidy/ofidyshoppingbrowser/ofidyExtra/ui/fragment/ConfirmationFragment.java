package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderBillDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderBillEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.CheckoutActivity;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Damilola on 10/18/2018.
 */

public class ConfirmationFragment extends BaseFragment {

    @Bind(R.id.total)
    TextView mTotal;
    @Bind(R.id.payment_method)
    TextView mPaymentMethod;
    @Bind(R.id.shipping_method)
    TextView mShippingMethod;
    @Bind(R.id.shipping_address)
    TextView mShippingAddress;
    @Bind(R.id.billing_address)
    TextView mBillingAddress;

    private List<Address> addresses;

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    public static ConfirmationFragment newInstance() {
        ConfirmationFragment fragment = new ConfirmationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_confirmation, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshValues();
    }

    private void refreshValues(){
        double t = 0;
        if(((CheckoutActivity)getActivity()).paymentMethodSelected > -1)
            switch (((CheckoutActivity)getActivity()).paymentMethodSelected){
                case 0:
                    mPaymentMethod.setText("PayPal");
                    break;
                case 1:
                    mPaymentMethod.setText("Paystack");
                    break;
                case 2:
                    mPaymentMethod.setText("Bank Transfer");
                    break;
                case 3:
                    mPaymentMethod.setText("Rave");
                    break;
            }
        if(((CheckoutActivity)getActivity()).shippingMethodValues != null) {
            mShippingMethod.setText(((CheckoutActivity) getActivity()).shippingMethodValues
                    .get(((CheckoutActivity) getActivity()).shippingMethodSelected));
            t = ((CheckoutActivity) getActivity()).shippingMethodValuesD.
                    get(((CheckoutActivity) getActivity()).shippingMethodSelected);
        }
        if(((CheckoutActivity)getActivity()).total > 0) {
            t += ((CheckoutActivity) getActivity()).total ;
            DecimalFormat precision = new DecimalFormat("0.00");
            mTotal.setText("Item(s) Cost = " + Ofidy.getInstance().getCurrency() + precision.format(t));
        }
        addresses = OfidyDB.getInstance(getContext()).getAddresses();
        if(!addresses.isEmpty()) {
            List<String> spinnerArray = new ArrayList<>();
            for (Address address : addresses) {
                StringBuilder sb = new StringBuilder();
                if (!TextUtils.isEmpty(address.getAddressLine1()))
                    sb.append(address.getAddressLine1());
                if (!TextUtils.isEmpty(address.getAddressLine2()))
                    sb.append(", ").append(address.getAddressLine2());
                if (!TextUtils.isEmpty(address.getCity()))
                    sb.append(address.getCity());
                if (!TextUtils.isEmpty(address.getCountry()))
                    sb.append(", ").append(address.getCountry());
                spinnerArray.add(sb.toString());
            }
            if(((CheckoutActivity)getActivity()).billingAddressSelected > 0)
                mBillingAddress.setText(spinnerArray.get(((CheckoutActivity)getActivity()).billingAddressSelected - 1));
            else
                mBillingAddress.setText("");
            if(((CheckoutActivity)getActivity()).shippingAddressSelected > 0)
                mShippingAddress.setText(spinnerArray.get(((CheckoutActivity)getActivity()).shippingAddressSelected - 1));
            else
                mShippingAddress.setText("");
        }
        getBus().post(new GetOrderBillEvent());
    }

    @OnClick(R.id.proceed)
    protected void onProceedClick(){
        String billAdd;
        String shipAdd;
        String payMethod = null;
        int shipMethod = 0;
        if(((CheckoutActivity)getActivity()).billingAddressSelected <= 0){
            Snackbar.make(mTotal, "Please select billing address to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            billAdd = addresses.get(((CheckoutActivity)getActivity()).billingAddressSelected - 1).getId();
        }
        if(((CheckoutActivity)getActivity()).shippingAddressSelected <= 0){
            Snackbar.make(mTotal, "Please select shipping address to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            shipAdd = addresses.get(((CheckoutActivity)getActivity()).shippingAddressSelected - 1).getId();
        }
        if(((CheckoutActivity)getActivity()).shippingMethodSelected < 0){
            Snackbar.make(mTotal, "Please select shipping method to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            switch (((CheckoutActivity)getActivity()).shippingMethodSelected){
                case 0:
                    shipMethod = 20;
                    break;
                case 1:
                    shipMethod = 45;
                    break;
                case 2:
                    shipMethod = 55;
                    break;
            }
        }
        if(((CheckoutActivity)getActivity()).paymentMethodSelected <= -1){
            Snackbar.make(mTotal, "Please select a valid payment method to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            switch (((CheckoutActivity)getActivity()).paymentMethodSelected){
                case 0:
                    payMethod = "PayPal";
                    break;
                case 1:
                    payMethod = "Paystack";
                    break;
                case 2:
                    payMethod = "Bank Transfer";
                    break;
                case 3:
                    payMethod = "Rave";
                    break;
            }
        }
        if(!TextUtils.isEmpty(billAdd) && !TextUtils.isEmpty(shipAdd) &&
                !TextUtils.isEmpty(payMethod) && shipMethod > 0){
            getBus().post(new LoadPaymentInfoEvent(billAdd, shipAdd, shipMethod, payMethod,
                    ((CheckoutActivity)getActivity()).shippingComment));
        }
    }

    @Subscribe
    public void onGetOrderCostErrorEvent(final GetOrderBillDoneEvent event) {
        mTotal.setText(Ofidy.getInstance().getCurrency() + event.bill);
    }

}
