package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ofidy.ofidyshoppingbrowser.Materials.model.OrderInvoice;
import com.ofidy.ofidyshoppingbrowser.R;

import java.text.DecimalFormat;

import butterknife.Bind;

public class BankConfimation extends BaseActivity{

    @Bind(R.id.ship_to)
    TextView mShippingAddress;
    @Bind(R.id.bill_to)
    TextView mBillingAddress;


    private OrderInvoice orderInvoice;
    DecimalFormat precision = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_bank_confimation);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        orderInvoice = bundle.getParcelable(BundleKeys.ORDER);
        if (orderInvoice == null)
            finish();
        mShippingAddress.setText(orderInvoice.getFullAddress());
        mBillingAddress.setText(orderInvoice.getFullAddress());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
