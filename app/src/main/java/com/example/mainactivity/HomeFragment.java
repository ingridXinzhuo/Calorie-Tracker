package com.example.mainactivity;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {
    TextView welcome;
    View vMain;
    TextView textViewGoalHint;
    EditText editTextSetGoal;
    Button setGoalButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_home, container, false);
        textViewGoalHint = (TextView) vMain.findViewById(R.id.textViewGoalHint);
        editTextSetGoal = (EditText) vMain.findViewById(R.id.editTextSetGoal);
        setGoalButton = (Button) vMain.findViewById(R.id.setGoalButton);


        Appuser user = (Appuser) getActivity().getIntent().getSerializableExtra("user");
        welcome = vMain.findViewById(R.id.Welcome);
        welcome.setText("Hello, " + user.getFirstname());

        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateReportAsyn addNewReport = new CreateReportAsyn();
                addNewReport.execute();
            }
        });


        return vMain;
    }

    private class CreateReportAsyn extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentTime = sdf.format(date);

            Appuser user =(Appuser) getActivity().getIntent().getSerializableExtra("user");
            String res = RestClient.findByUseridAndReportdate(user.getUserid(),currentTime);
            int reportId = Integer.parseInt(RestClient.calculateTotalReport()) + 1;
            String result = "";
            if(res.equals("[]")) {
                Report report = new Report(reportId,currentTime,"0","0","0",
                        editTextSetGoal.getText().toString(),user);
                RestClient.generateReport(report);
                result = "New report added";
                return result;
            }else{
                String reportObj = "";
                JSONArray jsonArr = null;
                try {
                    jsonArr = new JSONArray(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArr.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reportObj =  jsonObject.toString();

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                Report report = gson.fromJson(reportObj, Report.class);
                report.setCalorieGoalToday(editTextSetGoal.getText().toString());
                RestClient.updateReport(report);
                result = "Report is updated";
                return result;
            }


        }
        @Override
        protected void onPostExecute (String result) {
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}