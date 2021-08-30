package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Clock_app.R;

public class FragmentStopper extends Fragment {

    public static final String TAG = "StopperFragment";

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopper, container, false);
        FloatingActionButton fab_start = view.findViewById(R.id.start_fab_stopper);
        FloatingActionButton fab_pause = view.findViewById(R.id.pause_fab_stopper);
        FloatingActionButton fab_stop = view.findViewById(R.id.stop_fab_stopper);
        FloatingActionButton fab_pin = view.findViewById(R.id.pin_fab_stopper);
        TextView text_time = view.findViewById(R.id.stopper_tv_time);

        Log.d(TAG, "StopperFragment: Started");

        RecyclerView pinnedListLayout = view.findViewById(R.id.pinned_layout);
        List<String> pinnedList = new ArrayList<>();

        StopperPinnedAdapter adapter = new StopperPinnedAdapter(this.getContext(), pinnedList);
        pinnedListLayout.setAdapter(adapter);
        pinnedListLayout.setLayoutManager(new LinearLayoutManager(this.getContext()));

        TimeManager manager = new TimeManager(text_time);

        fab_start.setOnClickListener(v -> {
            manager.stopperStart();
            if (!fab_stop.isShown()) {
                pinnedList.clear();
                adapter.notifyDataSetChanged();
            }

            fab_start.setVisibility(View.GONE);
            fab_pin.setVisibility(View.VISIBLE);
            fab_pause.setVisibility(View.VISIBLE);
            fab_stop.setVisibility(View.GONE);
        });

        fab_pause.setOnClickListener(v -> {
            manager.stopperPause();
            fab_start.setVisibility(View.VISIBLE);
            fab_stop.setVisibility(View.VISIBLE);
            fab_pin.setVisibility(View.GONE);
            fab_pause.setVisibility(View.GONE);
        });

        fab_stop.setOnClickListener(v -> {
            manager.stopperStop();
            fab_start.setVisibility(View.VISIBLE);
            fab_pause.setVisibility(View.GONE);
            fab_pin.setVisibility(View.GONE);
            fab_stop.setVisibility(View.GONE);
        });
        fab_pin.setOnClickListener(v -> {
            pinnedList.add(manager.getTimeText());
            adapter.notifyDataSetChanged();
            pinnedListLayout.scrollToPosition(pinnedList.size() - 1);
        });


        return view;
    }

    private static class TimeManager {

        public static final String TAG = "TimeManager";

        private final TextView textView;
        private long millisecondTime, startTime, updateTime = 0L;

        private Handler handler;
        private int seconds, minutes, milliSeconds;
        private int pined_count = 0;

        private String time;

        public TimeManager(TextView textView) {
            this.textView = textView;
        }

        //Here are stopper methods
        public void stopperStart() {
            handler = new Handler();
            startTime = SystemClock.uptimeMillis();
            Log.d(TAG, "TimeManager: Started");
            handler.postDelayed(runnable, 0);
        }

        public void stopperPause(){
            handler.removeCallbacks(runnable);
        }

        @SuppressLint("SetTextI18n")
        public void stopperStop(){
            millisecondTime = 0L ;
            startTime = 0L ;
            updateTime = 0L ;
            seconds = 0 ;
            minutes = 0 ;
            milliSeconds = 0 ;
            pined_count = 0;

            Log.d(TAG, "TimeManager: Stopped");

            handler.removeCallbacks(runnable);
            textView.setText("00:00:00");
        }

        public String getTimeText(){
            pined_count++;
            return pined_count + "." + "  " + time;
        }

        private final Runnable runnable = new Runnable() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void run() {
                millisecondTime = SystemClock.uptimeMillis() - startTime;
                startTime = SystemClock.uptimeMillis();
                updateTime += millisecondTime;

                seconds = (int) (updateTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                milliSeconds = (int) (updateTime % 1000) / (10);

                time = "" + minutes + ":" + String.format("%02d", seconds) + ":" + String.format("%02d", milliSeconds);
                textView.setText(time);

                handler.postDelayed(this, 0);
            }
        };

    }

}
