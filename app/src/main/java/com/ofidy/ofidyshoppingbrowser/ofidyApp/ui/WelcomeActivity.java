package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadEnvironmentEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.NetworkUtils;
import com.ofidy.ofidyshoppingbrowser.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider.getBus;


public class WelcomeActivity extends AppCompatActivity {
    @Bind(R.id.image)
    ImageView imageView;

    String countryCodeValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);
        AppState.getInstance(this).setBoolean(AppState.Key.WELCOMED, true);
        if(NetworkUtils.isConnected(this))
            getBus().post(new LoadEnvironmentEvent(true));

        TelephonyManager tm = (TelephonyManager)this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        countryCodeValue = tm.getNetworkCountryIso();
    }


    @OnClick(R.id.start)
    protected void startShop(){
        if(!AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) &&
                !AppState.getInstance(this).getBoolean(AppState.Key.GUEST)){
           getBus().post(new GuestLoginStartEvent(true));
        }

        if(countryCodeValue.equalsIgnoreCase("ng")){
            Intent in = new Intent(WelcomeActivity.this, MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String transitionName = getString(R.string.splash_transition);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
            ActivityCompat.startActivity(this, in, transitionActivityOptions.toBundle());
            finish();

        }else{

            Intent in = new Intent(WelcomeActivity.this, LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String transitionName = getString(R.string.splash_transition);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
            ActivityCompat.startActivity(this, in, transitionActivityOptions.toBundle());
            finish();
        }



    }

    @OnClick(R.id.sign_up)
    protected void signUp(){
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String transitionName = getString(R.string.splash_transition);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
        ActivityCompat.startActivity(this, i, transitionActivityOptions.toBundle());
        finish();
    }
}
