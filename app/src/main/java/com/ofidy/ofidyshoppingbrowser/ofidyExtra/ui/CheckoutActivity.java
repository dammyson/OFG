package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.BillingFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.ConfirmationFragment;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment.PayFragment;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CheckoutActivity extends BaseActivity {

    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.fragment_container)
    LinearLayout container;
    @Bind(R.id.flipper)
    ViewFlipper flipper;

    public String billingAddressId;
    public String shippingAddressId;
    public String shippingComment = "";
    public int billingAddressSelected = 0;
    public int shippingAddressSelected = 0;
    public int shippingMethodSelected = -1;
    public int paymentMethodSelected = -1;
    public List<String> shippingMethodValues;
    public List<Double> shippingMethodValuesD;
    public double total;
    public boolean pod = false;
    public boolean simplepay = false;
    public boolean flupay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_checkout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(!bundle.containsKey(BundleKeys.CART_TOTAL))
            finish();
        total = bundle.getDouble(BundleKeys.CART_TOTAL);

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Billing"));
        tabLayout.addTab(tabLayout.newTab().setText("Payment Type"));
        tabLayout.addTab(tabLayout.newTab().setText("Confirmation"));

        //replace default fragment
        replaceFragment(BillingFragment.newInstance());

        //handling tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(billingAddressSelected == 0 && shippingAddressSelected == 0){
                    tab = tabLayout.getTabAt(0);
                    tab.select();
                }
                else if(paymentMethodSelected == -1 && tab.getPosition() > 1){
                    tab = tabLayout.getTabAt(1);
                    tab.select();
                }
                else if (tab.getPosition() == 0) {
                    replaceFragment(BillingFragment.newInstance());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(PayFragment.newInstance());
                } else {
                    replaceFragment(ConfirmationFragment.newInstance());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void changeTab(int index){
        new Handler().postDelayed(
                () -> tabLayout.getTabAt(index).select(), 100);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.checkout, menu);
//        return true;
//    }

    @Subscribe
    public void onGetOrderCostDoneEvent(final GetOrderCostDoneEvent event) {
        shippingMethodValues = event.values;
        shippingMethodValuesD = event.valuesD;
        shippingMethodSelected = 0;
        pod = event.pod;
        simplepay = event.simplepay;
    }

    @Subscribe
    public void onGetOrderCostErrorEvent(final GetOrderCostErrorEvent event) {
        shippingMethodValues = null;
        shippingMethodValuesD = null;
    }

    @Subscribe
    public void onLoadPaymentInfoEvent(final LoadPaymentInfoEvent event) {
        flipper.setDisplayedChild(2);
    }

    @Subscribe
    public void onLoadPaymentInfoErrorEvent(final LoadPaymentInfoErrorEvent event) {
        flipper.setDisplayedChild(1);
    }

    @OnClick(R.id.reload)
    protected void onProceedClick(){
        List<Address> addresses = OfidyDB.getInstance(this).getAddresses();
        String billAdd;
        String shipAdd;
        String payMethod = null;
        int shipMethod = 0;
        if(billingAddressSelected <= 0){
            Snackbar.make(toolbar, "Please select billing address to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            billAdd = addresses.get(billingAddressSelected - 1).getId();
        }
        if(shippingAddressSelected <= 0){
            Snackbar.make(toolbar, "Please select shipping address to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            shipAdd = addresses.get(shippingAddressSelected - 1).getId();
        }
        if(shippingMethodSelected < 0){
            Snackbar.make(toolbar, "Please select shipping method to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            switch (shippingMethodSelected){
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
        if(paymentMethodSelected <= -1){
            Snackbar.make(toolbar, "Please select a valid payment method to proceed", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else{
            switch (paymentMethodSelected){
                case 0:
                    payMethod = "PayPal";
                    break;
                case 1:
                    payMethod = "Paystack";
                    break;
                case 2:
                    payMethod = "BankTransfer";
                    break;
                case 3:
                    payMethod = "Rave";
                    break;
            }
        }
        if(!TextUtils.isEmpty(billAdd) && !TextUtils.isEmpty(shipAdd) &&
                !TextUtils.isEmpty(payMethod) && shipMethod > 0){
            getBus().post(new LoadPaymentInfoEvent(billAdd, shipAdd, shipMethod, payMethod,
                    shippingComment));
        }
    }

    @Subscribe
    public void onLoadPaymentInfoEvent(final LoadPaymentInfoDoneEvent event) {
        if(event.orderInvoice != null){
            //getBus().post(new LoadShoppingCartEvent(false));
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra(BundleKeys.ORDER, event.orderInvoice);
            intent.putExtra(BundleKeys.ADDRESS,
                    OfidyDB.getInstance(this).getAddresses().get(billingAddressSelected - 1).getFullAddress());
            intent.putExtra(BundleKeys.SHIPPING_METHOD,
                    shippingMethodValues.get(shippingMethodSelected));
            startActivity(intent);
            finish();
        }
    }

}
