package com.example.mainactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

        EditText username;
        EditText password;
        TextView hintWords;
        Button loginButton;
        Button registerButton;
        TextView loginRespond;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);
            hintWords = (TextView) findViewById(R.id.hintWords);
            loginButton = (Button) findViewById(R.id.loginButton);
            registerButton = (Button) findViewById(R.id.registerButton);
            loginRespond = (TextView) findViewById(R.id.loginRespond);


            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginAsyncTask loginAsy = new LoginAsyncTask();
                    loginAsy.execute();
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(i);
                }
            });

        }

        private class LoginAsyncTask extends AsyncTask<Void, Void, String>
        {
            String userName = username.getText().toString();
            String passWord = password.getText().toString();


            String hexStringInput = "";
            @Override
            protected String doInBackground (Void...params){
                try{
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(passWord.getBytes("UTF-8"));
                    StringBuffer hashContent = new StringBuffer();

                    for (int i = 0; i < hash.length; i++) {
                        String hex = Integer.toHexString(0xff & hash[i]);
                        if(hex.length() == 1) hashContent.append('0');
                        hashContent.append(hex);
                    }
                    hexStringInput =  hashContent.toString();
                } catch(Exception ex){
                    throw new RuntimeException(ex);
                }
                String checkResult = RestClient.checkLoginUser(userName,hexStringInput);
                String userString = "";

                try {
                    JSONArray jsonArray = new JSONArray(checkResult);
                    JSONObject jsonObject;
                    jsonObject = jsonArray.getJSONObject(0).getJSONObject("userid");
                    userString =  jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userString;

            }
            @Override
            protected void onPostExecute (String userString){
                if (!userString.equals("")) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    Appuser appUser = gson.fromJson(userString, Appuser.class);
                    //intent across activities(current,target)
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    //putExtra: current user
                    intent.putExtra("user",appUser);
                    startActivity(intent);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"The password or username is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

