package com.example.mainactivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";


    EditText registerUsername;
    EditText registerPassword;

    EditText registerFirstName;
    EditText registerSurName;
    EditText registerDOB;
    EditText registerEmail;
    EditText registerHeight;
    EditText registerWeight;
    RadioGroup registerGender;
    EditText registerAddress;
    EditText registerPostcode;
    TextView registerSpinner;
    Spinner registerLevelOfActivity;
    TextView registerStepPerMile;
    Button registerButton;
    private DatePickerDialog.OnDateSetListener appDateSetListener;
    private String gender = "M";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerPassword = (EditText) findViewById(R.id.registerPassword);

        registerFirstName = (EditText) findViewById(R.id.registerFirstName);
        registerSurName = (EditText) findViewById(R.id.registerSurName);
        registerDOB = (EditText) findViewById(R.id.registerDOB);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerHeight = (EditText) findViewById(R.id.registerHeight);
        registerWeight = (EditText) findViewById(R.id.registerWeight);
        registerGender = (RadioGroup) findViewById(R.id.registerGender);
        registerAddress = (EditText) findViewById(R.id.registerAddress);
        registerPostcode = (EditText) findViewById(R.id.registerPostcode);
        registerSpinner = (TextView) findViewById(R.id.registerSpinner) ;
        registerLevelOfActivity = (Spinner) findViewById(R.id.registerLevelOfActivity);
        registerStepPerMile = (TextView) findViewById(R.id.registerStepPerMile);

        registerDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        appDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        appDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyy/mm/dd/: " + month + "-" + day + "-" + year);

                String date = year + "-" + month + "-" + day;
                registerDOB.setText(date);
            }
        };

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registerLevelOfActivity.setAdapter(dataAdapter);

        registerGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRadioBtn();
            }

            private void selectRadioBtn() {
                RadioButton rb = (RadioButton)RegisterActivity.this.findViewById(registerGender.getCheckedRadioButtonId());
                gender = rb.getText().toString();
            }
        });



        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PostAsyncTask postAsyncTask=new PostAsyncTask();
                try {
                    postAsyncTask.execute(

                            registerUsername.getText().toString(),//0
                            getStringSHA256(registerPassword.getText().toString()),//1

                            registerFirstName.getText().toString(),//2
                            registerSurName.getText().toString(),//3

                            registerEmail.getText().toString(),//4
                            registerDOB.getText().toString(),//5
                            registerHeight.getText().toString(),//6
                            registerWeight.getText().toString(),//7
                            gender,//8
                            registerAddress.getText().toString(),//9
                            registerPostcode.getText().toString(),//10
                            registerLevelOfActivity.getSelectedItem().toString(),//11
                            registerStepPerMile.getText().toString()//12
                    );
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static String getStringSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashContent = no.toString(16);
        while (hashContent.length() < 32) {
            hashContent = "0" + hashContent;
        }
        return hashContent;



    }

    private class PostAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            int allUserNumber = Integer.parseInt(RestClient.countUserNumber());
            int userID = allUserNumber + 1;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentTime = simpleDateFormat.format(date);

            Appuser currentAppuser = new Appuser(userID,params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9], params[10], params[11],params[12]);
            Credential userNewCredential = new Credential(userID,params[0],params[1],currentTime,currentAppuser);
            boolean haveEmpty = false;
            for (String thisParam : params) {
                if (thisParam.isEmpty())
                    haveEmpty = true;

            }
            if(!haveEmpty) {

                RestClient.createUser(currentAppuser);
                RestClient.createCreUser(userNewCredential);

                return "Registered successfully";
            }
            else
            {
                return "Login FAILED, NULL value is not acceptable in any ";
            }

        }
        @Override
        protected void onPostExecute(String response) {

            Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}