package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.splash.KenBurnsView;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class SplashActivity extends AppCompatActivity {
    protected OkHttpClient mOkHttpClient = null;
    private static int SPLASH_TIME_OUT = 5000;
    private KenBurnsView mKenBurns;

    @Bind(R.id.imagelogo)
    ImageView imageView;
    String countryCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setAnimation();
        TelephonyManager tm = (TelephonyManager)this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        countryCodeValue = tm.getNetworkCountryIso();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        mKenBurns.setImageResource(R.drawable.splash_background);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i;
                if(! AppState.getInstance(SplashActivity.this).getBoolean(AppState.Key.WELCOMED)){
                    i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String transitionName = getString(R.string.splash_transition);
                    try {
                        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView, transitionName);
                        ActivityCompat.startActivity(SplashActivity.this, i, transitionActivityOptions.toBundle());
                    }
                    catch (Exception e){
                        startActivity(i);
                    }
                }
                else{

                          if(countryCodeValue.equalsIgnoreCase("ng")){
                              Intent in = new Intent(SplashActivity.this, MainActivity.class);
                              startActivity(in);
                          }else{
                              Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                              startActivity(in);
                          }

//                    i = new Intent(SplashActivity.this, HomeActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);


                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void setAnimation() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();

        findViewById(R.id.imagelogo).setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        findViewById(R.id.imagelogo).startAnimation(anim);
    }
}
