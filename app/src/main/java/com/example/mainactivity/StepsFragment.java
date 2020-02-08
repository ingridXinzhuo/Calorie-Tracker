package com.example.mainactivity;

import android.arch.persistence.room.Room;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class StepsFragment extends Fragment {

   /*
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
     */
   View vRes;
    TextView textViewSteps;
    EditText editTextSteps;
    Button addToDBButton;
    TextView textViewAddToDB;
    Button addNetbeanButton;
    StepsDB db = null;
    Button readFromDBButton;
    TextView textViewReadFromDB;
    Button deleteFromDBButton;
    TextView textViewDeleteFromDB;
    Button upDateDBButton;
    TextView textViewUpdateDB;
    Calendar calendar = Calendar.getInstance();
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vRes = inflater.inflate(R.layout.fragment_steps, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepsDB.class, "StepsDB")
                .fallbackToDestructiveMigration()
                .build();
        textViewSteps = (TextView) vRes.findViewById(R.id.textViewSteps);
        editTextSteps = (EditText) vRes.findViewById(R.id.editTextSteps);
        addToDBButton = (Button) vRes.findViewById(R.id.addToDBButton);
        textViewAddToDB = (TextView) vRes.findViewById(R.id.textViewAddToDB);
        addNetbeanButton = (Button) vRes.findViewById(R.id.addNetbeanButton);
        readFromDBButton = (Button) vRes.findViewById(R.id.readFromDBButton);
        textViewReadFromDB = (TextView) vRes.findViewById(R.id.textViewReadFromDB) ;
        deleteFromDBButton = (Button) vRes.findViewById(R.id.deleteFromDBButton);
        textViewDeleteFromDB = (TextView) vRes.findViewById(R.id.textViewDeleteFromDB) ;
        upDateDBButton = (Button) vRes.findViewById(R.id.upDateDBButton);
        textViewUpdateDB = (TextView) vRes.findViewById(R.id.textViewUpdateDB) ;


        addToDBButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                InsertDatabase addDatabase = new InsertDatabase();
                addDatabase.execute();
            }
        });

        readFromDBButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        });


        deleteFromDBButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                DeleteDatabase deleteDatabase = new DeleteDatabase();
                deleteDatabase.execute();
            }
        });



        upDateDBButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute();
            }
        });

        addNetbeanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteToNetBean writeToNetBean = new WriteToNetBean();
                writeToNetBean.execute();

            }
        });

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        ClockForTimer taskTimer = new ClockForTimer();

        Date date = calendar.getTime();
        Timer timer = new Timer();
        timer.schedule(taskTimer,date,PERIOD_DAY);

        return vRes;
    }

    private class WriteToNetBean extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            Appuser user = (Appuser) getActivity().getIntent().getSerializableExtra("user");
            int userID = user.getUserid();
            List<Steps> steps = db.stepsDao().findByuserId(userID);
            ArrayList<Integer> allSteps = new ArrayList<>();
            for(Steps stepS : steps){
                int stepsU = Integer.parseInt(stepS.getSteps());
                allSteps.add(stepsU);
            }
            int sum = 0;
            for(int i = 0; i < allSteps.size();i++){
                sum = sum + allSteps.get(i);
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentTime = sdf.format(date);

            String res = RestClient.findByUseridAndReportdate(user.getUserid(),currentTime);
            String reportObj = "";
            //JsonObject jsonObject = new JsonObject("res");
            try {
                JSONArray jsonArr = new JSONArray(res);
                JSONObject jsonObject = jsonArr.getJSONObject(0);
                reportObj =  jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Report report = gson.fromJson(reportObj, Report.class);
            report.setTotalStepsTaken(String.valueOf(sum + Integer.parseInt(report.getTotalStepsTaken())) );
            RestClient.updateReport(report);
            db.stepsDao().deleteAll();
            String resultSet = "Update successful";
            return resultSet;
        }


        @Override
        protected void onPostExecute(String details) {
            //textViewAddDB.setText("Added Record: " + details);
            Toast.makeText(getActivity().getApplicationContext(),details, Toast.LENGTH_SHORT).show();

        }
    }



    private class InsertDatabase extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            Appuser user = (Appuser) getActivity().getIntent().getSerializableExtra("user");
            int userID = user.getUserid();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String curTimeC = sdf.format(date);
            if (!(editTextSteps.getText().toString().isEmpty())){
                Steps steps = new Steps(userID,curTimeC,editTextSteps.getText().toString());
                long id = db.stepsDao().insert(steps);
                return (id + "UserId: " + userID + " , "+ " Current time: " + curTimeC + " Steps: " + editTextSteps.getText().toString());
            }
            else
                return "";
        }

        @Override
        protected void onPostExecute(String details) {
            textViewAddToDB.setText("Added Record: " + details);
        }
    }

    private class ReadDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            List<Steps> steps = db.stepsDao().getAll();
            if (!(steps.isEmpty() || steps == null) ){
                String allSteps = "";
                for (Steps temp : steps) {
                    String stepsR = (temp.getId() + " " + temp.getUserId() + " " + temp.getDate() + " "+ temp.getSteps()+ " . || " );
                    allSteps = allSteps + stepsR;
                }
                return allSteps;
            }
            else
                return "";
        }

        @Override
        protected void onPostExecute(String details) {
            textViewReadFromDB.setText("All data: " + details);
        }
    }

    private class DeleteDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            db.stepsDao().deleteAll();
            return null;
        }
        protected void onPostExecute(Void param) {
            textViewDeleteFromDB.setText("All data was deleted");
        }
    }

    private class UpdateDatabase extends AsyncTask<Void, Void, String> {
        @Override protected String doInBackground(Void... params) {
            Steps steps=null;
            String[] details= editTextSteps.getText().toString().split(" ");
            if (details.length==4) {
                int id = Integer.parseInt(details[0]);
                steps = db.stepsDao().findByID(id);
                steps.setUserId(Integer.parseInt(details[1]));
                steps.setDate(details[2]);
                steps.setSteps(details[3]);
            }
            if (steps!=null) {
                db.stepsDao().updateUsers(steps);
                return (details[0] + " " + details[1] + " " + details[2] + " "+ details[3]);
            }
            return "";
        }
        @Override
        protected void onPostExecute(String details) {
            textViewUpdateDB.setText("Updated details: "+ details);
        }
    }
}


