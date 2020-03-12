package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendPaypalVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.OrderInvoice;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;

public class PaystackActivity extends BaseActivity {

    public static final int MY_SCAN_REQUEST_CODE = 1;
    public static final String US = "93KXD9JD8JVZA";
    public static final String UK = "DGMYBJ9GE6ZVA";

    ProgressDialog dialog;
    private OrderInvoice orderInvoice;
    DecimalFormat precision = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_paypal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        orderInvoice = bundle.getParcelable(BundleKeys.ORDER);
        if (orderInvoice == null)
            finish();

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyBrowser());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        String url = "https://ofidy.com/devtwo/drop4me/userdashboard/pay.php?email="+UserPrefs.getInstance(this).getString(UserPrefs.Key.EMAIL) +"&amount="+ precision.format(orderInvoice.getFullBill())+"&ref="+orderInvoice.getInvoiceId()+"&fName="+UserPrefs.getInstance(this).getString(UserPrefs.Key.FIRST_NAME)+"&lName="+UserPrefs.getInstance(this).getString(UserPrefs.Key.LAST_NAME);


        myWebView.loadUrl(url);

    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals("http://confirm_activity.com/")){
                Intent i = new Intent(PaystackActivity.this, Confimation.class);
                i.putExtra("ref", 1111);
                startActivity(i);
            }


            view.loadUrl(url);
            return true;
        }
    }



   /* private class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equalsIgnoreCase("www.ofidy.com/payment-complete.com")){
                getBus().post(new SendPaypalVerifyEvent());
            }
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            view.loadUrl(request.getUrl().toString());
            return true;
        }

    }*/



    @Override
    public void onPause() {
        super.onPause();
    }

    @Subscribe
    public void onSendSimplePayTokenDoneEvent(final SendSimplePayTokenDoneEvent event) {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        Snackbar.make(toolbar, event.message, Snackbar.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("message",event.message);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Subscribe
    public void onLoadSendSimplePayTokenErrorEvent(final SendSimplePayTokenErrorEvent event) {
        //mRetry.setVisibility(View.VISIBLE);
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        Snackbar.make(toolbar, event.message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        getBus().post(new SendPaypalVerifyEvent());
        super.onBackPressed();
    }
}