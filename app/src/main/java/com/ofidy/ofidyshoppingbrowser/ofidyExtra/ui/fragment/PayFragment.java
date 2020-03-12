package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.CheckoutActivity;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Damilola on 10/18/2018.
 */

public class PayFragment extends BaseFragment {

    @Bind(R.id.paystack_payment)
    RadioButton mPaystackPayment;
    @Bind(R.id.paypal_payment)
    RadioButton mPaypalPayment;
    @Bind(R.id.bt_payment)
    RadioButton mBTPayment;
    @Bind(R.id.flur_payment)
    RadioButton mRavePayment;
    @Bind(R.id.simplepay_layout)
    View mSimplepayLayout;
    @Bind(R.id.paypal_layout)
    View mPaypalLayout;
    @Bind(R.id.bt_layout)
    View mBTLayout;
    @Bind(R.id.flu_layout)
    View mFluLayout;

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance() {
        PayFragment fragment = new PayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, view);
        if(((CheckoutActivity)getActivity()).paymentMethodSelected > -1)
            switch (((CheckoutActivity)getActivity()).paymentMethodSelected){
                case 0:
                    mPaypalPayment.setChecked(true);
                    mPaystackPayment.setChecked(false);
                    mBTPayment.setChecked(false);
                    mRavePayment.setChecked(false);
                    break;
                case 1:
                    mPaystackPayment.setChecked(true);
                    mPaypalPayment.setChecked(false);
                    mBTPayment.setChecked(false);
                    mRavePayment.setChecked(false);
                    break;
                case 2:
                    mBTPayment.setChecked(true);
                    mPaystackPayment.setChecked(false);
                    mPaypalPayment.setChecked(false);
                    mRavePayment.setChecked(false);
                    break;
                case 3:
                    mRavePayment.setChecked(true);
                    mPaystackPayment.setChecked(false);
                    mPaypalPayment.setChecked(false);
                    mBTPayment.setChecked(false);
                    break;
            }
        if(Ofidy.getInstance().getCurrency().equals("NGN")){
            mPaypalPayment.setVisibility(View.GONE);
        }
        else{
            mSimplepayLayout.setVisibility(View.GONE);
            mBTLayout.setVisibility(View.GONE);
            mFluLayout.setVisibility(View.GONE);

        }
        return view;
    }

    @OnClick(R.id.next)
    protected void onNextClicked(){
        ((CheckoutActivity)getActivity()).changeTab(2);
    }

    @OnClick({R.id.paystack_payment, R.id.paypal_payment, R.id.bt_payment, R.id.flur_payment})
    public void radioGroupUpdate() {
        if(mPaypalPayment.isChecked()) {
            ((CheckoutActivity) getActivity()).paymentMethodSelected = 0;
            mPaystackPayment.setChecked(false);
            mBTPayment.setChecked(false);
            mRavePayment.setChecked(false);
        }
        else if(mPaystackPayment.isChecked()) {
            ((CheckoutActivity) getActivity()).paymentMethodSelected = 1;
            mPaypalPayment.setChecked(false);
            mBTPayment.setChecked(false);
            mRavePayment.setChecked(false);
        }
        else if(mBTPayment.isChecked()) {
            ((CheckoutActivity) getActivity()).paymentMethodSelected = 2;
            mPaystackPayment.setChecked(false);
            mPaypalPayment.setChecked(false);
            mRavePayment.setChecked(false);
        }
        else if(mRavePayment.isChecked()) {
            ((CheckoutActivity) getActivity()).paymentMethodSelected = 3;
            mPaystackPayment.setChecked(false);
            mPaypalPayment.setChecked(false);
            mBTPayment.setChecked(false);
        }
        else
            ((CheckoutActivity)getActivity()).paymentMethodSelected = -1;
    }

    @Subscribe
    public void onGetOrderCostDoneEvent(final GetOrderCostDoneEvent event) {
        if(!event.pod) {
            mBTLayout.setVisibility(View.GONE);
            mBTPayment.setChecked(false);
        }
        else{
            mBTLayout.setVisibility(View.VISIBLE);
        }
//        if(!event.simplepay) {
//            mSimplepayLayout.setVisibility(View.GONE);
//            mPaystackPayment.setChecked(false);
//        }
//        else
        mSimplepayLayout.setVisibility(View.VISIBLE);
        mFluLayout.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onGetOrderCostErrorEvent(final GetOrderCostErrorEvent event) {
        mBTPayment.setEnabled(true);
        mPaystackPayment.setEnabled(true);
        mFluLayout.setEnabled(true);
    }

}
