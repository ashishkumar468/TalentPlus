package com.example.talentplusapplication;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.example.talentplusapplication.Proxy.RegistrationInput;
import com.example.talentplusapplication.Proxy.ResponseIsUserExist;
import com.example.talentplusapplication.Proxy.ResponseRegistration;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_SMS};
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Dialog dialogOTP;
    private CountDownTimer countDownTimer;


    private EditText edt_f_name;
    private EditText edt_l_name;
    private EditText edt_phone;
    private EditText edt_password;

    private String f_name;
    private String l_name;
    private String phone;

    private AppCompatButton btn_next;
    private LinearLayout mCoordinatorLayout;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registartion);

        mSharedPreferences=getSharedPreferences(Constants.Shared_Pref_Name,MODE_PRIVATE);

        initialization();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                if (dialogOTP != null)
                    dialogOTP.cancel();

                edt_f_name.setVisibility(View.GONE);
                edt_l_name.setVisibility(View.GONE);
                edt_phone.setVisibility(View.GONE);
                edt_password.setVisibility(View.VISIBLE);

                // OnVerificationStateChangedCallbacks

                Log.d(TAG, "onVerificationCompleted:" + credential);

                Toast.makeText(RegisterActivity.this, "mobile number verified successfully", Toast.LENGTH_LONG).show();

                // signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
//                Toast.makeText(RegisterActivity.this, "onVerificationFailed", Toast.LENGTH_LONG).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                Toast.makeText(RegisterActivity.this, "OTP send", Toast.LENGTH_LONG).show();
                // Save verification ID and resending token so we can use them later
               /* mVerificationId = verificationId;
                mResendToken = token;*/

                // ...
            }
        };


        TextView link_login = findViewById(R.id.link_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }

    private void initialization() {

        mCoordinatorLayout = findViewById(R.id.scroll_view);
        edt_f_name = findViewById(R.id.edt_f_name);
        edt_l_name = findViewById(R.id.edt_l_name);
        edt_phone = findViewById(R.id.edt_phone_number);
        edt_password = findViewById(R.id.edt_password);


        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_password.getVisibility() == View.GONE) {
                    if (validateName(edt_f_name.getText().toString(), edt_f_name)) {

                        if (validateName(edt_l_name.getText().toString(), edt_l_name)) {

                            if (validatePhoneNumber(edt_phone.getText().toString())) {
                                if (new Utility(RegisterActivity.this).isConnectingToInternet()) {
                                    onCallIsUserExistAPI(edt_phone.getText().toString());
                                } else {
                                    onShowToast("Internet connection is not available");
                                }

                            }


                        }
                    }

                } else {
                    if (validateName(edt_password.getText().toString(), edt_password)) {

                        onCallRegisterAPI();

                    }
                }


//
//
            }
        });


    }

    private void onCallRegisterAPI() {

        edt_password.setEnabled(false);

        RegistrationInput mRegistrationInput = new RegistrationInput("",

                edt_f_name.getText().toString(), edt_l_name.getText().toString(), edt_password.getText().toString(),
                edt_phone.getText().toString(),
                "", "", ""
        );

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);


        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("fName", mRegistrationInput.getfName());
        mJsonObject.addProperty("lName", mRegistrationInput.getlName());
        mJsonObject.addProperty("password", mRegistrationInput.getPassword());
        mJsonObject.addProperty("contactNo", mRegistrationInput.getContactNo());


        mApiInterface.registration(mJsonObject).enqueue(new Callback<ResponseRegistration>() {
            @Override
            public void onResponse(Call<ResponseRegistration> call, Response<ResponseRegistration> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("200")) {

                        onShowToast(response.body().getDescription());
//                        Intent mIntent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
                        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
                        mEditor.putString(Constants.USER_ID,""+response.body().getUserDto().getUserId());
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body().getUserDto());
                        mEditor.putString(Constants.USER_DTO, json);
                        mEditor.apply();
                        finish();
                    } else {
                        onShowToast(response.body().getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRegistration> call, Throwable t) {

                onShowToast("Something is wrong");
//                                Intent mIntent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
//                                startActivity(mIntent);
//                                finish();
            }
        });
    }

    private void onCallIsUserExistAPI(String phone) {

        onOpenDialogueForWait("Please wait");
        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("contactNo", phone);

        mApiInterface.isUserExist(phone).enqueue(new Callback<ResponseIsUserExist>() {
            @Override
            public void onResponse(Call<ResponseIsUserExist> call, Response<ResponseIsUserExist> response) {
                onCloseDialogueForWait();
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("UAE-005")) {

                        onShowToast(response.body().getDescription());
                        if (new Utility(RegisterActivity.this).isConnectingToInternet()) {

                            if (!hasPermissions(RegisterActivity.this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS, PERMISSION_ALL);
                            } else {
                                Log.e(TAG, "update call " + edt_phone.getText().toString());
                                updateProfile(edt_phone.getText().toString());
                            }

                            View view = RegisterActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                        } else {
                            showInternetSnackBar();
                        }
                    } else if (response.body().getCode().equals("200")) {
                        onShowToast(response.body().getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIsUserExist> call, Throwable t) {
                onCloseDialogueForWait();
                onShowToast("Something is wrong");
            }
        });


    }



    private ProgressDialog dialog;

    private void onOpenDialogueForWait(String message) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.show();
    }

    private void onCloseDialogueForWait() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void updateProfile(String Number) {

        Log.e(TAG, "number " + Number);

        dialogOTP = new Dialog(RegisterActivity.this, R.style.Theme_Dialog);
        dialogOTP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOTP.setCancelable(false);
        dialogOTP.setContentView(R.layout.otp_screen);
        ((TextView) dialogOTP.findViewById(R.id.txt_mob)).setText("+91 " + Number);

        ((Button) dialogOTP.findViewById(R.id.sendOTP)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + Number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        RegisterActivity.this,               // Activity (for callback binding)
                        mCallbacks);


                countDownTimer = new CountDownTimer(30000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {
                        ((TextView) dialogOTP.findViewById(R.id.txt_retry)).setVisibility(View.VISIBLE);

                        ((TextView) dialogOTP.findViewById(R.id.txt_retry)).setText("" + String.format("%d sec",
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        ((TextView) dialogOTP.findViewById(R.id.txt_retry)).setEnabled(true);
                        ((TextView) dialogOTP.findViewById(R.id.txt_retry)).setText("we are unable to verify your number click here for Retry");

                    }
                }.start();

                ((Button) dialogOTP.findViewById(R.id.sendOTP)).setVisibility(View.GONE);

            }
        });


        ((TextView) dialogOTP.findViewById(R.id.txt_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                dialogOTP.cancel();


            }
        });


        dialogOTP.show();


    }

    private void showInternetSnackBar() {
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, "Internet not available", Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });

        snackbar.show();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validatePhoneNumber(String phone_number) {
        if (phone_number.isEmpty() || !Patterns.PHONE.matcher(phone_number).matches()) {
            edt_phone.setError("enter a valid phone number");
            return false;
        } else {
            edt_phone.setError(null);
            return true;
        }
    }

    private boolean validateName(String name, EditText edt) {
        if (name.isEmpty()) {
            edt.setError("field should not empty");
            return false;
        } else {
            edt.setError(null);
            return true;
        }
    }

}
