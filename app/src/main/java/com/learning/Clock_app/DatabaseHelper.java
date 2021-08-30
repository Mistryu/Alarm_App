package com.learning.Clock_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String ALARMS_TABLE = "ALARMS_TABLE";
    private static final String HOUR = "HOUR";
    private static final String MINUTE = "MINUTE";
    private static final String LABEL = "LABEL";
    private static final String DAYS = "DAYS";
    private static final String IS_ACTIVE = "IS_ACTIVE";
    private static final String ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Alarms", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ALARMS_TABLE +
                "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HOUR + " INTEGER, "  + MINUTE + " INTEGER, " + LABEL + " TEXT, " + DAYS + " TEXT, " + IS_ACTIVE + " BOOL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(AlarmModel alarmModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(HOUR, alarmModel.getHour());
        cv.put(MINUTE, alarmModel.getMinute());
        cv.put(LABEL, alarmModel.getLabel());
        cv.put(DAYS, alarmModel.getDays());
        cv.put(IS_ACTIVE, alarmModel.isActive());

        long insert = db.insert(ALARMS_TABLE, null, cv);
        return insert != -1;
    }

    public boolean deleteOne(AlarmModel alarmModel){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ALARMS_TABLE + " WHERE " + ID + " = " + alarmModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }

    public List<AlarmModel> getEveryone(){
        List<AlarmModel> returnList = new ArrayList<>();

        String select_str = "SELECT * FROM " + ALARMS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(select_str, null)) {

            if (cursor.moveToFirst()) {
                //loop through the result and create new customer objects. Put them in the list
                do {
                    int id = cursor.getInt(0);
                    int hour = cursor.getInt(1);
                    int minutes = cursor.getInt(2);
                    String label = cursor.getString(3);
                    String days = cursor.getString(4);
                    boolean is_active = cursor.getInt(5) == 1; //1 - true

                    AlarmModel alarmModel = new AlarmModel(id, hour, minutes, label, days, is_active);
                    returnList.add(alarmModel);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return returnList;
    }

    public AlarmModel getOne(int position){
        String select_str = "SELECT * FROM " + ALARMS_TABLE + " WHERE ID = " + position;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(select_str, null)) {
            int hour = 0;
            int minutes = 0;
            String label = null;
            String days = null;
            boolean is_active = false;

            if (cursor.moveToFirst()) {
                hour = cursor.getInt(1);
                minutes = cursor.getInt(2);
                label = cursor.getString(3);
                days = cursor.getString(4);
                is_active = cursor.getInt(5) == 1;
            }

            db.close();
            return new AlarmModel(position, hour, minutes, label, days, is_active);
        }
        catch (Exception e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }
}
