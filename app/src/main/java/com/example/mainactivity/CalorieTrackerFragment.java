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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalorieTrackerFragment extends Fragment {
    Button btnTrackCalorie;
    TextView textViewDetail;
    View vM;
    List<Food> consumptedFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vM = inflater.inflate(R.layout.fragment_calorie_tracker, container, false);
        textViewDetail = (TextView) vM.findViewById(R.id.textViewDetail);
        btnTrackCalorie = (Button) vM.findViewById(R.id.btnTrackCalorie);
        consumptedFood = new ArrayList<>();

        btnTrackCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackCalorie trackCalorie = new TrackCalorie();
                trackCalorie.execute();
            }
        });


        return vM;
    }

    private class TrackCalorie extends AsyncTask<Void, Void, Report> {


        @Override
        protected Report doInBackground(Void... voids) {
            Appuser user =(Appuser) getActivity().getIntent().getSerializableExtra("user");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String curTimeC = sdf.format(date);
            String consumptionAll = RestClient.findConsumptionByUseridAndDate(user.getUserid().toString(),curTimeC);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(consumptionAll);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonArray.length();i++){
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(i).getJSONObject("foodid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String foodConsum = jsonObject.toString();
                Gson gson = new Gson();
                Food foodCons = (Food)gson.fromJson(foodConsum,Food.class);
                consumptedFood.add(foodCons);
            }

            int totalCalorieCon = 0;
            for(Food fooEaten:consumptedFood){
                totalCalorieCon = totalCalorieCon + Integer.parseInt(fooEaten.getCalorieAmount());
            }


            double totalCalBurned = Double.parseDouble(RestClient.calcuTotalBurned(user.getUserid().toString()));


            String res = RestClient.findByUseridAndReportdate(user.getUserid(),curTimeC);
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
            report.setTotalCalorieBurned(String.valueOf(totalCalBurned));
            report.setTotalCalorieConsumed(String.valueOf(totalCalorieCon));
            RestClient.updateReport(report);
            return report;
        }


        @Override
        protected void onPostExecute(Report details) {

            textViewDetail.setText("Set calorie is: " + details.getCalorieGoalToday() + ", total steps taken is : " + details.getTotalStepsTaken() + " , total calorie consumed: " + details.getTotalCalorieConsumed() + " , total calorie burned: " + details.getTotalCalorieBurned() + " .");

        }
    }
}
