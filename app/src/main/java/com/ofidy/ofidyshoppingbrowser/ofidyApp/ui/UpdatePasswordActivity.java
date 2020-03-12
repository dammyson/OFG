package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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

public class UpdatePasswordActivity extends AppCompatActivity {

    // UI references.
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.email_layout1)
    TextInputLayout inputLayoutEmail;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.password2)
    EditText mPasswordView2;
    @Bind(R.id.password3)
    EditText mPasswordView3;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    // @Bind(R.id.email_layout)
    //TextInputLayout inputLayoutEmail;
    @Bind(R.id.password_layout1)
    TextInputLayout inputLayoutPassword;
    @Bind(R.id.password_layout2)
    TextInputLayout inputLayoutPassword2;

    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
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
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email);// && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.send)
    protected void attemptPasswordChange() {
        String email= mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password1 = mPasswordView2.getText().toString();
        String password2 = mPasswordView3.getText().toString();
        String passString1= new String(password1);
        String passString2 = new String(password2);

        boolean cancel = false;
        View focusView = null;

        if (!validateEmail(email)) {
            focusView = mEmailView;
            cancel = true;
        }

        if (!validatePassword(password)) {
            focusView = mPasswordView;
            cancel = true;
        }
        if (!validatePassword(password1)) {
            focusView = mPasswordView2;
            cancel = true;
        }
        if (!validatePassword(password2)) {
            focusView = mPasswordView3;
            cancel = true;
        }
        boolean check =passString1.equals(passString2);
        if(!check){
            inputLayoutPassword2.setError("Passwords do not Match");
            cancel=false;
        }

        if (cancel) {
            requestFocus(focusView);//focusView.requestFocus();
        } else {
            showProgress(true);
            new ChangePassword().execute(email,password, password1,password2);
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    class ChangePassword extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(final String... params) {
            result = null;
            final String action = "changePassword";
            System.out.println("............................................testings ==");
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", "customer")
                        .add("email", params[0])
                        .add("currentpwd", params[1])
                        .add("newpwd", params[2])
                        .add("pwdcheck", params[3])
                        .build();
                Request request = new Request.Builder()
                        .url(ConfigHelper.getConfigValue(UpdatePasswordActivity.this, "api_url"))
                        .post(formBody)
                        .build();
                Response response = Ofidy.getOkHttpClient().newCall(request).execute();
                result = response.body().string();
                //UserPrefs.getInstance(LoginActivity.this).setString(UserPrefs.Key.UNAME, params[0]);
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            showProgress(false);
            System.out.println("............................................testings = = = =" + result);
            if(TextUtils.isEmpty(result)){
                Snackbar.make(mPasswordView, "Network error", Snackbar.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                System.out.println("............................................testings = = =");
                if (!json.getBoolean("error")) {
                    if(json.getString("data").equals("Password changed")){
                        Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        EditText errorView = mPasswordView;
                        TextInputLayout errorLayout = inputLayoutPassword;
                        errorView.requestFocus();
                        errorLayout.setError(json.getString("data"));

                    }

                }
            }catch (Exception e){
                Snackbar.make(mPasswordView, "Internal application error", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
