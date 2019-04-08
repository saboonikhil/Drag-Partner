package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ran.partner.model.Partner;
import com.ran.partner.network.APIUtils;
import com.ran.partner.network.EndPointInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddPartnerFragment extends DialogFragment {

    public static String TAG = "AddPartnerFragment";
    private Activity parentActivity;
    private View rootView;
    private Partner partner;
    private ImageButton closeView;
    private Button saveView;
    private InputMethodManager imm;
    private TextInputLayout passwordLayout, confirmPasswordLayout;
    private EditText nameView, emailView, contactView, alternateContactView, passwordView, confirmPasswordView;
    private String token, alternateContact, countryCode = "+91 ";
    private TextWatcher textWatcher1, textWatcher;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_add_partner, container, false);
        initViews();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePartner();
            }
        });

        contactView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                contactView.setFocusableInTouchMode(true);
                contactView.requestFocus();
                imm.showSoftInput(contactView, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        contactView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (contactView.getText().toString().length() == 0) {
                        contactView.setText(countryCode);
                        Selection.setSelection(contactView.getText(), contactView.getText().length());
                    }
                    imm.showSoftInput(contactView, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    if (contactView.getText().toString().equals(countryCode)) {
                        contactView.removeTextChangedListener(textWatcher);
                        contactView.getText().clear();
                        contactView.addTextChangedListener(textWatcher);
                    }
                }
            }
        });

        contactView.addTextChangedListener(textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+91 ")) {
                    contactView.setText(countryCode);
                    Selection.setSelection(contactView.getText(), contactView.getText().length());
                }
            }
        });

        alternateContactView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alternateContactView.setFocusableInTouchMode(true);
                alternateContactView.requestFocus();
                imm.showSoftInput(alternateContactView, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        alternateContactView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (alternateContactView.getText().toString().length() == 0) {
                        alternateContactView.setText(countryCode);
                        Selection.setSelection(alternateContactView.getText(), alternateContactView.getText().length());
                    }
                    imm.showSoftInput(alternateContactView, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    if (alternateContactView.getText().toString().equals(countryCode)) {
                        alternateContactView.removeTextChangedListener(textWatcher1);
                        alternateContactView.getText().clear();
                        alternateContactView.addTextChangedListener(textWatcher1);
                    }
                }
            }
        });

        alternateContactView.addTextChangedListener(textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+91 ")) {
                    alternateContactView.setText(countryCode);
                    Selection.setSelection(alternateContactView.getText(), alternateContactView.getText().length());
                }
            }
        });

        passwordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (passwordView.getText().toString().length() == 0) {
                        passwordLayout.setErrorTextAppearance(R.style.NoteDisplay);
                        passwordLayout.setError("Password should contain at least one number, " +
                                "one uppercase letter and one special character.");
                    }
                } else {
                    passwordLayout.setErrorEnabled(false);
                }
            }
        });

        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setErrorEnabled(false);
                confirmPasswordLayout.setErrorEnabled(false);
                confirmPasswordView.setText(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    passwordLayout.setErrorTextAppearance(R.style.NoteDisplay);
                    passwordLayout.setError("Password should contain at least one number, " +
                            "one uppercase letter and one special character.");
                }
            }
        });

        confirmPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }

    private void savePartner() {
        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String contact = contactView.getText().toString();
        String password = passwordView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();

        if (!TextUtils.isEmpty(alternateContactView.getText().toString()))
            alternateContact = alternateContactView.getText().toString();

        passwordLayout.setErrorEnabled(false);
        confirmPasswordLayout.setErrorEnabled(false);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            focusView = nameView;
            cancel = true;
        } else if (!name.matches(".*[a-zA-Z]+.*")) {
            focusView = nameView;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            focusView = emailView;
            cancel = true;
        } else if (TextUtils.isEmpty(contact)) {
            focusView = contactView;
            cancel = true;
        } else if (contact.length() > 4 && contact.length() < 14) {
            focusView = contactView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            focusView = passwordView;
            cancel = true;
        } else if (password.length() > 0 && password.length() < 6) {
            focusView = passwordView;
            cancel = true;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            focusView = confirmPasswordView;
            cancel = true;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordLayout.setError("Passwords do not match.");
            focusView = confirmPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            focusView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        } else {
            if (isConnectedToInternet()) {
                EndPointInterface service = APIUtils.getAPIService();
                Call<Partner> call = service.addPartner(
                        partner.getEmail(), token, name, email, contact, alternateContact, password);

                call.enqueue(new Callback<Partner>() {
                    @Override
                    public void onResponse(@NonNull Call<Partner> call, @NonNull Response<Partner> response) {
                        if (response.body() != null) {
                            if (response.body().res()) {
                                Toast.makeText(parentActivity, response.body().response(), Toast.LENGTH_LONG).show();
                                dismiss();
                            } else {
                                Toast.makeText(parentActivity, response.body().response(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Partner> call, @NonNull Throwable t) {
                        Log.e(TAG + " On Failure", t.getMessage());
                        Toast.makeText(parentActivity, "Something went wrong. Please try again later!", Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    private void initViews() {
        closeView = rootView.findViewById(R.id.add_partner_close);
        saveView = rootView.findViewById(R.id.add_partner_save);
        nameView = rootView.findViewById(R.id.add_partner_name);
        emailView = rootView.findViewById(R.id.add_partner_email);
        contactView = rootView.findViewById(R.id.add_partner_contact);
        alternateContactView = rootView.findViewById(R.id.add_partner_alternate_contact);
        passwordLayout = rootView.findViewById(R.id.add_partner_password_layout);
        passwordView = rootView.findViewById(R.id.add_partner_password);
        confirmPasswordLayout = rootView.findViewById(R.id.add_partner_confirm_password_layout);
        confirmPasswordView = rootView.findViewById(R.id.add_partner_confirm_password);
    }
}