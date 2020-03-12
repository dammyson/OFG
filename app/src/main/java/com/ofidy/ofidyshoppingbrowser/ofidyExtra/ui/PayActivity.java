package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayManager;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadShoppingCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendRavepayVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.Materials.model.OrderInvoice;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.CartDetailsAdapter;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

public class PayActivity extends BaseActivity {

    private static final int PAYSTACK_GATEWAY = 2;
    private static final int PAYPAL_GATEWAY = 1;

    // actual keys
    private static final String RAVE_PUBLICKEY = "FLWPUBK-4b44483b43cc5ef06550aaeea671010a-X";
    private static final String RAVE_SECRETKEY = "FLWSECK-426825ba86cc78cd7b4e7377c28d1f08-X";
    private OrderInvoice orderInvoice;
    private ProgressDialog progress;
// staging environment keys
    //private static final String RAVE_PUBLICKEY = "FLWPUBK-b221de926958a7f4af45e7e80e1d23a0-X";
    //private static final String RAVE_SECRETKEY = "FLWSECK-c35280c436ce09a6940741d808ef6484-X";

    @Bind(R.id.payment_method)
    TextView mText;
    @Bind(R.id.sub_total)
    TextView mSubTotal;
    @Bind(R.id.total)
    TextView mTotal;
    @Bind(R.id.shipping_cost)
    TextView mShipping;
    @Bind(R.id.other_cost)
    TextView mOther_cost;
    @Bind(R.id.cust_name)
    TextView mCustName;
    @Bind(R.id.shipping_address)
    TextView mShippingAddress;
    @Bind(R.id.billing_address)
    TextView mBillingAddress;
    @Bind(R.id.pay)
    FancyButton mPay;
    @Bind(R.id.retry)
    FancyButton mRetry;
    private String fullbill;
    private String token;
    private boolean paypalLive = true;


    @Bind(R.id.list)
    RecyclerView mPostList;
    private CartDetailsAdapter adapter;
    private List<Cart> mCarts;
    //ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_pay);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        orderInvoice = bundle.getParcelable(BundleKeys.ORDER);
        if(orderInvoice ==  null)
            finish();
        DecimalFormat precision = new DecimalFormat("0.00");
        // double othercharges=orderInvoice.getFullBill()-(orderInvoice.getProductsBill()+ orderInvoice.getShipBill());
        double othercharges1= orderInvoice.getOtherCost();
        double fullbill= Math.ceil(orderInvoice.getFullBill());
        mText.setText("Payment method is "+ orderInvoice.getPayMethod());
        mShipping.setText("Shipping = " + orderInvoice.getCurrency() + precision.format(orderInvoice.getShipBill()));
        mSubTotal.setText("Item(s) Cost = " + orderInvoice.getCurrency() + precision.format(orderInvoice.getProductsBill()));
        mOther_cost.setText("Other Charges = " + orderInvoice.getCurrency() + precision.format(othercharges1));
        mTotal.setText("Total = " + orderInvoice.getCurrency()  + precision.format(fullbill));
        mShippingAddress.setText(orderInvoice.getFullAddress());
        mCustName.setText(UserPrefs.getInstance(this).getString(UserPrefs.Key.FIRST_NAME) + " "+ UserPrefs.getInstance(this).getString(UserPrefs.Key.LAST_NAME));



        mCarts = Ofidy.cartItems;

        adapter = new CartDetailsAdapter(this, mCarts, R.layout.detail_list, view -> {
            int pos = mPostList.getChildLayoutPosition(view);
        });
        mPostList.setAdapter(adapter);
        mPostList.setLayoutManager(new LinearLayoutManager(this));
        mPostList.setItemAnimator(new DefaultItemAnimator());
        getBus().post(new LoadShoppingCartEvent(true));



    }


    @Subscribe
    public void onCartsLoadedEvent(ShoppingCartLoadedEvent event) {
        if(progress != null)
            progress.dismiss();
        mCarts = Ofidy.cartItems;
        adapter.notifyDataSetChanged();

    }




    @OnClick(R.id.pay)
    protected void onPay(){
        if(orderInvoice.getPayMethod().equals("PayPal"))
            payPay();
        else if (orderInvoice.getPayMethod().equals("Paystack"))
            paystack();
        else if (orderInvoice.getPayMethod().equals("Rave"))
            ravePay();
        else
            BankPay();

    }

    @OnClick(R.id.retry)
    protected void onRetry(){
        if(!TextUtils.isEmpty(token)) {
            mRetry.setVisibility(View.GONE);
            progress.show();
            getBus().post(new SendSimplePayTokenEvent(token, fullbill));
        }
    }

    private void paystack(){
        Intent intent = new Intent(this, PaystackActivity.class);
        intent.putExtra(BundleKeys.ORDER, orderInvoice);
        startActivityForResult(intent, PAYSTACK_GATEWAY);
    }

    private void payPay(){
        Intent intent = new Intent(this, PaypalActivity.class);
        intent.putExtra(BundleKeys.ORDER, orderInvoice);
        startActivityForResult(intent, PAYPAL_GATEWAY);
    }

    private void BankPay(){
        //  getBus().post(new SendBankVerifyEvent());
        UserPrefs prefs = UserPrefs.getInstance(PayActivity.this);
        Intent intent = new Intent(this, BankActivity.class);
        intent.putExtra(BundleKeys.ORDER, orderInvoice);
        startActivity(intent);
    }

    private void  ravePay(){
        //PayActivity payActivity = new PayActivity();
        //double fullbill= Math.round(orderInvoice.getFullBill());
        RavePayManager ravePayManager= new RavePayManager(PayActivity.this);
        ravePayManager.setAmount(Math.ceil(orderInvoice.getFullBill()));
        ravePayManager.setCountry(orderInvoice.getCountrySp());
        ravePayManager.setCurrency(orderInvoice.getCurrency());
        ravePayManager.setfName(UserPrefs.getInstance(this).getString(UserPrefs.Key.FIRST_NAME));
        ravePayManager.setlName(UserPrefs.getInstance(this).getString(UserPrefs.Key.LAST_NAME));
        ravePayManager.setEmail(UserPrefs.getInstance(this).getString(UserPrefs.Key.EMAIL));
        ravePayManager.setNarration(orderInvoice.getInvoiceId());
        ravePayManager.setPublicKey(RAVE_PUBLICKEY);
        ravePayManager.setSecretKey(RAVE_SECRETKEY);
        ravePayManager.setTxRef(orderInvoice.getInvoiceId());
        ravePayManager.acceptAccountPayments(false);
        ravePayManager.acceptCardPayments(true);
        ravePayManager.onStagingEnv(false);
        ravePayManager.withTheme(R.style.DefaultTheme);
        ravePayManager.initialize();

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        if(requestCode== PAYPAL_GATEWAY){
            if (resultCode == Activity.RESULT_OK){

            }else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            }
        }
        else if(requestCode == RaveConstants.RAVE_REQUEST_CODE && data!=null){
            String message = data.getStringExtra("response");
            Log.d("message from rave",message);
            if(resultCode == com.flutterwave.raveandroid.RavePayActivity.RESULT_SUCCESS) {
                mPay.setVisibility(View.GONE);
                mRetry.setVisibility(View.GONE);
                //Toast.makeText(this,"SUCCESS"+message,Toast.LENGTH_SHORT).show();
                progress = new ProgressDialog(PayActivity.this);
                progress.setOwnerActivity(PayActivity.this);
                progress.setMessage("Verifying payment details....");
                progress.show();
                getBus().post(new SendRavepayVerifyEvent(orderInvoice.getInvoiceId(),Math.ceil(orderInvoice.getFullBill())));

            }
            else if (resultCode == com.flutterwave.raveandroid.RavePayActivity.RESULT_ERROR){
                Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == com.flutterwave.raveandroid.RavePayActivity.RESULT_CANCELED){
                Toast.makeText(this,"CANCELLED ",Toast.LENGTH_SHORT).show();

            }

        }
        else{
            mPay.setVisibility(View.GONE);
            mRetry.setVisibility(View.GONE);
            super.onActivityResult(requestCode,resultCode,data);

        }


        // if (resultCode == Activity.RESULT_OK) {
        // if(requestCode == PAYPAL_GATEWAY) {

        //}
        //else{
        // mPay.setVisibility(View.GONE);
        // mRetry.setVisibility(View.GONE);
        //mText.setText();
        //}
        //}
        //else if (resultCode == Activity.RESULT_CANCELED) {
        //Log.i("paymentExample", "The user canceled.");
        //}

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onSendSimplePayTokenDoneEvent(final SendSimplePayTokenDoneEvent event) {
        if(progress != null && progress.isShowing())
            progress.dismiss();
        mPay.setVisibility(View.GONE);
        mRetry.setVisibility(View.GONE);
        mText.setText(event.message);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                PayActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Comfirmation");
        builder.setMessage(event.message);
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("message",event.message);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
        builder.show();


    }



    @Subscribe
    public void onLoadSendSimplePayTokenErrorEvent(final SendSimplePayTokenErrorEvent event) {
        mRetry.setVisibility(View.VISIBLE);
        if(progress != null  && progress.isShowing())
            progress.dismiss();
        Snackbar.make(mText, event.message, Snackbar.LENGTH_LONG).show();
    }
}
