package com.drag.partner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private LinearLayout rootView;
    private TextInputLayout emailLayout, passwordLayout;
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private Button loginView;
    private InputMethodManager imm;
    private SharedPreferences pref;
    private ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_login);
        initViews();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                attemptLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isTokenValid()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private boolean isTokenValid() {
        String exp = pref.getString("expires", "");
        long time = System.currentTimeMillis();
        long expires;
        try {
            expires = Long.parseLong(exp);
        } catch (NumberFormatException nfe) {
            expires = 0;
        }
        return time < expires;
    }

    private void attemptLogin() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        emailLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            focusView = emailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            focusView = passwordView;
            cancel = true;
        } else if (password.length() > 0 && password.length() < 5) {
            focusView = passwordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            focusView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        } else {
            if (isConnectedToInternet()) {
                pd = ProgressDialog.show(this, "", "Logging in...", true, false);
                EndPointInterface service = APIUtils.getAPIService(LoginActivity.this);
                service.authSignIn(email, password, "partner").enqueue(new Callback<Partner>() {
                    @Override
                    public void onResponse(@NonNull Call<Partner> call, @NonNull Response<Partner> response) {
                        pd.dismiss();
                        if (response.body() != null) {
                            if (response.body().res()) {
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString("token", response.body().token().token());
                                edit.putString("expires", response.body().token().expires());
                                edit.putString("dbObj", new Gson().toJson(response.body().token().partner()));
                                edit.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                displayResponse(response.body().response());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Partner> call, @NonNull Throwable t) {
                        pd.dismiss();
                        Log.e(TAG + " On Failure", t.getMessage());
                        Snackbar.make(rootView, "Something went wrong. Please try again later!", Snackbar.LENGTH_LONG).show();
                    }
                });
            } else
                Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void displayResponse(String message) {
        switch (message) {
            case "Invalid Password":
                passwordLayout.setError("Password not valid");
                passwordView.requestFocus();
                break;
            case "Email Not Registered":
                emailLayout.setError("Email address not registered");
                emailView.requestFocus();
                break;
            case "Invalid Credentials":
                emailLayout.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                passwordLayout.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initViews() {
        rootView = findViewById(R.id.login_activity_layout);
        emailLayout = findViewById(R.id.login_email_layout);
        emailView = findViewById(R.id.login_email);
        passwordLayout = findViewById(R.id.login_password_layout);
        passwordView = findViewById(R.id.login_password);
        loginView = findViewById(R.id.login_button);
    }
}