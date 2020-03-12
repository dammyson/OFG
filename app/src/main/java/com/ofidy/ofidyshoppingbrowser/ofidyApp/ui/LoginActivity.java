package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class LoginActivity extends BaseActivity {

    // UI references.
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.email_layout)
    TextInputLayout inputLayoutEmail;
    @Bind(R.id.password_layout)
    TextInputLayout inputLayoutPassword;
    @Bind(R.id.image)
    ImageView imageView;
    private boolean fromActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            fromActivity = bundle.getBoolean(BundleKeys.FROM_ACTIVITY);
        }
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!fromActivity){
                    Intent i = new Intent(this, MainActivity.class);
                    String transitionName = getString(R.string.splash_transition);

                    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
                    ActivityCompat.startActivity(this, i, transitionActivityOptions.toBundle());
                    if(!AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) &&
                            !AppState.getInstance(this).getBoolean(AppState.Key.GUEST)){
                        getBus().post(new GuestLoginStartEvent(true));
                    }
                }
                finish();
                return true;
        }
        return false;
    }

    @OnClick(R.id.sign_up)
    protected void haveAccount() {
        Intent i = new Intent(this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(BundleKeys.FROM_ACTIVITY, fromActivity);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.fb)
    protected void onClick() {
        //loginButton.performClick();
    }


    @OnClick(R.id.sign_in_button)
    protected void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!validatePassword(password)) {
            focusView = mPasswordView;
            cancel = true;
        }

        if (!validateEmail(email)) {
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            requestFocus(focusView);//focusView.requestFocus();
        } else {
            showProgress(true);
            sendLoginRequest(email, password);
        }
    }

    @OnClick(R.id.guest)
    protected void guestLogin() {
        if(AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) &&
                AppState.getInstance(this).getBoolean(AppState.Key.GUEST)) {
            if(!fromActivity){
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            finish();
            return;
        }
        showProgress(true);
        getBus().post(new GuestLoginStartEvent(true));
    }

    @OnClick(R.id.forgot_password)
    protected void forgotPassword() {
        Intent i = new Intent(this, ForgotActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email);// && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void sendLoginRequest(String email, String password) {
        getBus().post(new LoginStartEvent(email, password, true));
    }

    @Subscribe
    public void onLoginDoneEvent(LoginDoneEvent event) {
        UserPrefs prefs = UserPrefs.getInstance(this);
        prefs.setString(UserPrefs.Key.ID, event.id);
        prefs.setString(UserPrefs.Key.BODY, event.body);
        prefs.setString(UserPrefs.Key.EMAIL, event.email);
        prefs.setString(UserPrefs.Key.PASSWORD, event.password);
        prefs.setString(UserPrefs.Key.SID, event.sid);
        prefs.setString(UserPrefs.Key.CURRENCY, event.currency);
        if(!fromActivity) {
            Intent intent = new Intent(this, com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }

    @Subscribe
    public void onLoginDoneEvent(GuestLoginDoneEvent event) {
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
        EditText errorView = mPasswordView;
        TextInputLayout errorLayout = inputLayoutPassword;
        if ("NotFoundError".equals(event.error.toString())) {
            errorView = mEmailView;
            errorLayout = inputLayoutEmail;
        }
        errorView.requestFocus();
        errorLayout.setError(event.error.getMessage());
    }

    @Subscribe
    public void onLoginErrorEvent(GuestLoginErrorEvent event) {
        Crashlytics.logException(event.error);
        showProgress(false);
        EditText errorView = mPasswordView;
        TextInputLayout errorLayout = inputLayoutPassword;
        if ("NotFoundError".equals(event.error.toString())) {
            errorView = mEmailView;
            errorLayout = inputLayoutEmail;
        }
        errorView.requestFocus();
        errorLayout.setError(event.error.getMessage());
    }
}


