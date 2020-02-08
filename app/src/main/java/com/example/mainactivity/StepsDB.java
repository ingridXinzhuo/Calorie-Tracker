package com.example.mainactivity;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Steps.class},version = 2, exportSchema = false)
public abstract class StepsDB extends RoomDatabase {


    public abstract StepsDao stepsDao();

    private static volatile StepsDB INSTANCE;
    static StepsDB getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (StepsDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),StepsDB.class, "step_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
