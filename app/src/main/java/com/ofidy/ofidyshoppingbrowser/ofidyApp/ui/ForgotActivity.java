package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.ConfigHelper;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotActivity extends AppCompatActivity {

    // UI references.
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.username)
    EditText mUsernameView;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.email_layout)
    TextInputLayout inputLayoutEmail;
    @Bind(R.id.password_layout)
    TextInputLayout inputLayoutPassword;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!TextUtils.isEmpty(UserPrefs.getInstance(this).getString(UserPrefs.Key.UNAME)))
            mUsernameView.setText(UserPrefs.getInstance(this).getString(UserPrefs.Key.UNAME));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }


    @OnClick(R.id.send)
    protected void attemptLogin() {
        String email = mEmailView.getText().toString();
        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(username)) {
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            requestFocus(focusView);//focusView.requestFocus();
        } else {
            showProgress(true);
            new LoginTask().execute(email, username);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    class LoginTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(final String... params) {
            result = null;
            final String action = "forgot";
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", "customer")
                        .add("email", params[0])
                        .add("username", params[1])
                        .build();
                Request request = new Request.Builder()
                        .url(ConfigHelper.getConfigValue(ForgotActivity.this, "api_url"))
                        .post(formBody)
                        .build();
                Response response = Ofidy.getOkHttpClient().newCall(request).execute();
                result = response.body().string();
                System.out.println("..............................forgot = " + result);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (TextUtils.isEmpty(result)) {
                Snackbar.make(mEmailView, "Network error", Snackbar.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                if (json.has("data")) {
                    Toast.makeText(ForgotActivity.this, json.getString("data"), Toast.LENGTH_LONG).show();
                    exit(json.getString("data"));
                } else if(json.has("message")){
                    Toast.makeText(ForgotActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                    exit(json.getString("message"));}
            } catch (Exception e) {
                Snackbar.make(mEmailView, "Internal application error", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    public void exit(String Message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Password  Reset..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.logo_ofidy);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(ForgotActivity.this, UpdatePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
