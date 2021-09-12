package com.learning.Clock_app.Helpers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AlarmScheduler {

    private final int hour;
    private final int minute;
    private final String days;
    private final int ID;
    private final Context context;

    public AlarmScheduler(int id, int hour, int minute, String days, Context context) {
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.ID = id;
        this.context = context;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void scheduleAlarm() {
        final int WEEK_TO_MLS = 604_800_000;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        int dayOfWeek = getDay(days, hour, minute);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        long fire_time = calendar.getTimeInMillis();
        if (Calendar.getInstance().getTimeInMillis() >= fire_time) { //next
            fire_time += WEEK_TO_MLS;
        }

        Intent myIntent = new Intent(context, NotificationReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        myIntent.putExtra("ID", ID);
        myIntent.putExtra("hour", hour);
        myIntent.putExtra("minute", minute);
        myIntent.putExtra("days", days);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, fire_time, pendingIntent);

    }

    private int getDay(String days, int hour, int minute) {
        List<Integer> days_to_int = new ArrayList<>();
        final List<String> DAYS = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        if (days.equals("All Week"))
            days_to_int.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        else
            Arrays.stream(days.trim().split(" ")).forEach(x -> days_to_int.add(DAYS.indexOf(x) + 1));

        Calendar now_calendar = Calendar.getInstance();
        int today = now_calendar.get(Calendar.DAY_OF_WEEK);

        if (days_to_int.contains(today)) {
            if (now_calendar.get(Calendar.HOUR_OF_DAY) <= hour && now_calendar.get(Calendar.MINUTE) < minute)
                return today;
        }
        while (true) {
            today++;
            if (today >= 7)
                today = 0;
            else if (days_to_int.contains(today)) {
                return today;
            }
        }
    }
}
