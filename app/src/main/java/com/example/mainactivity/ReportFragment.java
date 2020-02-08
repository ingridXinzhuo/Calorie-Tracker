package com.example.mainactivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReportFragment extends Fragment {
    View vMain;
    final Calendar currentTime = Calendar.getInstance();
    EditText datePicker;
    PieChart pieChart;
    Button displayPieButton;
    EditText startDate;
    EditText endDate;
    Button displayBarButton;
    BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_report, container, false);
        datePicker = (EditText) vMain.findViewById(R.id.datePicker);
        pieChart = (PieChart) vMain.findViewById(R.id.pieChart);
        displayPieButton = (Button) vMain.findViewById(R.id.displayPieButton);
        startDate = (EditText) vMain.findViewById(R.id.startDate);
        endDate = (EditText) vMain.findViewById(R.id.endDate);
        displayBarButton = (Button) vMain.findViewById(R.id.displayBarButton);
        barChart = (BarChart) vMain.findViewById(R.id.barChart);



        pieChart.setUsePercentValues(true);
        displayPieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!datePicker.getText().toString().equals("")) {
                    ReportAsyncTask generateReport = new ReportAsyncTask();
                    generateReport.execute();

                }
            }});

        displayBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startDate.getText().toString().equals("") & !endDate.getText().toString().equals("")) {
                    BarAsyncTask barAsyncTask = new BarAsyncTask();
                    barAsyncTask.execute();

                }
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                currentTime.set(Calendar.YEAR, year);
                currentTime.set(Calendar.MONTH, monthOfYear);
                currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                datePicker.setText(sdf.format(currentTime.getTime()));
            }

        };


        datePicker.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH),
                    currentTime.get(Calendar.DAY_OF_MONTH)).show();
        });


        return vMain;
    }


    private class ReportAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            String DateSelected = datePicker.getText().toString();
            Appuser user =(Appuser) getActivity().getIntent().getSerializableExtra("user");
            String CalorieRemaining = RestClient.calculatRemainCalorie(user.getUserid(),DateSelected);

            return CalorieRemaining;
        }

        @Override
        protected void onPostExecute(String response) {
            if(!response.equals("[]")){
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String totalcalorieconsumed = null;
                try {
                    totalcalorieconsumed = jsonObject.getString("totalCalorieConsumed");
                    //Log.d("how","show~~~" + totalcalorieconsumed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String totalcalorieburned = null;
                try {
                    totalcalorieburned = jsonObject.getString("totalCalorieBurned");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String remainingcalorie = null;
                try {
                    remainingcalorie = jsonObject.getString("calorieRemain");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                List<PieEntry> yvalues = new ArrayList<>();
                yvalues.add(new PieEntry((Integer.parseInt(totalcalorieconsumed)), "totalcalorieconsumed"));
                yvalues.add(new PieEntry((Integer.parseInt((totalcalorieburned))), "totalcalorieburned"));
                yvalues.add(new PieEntry((Integer.parseInt((remainingcalorie))), "remainingcalorie"));

                PieDataSet dataSet = new PieDataSet(yvalues, "Report Results");

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());

                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(Color.rgb(120,0,170));
                colors.add(Color.rgb(230,0,0));
                colors.add(Color.rgb(190,220,0));
                dataSet.setColors(colors);

                pieChart.setData(data);
                pieChart.invalidate();
            }
            else
            {
                pieChart.setData(null);
                pieChart.invalidate();
            }
        }
    }

    private class BarAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            String startDateS = startDate.getText().toString();
            String EndDateS = endDate.getText().toString();
            Appuser user =(Appuser) getActivity().getIntent().getSerializableExtra("user");
            String reportPeriod = RestClient.findReportInDateRange(user.getUserid(),startDateS,EndDateS);
            return reportPeriod;
        }


        @Override
        protected void onPostExecute(String result) {

            String totalcalorieconsumed = "";
            String totalcalorieburned= "";
            String totalstepstaken= "";

            if(!result.equals("[]")){
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    totalcalorieconsumed = jsonObject.getString("totalCalorieConsumed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    totalcalorieburned = jsonObject.getString("totalCalorieBurned");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    totalstepstaken = jsonObject.getString("totalStepsTaken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<BarEntry> yvalues = new ArrayList<>();
                yvalues.add(new BarEntry(0f, (Float.parseFloat(totalcalorieconsumed))));
                yvalues.add(new BarEntry(1f, Float.parseFloat(totalcalorieburned)));
                yvalues.add(new BarEntry(2f, Float.parseFloat(totalstepstaken)));

                BarDataSet dataSet = new BarDataSet(yvalues, "Report Results");
                //LegendEntry entry = new LegendEntry();
                ArrayList<String> labels = new ArrayList<>();
                labels.add("totalcalorieconsumed");
                labels.add("totalcalorieburned");
                labels.add("totalstepstaken");
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

                BarData data = new BarData(dataSet);
                data.setValueFormatter(new PercentFormatter());

                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.rgb(141,23,146));
                colors.add(Color.rgb(255,134,32));
                colors.add(Color.rgb(83,255,13));
                dataSet.setColors(colors);
                barChart.setData(data);
                barChart.setFitBars(true);
                barChart.invalidate();

            }
            else{

                barChart.setData(null);
                barChart.invalidate();
            }

        }
    }


}
