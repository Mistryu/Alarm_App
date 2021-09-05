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

    //TODO rewrite database so it doesn't rely on database id

    private static final String ALARMS_TABLE = "alarms_table";
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
                "(" + ID + " INTEGER, " + HOUR + " INTEGER, " + MINUTE + " INTEGER, " + LABEL + " TEXT, " + DAYS + " TEXT, " + IS_ACTIVE + " BOOL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addOne(AlarmModel alarmModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int id = nextID();

        cv.put(HOUR, alarmModel.getHour());
        cv.put(MINUTE, alarmModel.getMinute());
        cv.put(LABEL, alarmModel.getLabel());
        cv.put(DAYS, alarmModel.getDays());
        cv.put(ID, id);
        cv.put(IS_ACTIVE, alarmModel.isActive());
        db.execSQL("DELETE FROM " + ALARMS_TABLE + " WHERE " + HOUR + " = NULL");
        long insert = db.insert(ALARMS_TABLE, null, cv);
        db.close();
        return id;
    }

    public boolean deleteOne(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + ALARMS_TABLE + " SET " + HOUR + " = null, " + MINUTE + " = null WHERE " + ID + " = " + id;
        db.execSQL(queryString);
        boolean result = db.rawQuery(queryString, null).moveToFirst();
        return result;
    }

    public List<AlarmModel> getEveryone() {
        List<AlarmModel> returnList = new ArrayList<>();

        String select_str = "SELECT * FROM " + ALARMS_TABLE + " WHERE " + HOUR + " IS NOT NULL";
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

    public AlarmModel getOne(int id) {
        String select_str = "SELECT * FROM " + ALARMS_TABLE + " WHERE ID = " + id;
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
            return new AlarmModel(id, hour, minutes, label, days, is_active);
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return null;
        }
    }
    private int nextID(){
        String select = "SELECT * FROM " + ALARMS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        int id = -1;
        try (Cursor cursor = db.rawQuery(select, null)) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        return id + 1;
    }
}
