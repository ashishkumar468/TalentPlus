package com.example.talentplusapplication;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
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

import com.example.talentplusapplication.Proxy.LoginInput;
import com.example.talentplusapplication.Proxy.ResponseIsUserExist;
import com.example.talentplusapplication.Proxy.ResponseRegistration;
import com.example.talentplusapplication.Proxy.UserDTOProxy;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    EditText edtPhone;
    EditText edtOtp;
    EditText edtPassword;
    EditText edtRePassword;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_SMS};
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Dialog dialogOTP;
    private CountDownTimer countDownTimer;
    private LinearLayout mCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        initialization();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                if (dialogOTP != null)
                    dialogOTP.cancel();

//                edtOtp.setVisibility(View.VISIBLE);
                edtPhone.setVisibility(View.GONE);

                edtPassword.setVisibility(View.VISIBLE);
                edtRePassword.setVisibility(View.VISIBLE);
                edtOtp.setVisibility(View.GONE);

                // OnVerificationStateChangedCallbacks

                Log.d(TAG, "onVerificationCompleted:" + credential);

                Toast.makeText(ForgotPassword.this, "mobile number verified successfully", Toast.LENGTH_LONG).show();

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

                Toast.makeText(ForgotPassword.this, "OTP send", Toast.LENGTH_LONG).show();
                // Save verification ID and resending token so we can use them later
               /* mVerificationId = verificationId;
                mResendToken = token;*/

                // ...
            }
        };


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initialization() {


        edtPhone = findViewById(R.id.number_email);
        edtOtp = findViewById(R.id.edtOTP);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);

        AppCompatButton button = findViewById(R.id.btn_next);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPhone.getVisibility() == View.VISIBLE) {
                    if (validatePhoneNumber(edtPhone.getText().toString())){


                        if (new Utility(ForgotPassword.this).isConnectingToInternet()) {
                            onCallIsUserExistAPI(edtPhone.getText().toString());
                        } else {
                            onShowToast("Internet connection is not available");
                        }




                    }

                } /*else if (edtOtp.getVisibility() == View.VISIBLE) {
                    edtPassword.setVisibility(View.VISIBLE);
                    edtRePassword.setVisibility(View.VISIBLE);
                    edtOtp.setVisibility(View.GONE);
                }*/ else if (edtPassword.getVisibility() == View.VISIBLE) {

                    if (validateName(edtPassword.getText().toString(),edtPassword)){

                        if (edtPassword.getText().toString().equals(edtRePassword.getText().toString())){

                            if (new Utility(ForgotPassword.this).isConnectingToInternet()) {
                                callWebServices();
                            } else {
                                onShowToast("Internet connection is not available");
                            }

                        }else {
                            onShowToast("Password is mismatch");
                        }
                    }


                }
            }
        });
    }
    UserDTOProxy mUserDTOProxy=new UserDTOProxy();
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

                    if (response.body().getCode().equals("200")) {

                        onShowToast(response.body().getDescription());
                        if (new Utility(ForgotPassword.this).isConnectingToInternet()) {


                            mUserDTOProxy= response.body().getUserDto();
                            if (!hasPermissions(ForgotPassword.this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(ForgotPassword.this, PERMISSIONS, PERMISSION_ALL);
                            } else {

                                Log.e(TAG, "update call " + edtPhone.getText().toString());
                                updateProfile(edtPhone.getText().toString());

                            }

                            View view = ForgotPassword.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }


                        } else {
                            showInternetSnackBar();
                        }
//                        Intent mIntent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();
                    } else if (response.body().getCode().equals("UAE-005")) {
                        onShowToast(response.body().getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIsUserExist> call, Throwable t) {

                onCloseDialogueForWait();
                onShowToast("Something is wrong");
//                                Intent mIntent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
//                                startActivity(mIntent);
//                                finish();
            }
        });


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


    private boolean validatePhoneNumber(String phone_number) {
        if (phone_number.isEmpty() || !Patterns.PHONE.matcher(phone_number).matches()) {
            edtPhone.setError("enter a valid phone number");
            return false;
        } else {
            edtPhone.setError(null);
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

    private void updateProfile(String Number) {

        Log.e(TAG, "number " + Number);

        dialogOTP = new Dialog(ForgotPassword.this, R.style.Theme_Dialog);
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
                        ForgotPassword.this,               // Activity (for callback binding)
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
//                dialogOTP.cancel();
                dialogOTP.dismiss();


            }
        });


        dialogOTP.show();


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

    private void callWebServices() {

        onOpenDialogueForWait("Please Wait");

//        LoginInput input = new LoginInput("2", edtPassword.getText().toString());
        LoginInput input = new LoginInput(""+mUserDTOProxy.getUserId(), edtPassword.getText().toString());

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);


        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("userId", input.getUserName());
        mJsonObject.addProperty("newPassword", input.getPassword());

        mApiInterface.forgotPassword(mJsonObject).enqueue(new Callback<ResponseRegistration>() {
            @Override
            public void onResponse(Call<ResponseRegistration> call, Response<ResponseRegistration> response) {
                onCloseDialogueForWait();
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("200")) {

                        onShowToast(response.body().getDescription());
                        Intent mIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        onShowToast(response.body().getDescription());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseRegistration> call, Throwable t) {
                onCloseDialogueForWait();
                onShowToast("Something is wrong");
//                                Intent mIntent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
//                                startActivity(mIntent);
//                                finish();
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
}
