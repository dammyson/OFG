package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadShoppingCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendBankTansferCancelEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendBankTransferCancelDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendBankVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.OrderInvoice;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

public class BankActivity extends BaseActivity {

    public static final long NOTIFY_INTERVAL = 1000 * 60 * 2;
    private Handler mHandler= new Handler();
    private Timer mTimer = null;
    String payc;
    @Bind(R.id.sub_total)
    TextView mSubTotal;
    @Bind(R.id.bankname)
    TextView mBank;
    @Bind(R.id.invoice_number)
    TextView mInvoiceNumber;
    @Bind(R.id.pay)
    FancyButton mPay;
    @Bind(R.id.retry)
    FancyButton mRetry;
    String result;
    private Context mContext;
    private ProgressDialog progress;

    private OrderInvoice orderInvoice;
    DecimalFormat precision = new DecimalFormat("0.00");
    int counter = 0;
    int counte = 0;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_bank);
        getBus().post(new SendBankVerifyEvent());

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Urgent")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Click Button to cancel Payment or end checkout process to continue shopping")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        orderInvoice = bundle.getParcelable(BundleKeys.ORDER);
        if (orderInvoice == null)
            finish();
        mBank.setText("Zenith Bank");
        mInvoiceNumber.setText(orderInvoice.getInvoiceId());
        double fullbill= Math.round(orderInvoice.getFullBill());
        mSubTotal.setText(orderInvoice.getCurrency() + precision.format(fullbill));
    }


    @OnClick(R.id.retry)
    protected void onPay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                BankActivity.this);
        builder.setTitle("Sample Alert");
        builder.setMessage("Are you sure you want to cancel Payment or end checkout process to continue shopping");
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(BankActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        progress = new ProgressDialog(BankActivity.this);
                        progress.setOwnerActivity(BankActivity.this);
                        progress.setMessage("Cancelling Payment & Restoring Cart....");
                        progress.show();
                        getBus().post(new SendBankTansferCancelEvent());
                    }
                });
        builder.show();

    }


    @OnClick(R.id.pay)
    protected void onPayNotification() {
        progress = new ProgressDialog(BankActivity.this);
        progress.setOwnerActivity(BankActivity.this);
        progress.setMessage("Sending Payment Notfication....");
        progress.show();
        getBus().post(new SendBankVerifyEvent());
    }

    @OnClick(R.id.home)
    protected void onHome() {
        Intent intent = new Intent(BankActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getBus().post(new LoadShoppingCartEvent(true));
        finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /* @Override
     protected void onPause() {
         super.onPause();
         payc = UserPrefs.getInstance(getBaseContext()).getString(UserPrefs.Key.PayC);
         if (payc.equalsIgnoreCase("notclicked")) {
             launch();
         } else {

         }


     }*/
    private void launch(){
        count = 1;
        if(counter < 4){


            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTesk(), 0, NOTIFY_INTERVAL);

            counter ++;
        }
    }


    private void BankPay() {
        Intent intent = new Intent(this, BankConfimation.class);
        intent.putExtra(BundleKeys.ORDER, orderInvoice);
        startActivity(intent);
    }


   /* @Subscribe
    public void onSendSimplePayTokenDoneEvent(final SendBankTransferCancelDoneEvent event) {

        Snackbar.make(toolbar, event.message, Snackbar.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("message",event.message);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }*/

    @Subscribe
    public void onSendBankTransferCancelDoneEvent(final SendBankTransferCancelDoneEvent event) {
        if(progress != null && progress.isShowing())
            progress.dismiss();
        Intent intent = new Intent(BankActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getBus().post(new LoadShoppingCartEvent(true));
        finish();


    }


    @Subscribe
    public void onSendBankVerifyEvent(final SendSimplePayTokenDoneEvent event) {
       /* if(progress != null && progress.isShowing())
            progress.dismiss();
        Intent intent = new Intent(BankActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*///

    }

    class TimeDisplayTimerTesk extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if(counte < 6){


                        showNotification(getApplicationContext());

                        counte ++;
                    }


                }
            });
        }


    }

    private void showNotification(Context context) {

        Intent activityIntent = new Intent(context, BankActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setColor(Color.GREEN);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentTitle("Please Click The PAYMENT MADE Button");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentText("Please click the Button once you make bank transfer");
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }


}