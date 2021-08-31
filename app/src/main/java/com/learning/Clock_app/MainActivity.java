package com.learning.Clock_app;

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
        int hour = intent.getIntExtra("hour", 4242);
        int minute = intent.getIntExtra("minute", 4242);
        String label = intent.getStringExtra("label");
        String days = intent.getStringExtra("days");

        AlarmModel alarmModel;
        try {
            alarmModel = new AlarmModel(-1, hour, minute, label, days, true);
            Toast.makeText(this, alarmModel.toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            alarmModel = new AlarmModel(-1, -1, -1, "ERROR", "ERROR", false);
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        boolean success = databaseHelper.addOne(alarmModel);
        Toast.makeText(this, "Success =" + success, Toast.LENGTH_SHORT).show();

        FragmentAdapter adapter = (FragmentAdapter) viewPager2.getAdapter();
        if (adapter != null) {
            ((FragmentAlarms) adapter.getFragment(0)).addAlarmToList(alarmModel);
        }
    }
}
