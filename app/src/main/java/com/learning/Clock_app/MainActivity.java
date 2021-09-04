package com.learning.Clock_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Clock_app.R;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main";
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");
        createNotificationChannel(); // this needs to run only once

        BottomNavigationView bottomNav = findViewById(R.id.upper_navigation);
        viewPager2 = findViewById(R.id.main_vp2);
        FragmentManager fm = getSupportFragmentManager();

        FragmentAdapter fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        bottomNav.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.alarms)
                viewPager2.setCurrentItem(0);

            else if (itemId == R.id.stopper)
                viewPager2.setCurrentItem(1);

            else if (itemId == R.id.timer)
                viewPager2.setCurrentItem(2);

            return true;
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("hour")){
            int hour = intent.getIntExtra("hour", 12);
            int minute = intent.getIntExtra("minute", 0);
            String label = intent.getStringExtra("label");
            String days = intent.getStringExtra("days");

            AlarmModel alarmModel;
            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            try {
                alarmModel = new AlarmModel(-1, hour, minute, label, days, true);
                int id = databaseHelper.addOne(alarmModel); //I get the id and set it here
                alarmModel.setId(id);

                AlarmScheduler alarmScheduler = new AlarmScheduler(id, hour, minute, days, this);
                alarmScheduler.scheduleAlarm();

            } catch (Exception e) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                alarmModel = null;
            }

            if (alarmModel != null) {
                FragmentAdapter adapter = (FragmentAdapter) viewPager2.getAdapter();
                if (adapter != null) {
                    ((FragmentAlarms) adapter.getFragment(0)).addAlarmToList(alarmModel);
                }
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel("Alarm", name, importance);
        channel.setVibrationPattern(new long[]{0, 100, 1000, 300, 200, 100, 500, 200, 100});
        channel.setDescription(description);

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
