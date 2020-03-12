package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ofidy.ofidyshoppingbrowser.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

    }



    @OnClick(R.id.modapp)
    protected void startAPP(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.modbrow)
    protected void startBrowser(){
        Intent i = new Intent(this, LoginActivity.class);
         startActivity(i);
    }

}