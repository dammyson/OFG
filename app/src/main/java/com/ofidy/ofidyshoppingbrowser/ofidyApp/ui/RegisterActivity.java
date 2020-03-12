package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.RegisterDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.RegisterStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Currency;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity  extends BaseActivity{

    // UI references.
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.username)
    EditText mUsernameView;
    @Bind(R.id.first_name)
    EditText mFirstNameView;
    @Bind(R.id.last_name)
    EditText mLastNameView;
    @Bind(R.id.mobile)
    EditText mMobileView;
    @Bind(R.id.terms)
    CheckBox mTermsView;
    @Bind(R.id.email_layout)
    TextInputLayout inputLayoutEmail;
    @Bind(R.id.password_layout)
    TextInputLayout inputLayoutPassword;
    @Bind(R.id.lname_layout)
    TextInputLayout inputLayoutLastName;
    @Bind(R.id.fname_layout)
    TextInputLayout inputLayoutFirstName;
    @Bind(R.id.uname_layout)
    TextInputLayout inputLayoutUsername;
    @Bind(R.id.mobile_layout)
    TextInputLayout inputLayoutMobile;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.gender_male)
    RadioButton mMale;
    @Bind(R.id.gender_female)
    RadioButton mFemale;
    @Bind(R.id.currency)
    Spinner mCurrency;
    private boolean fromActivity;
    String gender = "M";
    private ArrayList<Currency> currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            fromActivity = bundle.getBoolean(BundleKeys.FROM_ACTIVITY);
        }
        ButterKnife.bind(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptRegister();
                return true;
            }
            return false;
        });
        mMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                gender = "M";
        });
        mFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                gender = "F";
        });

        currencies = OfidyDB.getInstance(this).getCurrencies();
        if(currencies != null && !currencies.isEmpty()){
            System.out.println("................................cur..............." +currencies.size());
            List<String> li = new ArrayList<>(currencies.size());
            for(Currency c : currencies)
                li.add(c.getName());
            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, li);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mCurrency.setAdapter(adapter);
        }
        else{
            currencies = new ArrayList<>();
            String[] curs = getResources().getStringArray(R.array.currency);
            String[] curs_val = getResources().getStringArray(R.array.currency_value);
            int i = 0;
            for(String s: curs){
                currencies.add(new Currency("0", s, curs_val[i]));
                i++;
            }
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!fromActivity){
                    Intent i = new Intent(this, MainActivity.class);
                    String transitionName = getString(R.string.splash_transition);

                    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
                    ActivityCompat.startActivity(this, i, transitionActivityOptions.toBundle());;
                }
                finish();
                return true;
        }
        return false;
    }

    @OnClick(R.id.sign_in)
    protected void haveAccount() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.sign_up_button)
    protected void attemptRegister() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String fname = mFirstNameView.getText().toString();
        String lname = mLastNameView.getText().toString();
        String uname = mUsernameView.getText().toString();
        String mobile = mMobileView.getText().toString();
        int cur = mCurrency.getSelectedItemPosition();
        String curr = "USD";
        if(currencies != null && !currencies.isEmpty()){
            curr = currencies.get(cur).getCode();
        }

        boolean cancel = false;
        View focusView = null;

        if(!mTermsView.isChecked()){
            focusView = mTermsView;
            cancel = true;
            Snackbar.make(imageView, "You have to accept our terms and conditions to continue", Snackbar.LENGTH_SHORT).show();
        }
        if (!validatePassword(password)) {
            focusView = mPasswordView;
            cancel = true;
        }
        if (!validateEmail(email)) {
            focusView = mEmailView;
            cancel = true;
        }
        if (!validateEmail(email)) {
            focusView = mEmailView;
            cancel = true;
        }
        if(TextUtils.isEmpty(uname)){
            focusView = mUsernameView;
            inputLayoutUsername.setError("You must enter a username");
            cancel = true;
        }
        if(TextUtils.isEmpty(fname)){
            focusView = mFirstNameView;
            inputLayoutFirstName.setError("Please enter your first name");
            cancel = true;
        }
        if(TextUtils.isEmpty(lname)){
            focusView = mLastNameView;
            inputLayoutLastName.setError("Please enter your last name");
            cancel = true;
        }
        if (cancel) {
            requestFocus(focusView);
        } else {
            showProgress(true);
            sendRegisterRequest(email, password, fname, lname, uname, gender, mobile, curr);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty() || password.length() < 6) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void sendRegisterRequest(String email, String password,  String fname, String lname, String uname,
                                     String sex, String mobile, String currency) {
        getBus().post(new RegisterStartEvent(email, password, fname, lname, uname, "ofidy", "1", "1" ,"1990", sex,
                "", mobile, currency));
    }

    @Subscribe
    public void onLoginDoneEvent(RegisterDoneEvent event) {
        UserPrefs prefs = UserPrefs.getInstance(this);
        prefs.setString(UserPrefs.Key.ID, event.id);
        prefs.setString(UserPrefs.Key.BODY, event.body);
        prefs.setString(UserPrefs.Key.CURRENCY, "NGN");
        if(!fromActivity) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }

    @Subscribe
    public void onLoginErrorEvent(LoginErrorEvent event) {
        Crashlytics.logException(event.error);
        showProgress(false);
        Snackbar.make(imageView, event.error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }
}

