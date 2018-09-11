package com.example.social_photo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import com.example.social_photo.NetworkTools.NetworkUtilities;
import com.example.social_photo.R;
import com.example.social_photo.Utils.SaveSharedPreference;
import com.example.social_photo.Utils.Utilities;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import com.example.social_photo.NetworkTools.FacebookOperation;

import static com.example.social_photo.NetworkTools.NetworkUtilities.sendToken;

public class Login extends AppCompatActivity {

    private LoginButton loginButton;
    private TextView textView;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    private String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_photos", "user_birthday","user_friends","read_insights","user_likes"));
        textView = findViewById(R.id.login_textView);
        textView.setText("Welcome to the Login Page!");

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                id = loginResult.getAccessToken().getUserId();
                SaveSharedPreference.setUserID(Login.this,AccessToken.getCurrentAccessToken().getUserId());
                SaveSharedPreference.setUserToken(Login.this, AccessToken.getCurrentAccessToken().getToken());
                String pippo = sendToken(AccessToken.getCurrentAccessToken().getToken(),SaveSharedPreference.getUserID(Login.this));
                Log.d("TAG", "PIPPO: " + pippo);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            SaveSharedPreference.setUserName(Login.this,object.getString(("name")));
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                request.executeAndWait();

                try {
                    Bitmap propic = FacebookOperation.getFacebookProfilePicture(id);
                    String propic_converted = Utilities.BitMapToString(propic);
                    SaveSharedPreference.setProfilePicture(Login.this, propic_converted);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Login.this, TabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

}
