package com.learning.Clock_app;

import android.os.Bundle;
import android.util.Log;

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
}
