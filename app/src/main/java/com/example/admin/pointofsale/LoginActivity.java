package com.example.admin.pointofsale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Module.HttpClient;
import Module.Popup;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.AsyncTask;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button button = (Button) findViewById(R.id.button_LogIn);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                EditText txtUserName = (EditText) findViewById(R.id.editText_UserName);
                EditText txtPassword = (EditText) findViewById(R.id.editText_Password);

                if (txtUserName.getText().toString().isEmpty()) {
                    txtUserName.setError("Enter User Name");return;
                } else if (txtPassword.getText().toString().isEmpty()) {
                    txtPassword.setError("Enter Password");return;
                }

                asyncTask.execute();

            }
        });
    }


    AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

        @Override
        protected String doInBackground(Void... params) {
            try {
                EditText txtUserName = (EditText) findViewById(R.id.editText_UserName);
                EditText txtPassword = (EditText) findViewById(R.id.editText_Password);

                String url = "http://172.168.1.15/POS/WebAPI.php?Type=LoginValidation&UserId="+txtUserName.getText().toString()+"&Password="+txtPassword.getText().toString()+"";
                HttpClient client = new HttpClient();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Type", "LoginValidation")
                        .addFormDataPart("UserId", txtUserName.getText().toString())
                        .addFormDataPart("Password", txtPassword.getText().toString())
                        .build();

                Response response = client.get(url,requestBody);

                if (!response.isSuccessful()) {
                    return null;
                }else {
                    String result=response.body().string();
                    JSONObject reader = new JSONObject(result);
                    JSONArray nameTable = reader.getJSONArray("LoginValidation");
                    JSONObject item = nameTable.getJSONObject(0);
                    String Status=item.getString("Status");

                    if (Status.toString().equals("Valid"))
                    {
                        Intent Main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(Main);

                        finish();
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Popup.showDefaultMessage(LoginActivity.this,"Invalied User Login.");
                            }
                        });

                    }
                }

            } catch (Exception e) {
//                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Popup.showDefaultMessage(LoginActivity.this, "Error founded. Please Contact Administrator.");
                    }
                });

            }



            return null;
        }



    };





//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
//    }
//
//
//    private void hideSystemUI() {
//        // Enables regular immersive mode.
//        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
//        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE
//                        // Set the content to appear under the system bars so that the
//                        // content doesn't resize when the system bars hide and show.
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        // Hide the nav bar and status bar
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
//    }
//
//    private void showSystemUI() {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//    }
}
