package com.example.talentplusapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.talentplusapplication.Proxy.LoginInput;
import com.example.talentplusapplication.Proxy.ResponseLogin;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    public static final int LOGIN_REQUEST_CODE = 3;
    private SharedPreferences mSharedPreferences;

    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageView imageView;
    TextView txtUsername, txtEmail;
    EditText edt_username;
    EditText edt_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSharedPreferences = getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        edt_username = findViewById(R.id.input_phone_number);
        edt_password = findViewById(R.id.input_password);

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ?? " + loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        AppCompatButton button = findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);

                onOpenDialogueForWait("Please wait");
                ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
                LoginInput mLoginInput = new LoginInput(edt_username.getText().toString(),
                        edt_password.getText().toString());

                JsonObject mJsonObject = new JsonObject();
                mJsonObject.addProperty("userName", mLoginInput.getUserName());
                mJsonObject.addProperty("password", mLoginInput.getPassword());
//                Log.e("LoginActivity","String json " +mJsonObject.toString());

                mApiInterface.login(mJsonObject).enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        onCloseDialogueForWait();
                        if (response.isSuccessful()) {

                            if (response.body().getCode().equals("200")) {

                                onShowToast(response.body().getDescription());

//                                Intent mIntent=new Intent(LoginActivity.this,BottomNavigationActivity.class);
//                                startActivity(mIntent);
                                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                                mEditor.putString(Constants.USER_ID, "" + response.body().getUserDto().getUserId());
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body().getUserDto());
                                mEditor.putString(Constants.USER_DTO, json);
                                mEditor.apply();
                                finish();

                            } else {

                                button.setEnabled(true);
                                onShowToast(response.body().getDescription());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
//                        Toast.makeText(MyApplication.getAppContext(),"Something is wrong",Toast.LENGTH_SHORT).show();

                        onCloseDialogueForWait();
                        button.setEnabled(true);
                        onShowToast("Something is wrong");

                    }
                });

            }
        });

        TextView link_signup = findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

        TextView forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(mIntent);
//                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText(email);
//                            Picasso.with(LoginActivity.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

}
