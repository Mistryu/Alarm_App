package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Clock_app.R;

public class FragmentTimer extends Fragment {

    public static final String TAG = "TimerFragment";

    private TimeDownCounter counter;
    private NumberPicker numPick1, numPick2, numPick3;
    private FloatingActionButton start_btn, pause_btn, stop_btn;
    private TextView timeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        numPick1 = view.findViewById(R.id.num_picker1);
        numPick2 = view.findViewById(R.id.num_picker2);
        numPick3 = view.findViewById(R.id.num_picker3);
        start_btn = view.findViewById(R.id.start_fab_timer);
        pause_btn = view.findViewById(R.id.pause_fab_timer);
        stop_btn = view.findViewById(R.id.stop_fab_timer);
        timeText = view.findViewById(R.id.text_tv_timer);

        Log.d(TAG, "onCreateView: Started");

        if (numPick1 != null && numPick2 != null && numPick3 != null) {
            numPick1.setMinValue(0);
            numPick1.setMaxValue(24);
            numPick1.setWrapSelectorWheel(true);

            numPick2.setMinValue(0);
            numPick2.setMaxValue(60);
            numPick2.setWrapSelectorWheel(true);

            numPick3.setMinValue(0);
            numPick3.setMaxValue(60);
            numPick3.setWrapSelectorWheel(true);

            start_btn.setOnClickListener(v -> {
                int numPick1Val = numPick1.getValue();
                int numPick2Val = numPick2.getValue();
                int numPick3Val = numPick3.getValue();

                if (numPick1Val != 0 || numPick2Val != 0 || numPick3Val != 0) {
                    if (!stop_btn.isShown()) {

                        numPick1.setVisibility(View.GONE);
                        numPick2.setVisibility(View.GONE);
                        numPick3.setVisibility(View.GONE);
                        timeText.setVisibility(View.VISIBLE);
                                                        // hours   min          sec
                        counter = new TimeDownCounter(numPick1Val, numPick2Val, numPick3Val);
                    }
                    counter.countdownStart();
                    pause_btn.setVisibility(View.VISIBLE);
                    stop_btn.setVisibility(View.VISIBLE);
                    start_btn.setVisibility(View.GONE);
                }
            });

            pause_btn.setOnClickListener(v -> {
                pause_btn.setVisibility(View.GONE);
                start_btn.setVisibility(View.VISIBLE);
                counter.countdownPause();
            });

            stop_btn.setOnClickListener(v -> {
                pause_btn.setVisibility(View.GONE);
                stop_btn.setVisibility(View.GONE);
                start_btn.setVisibility(View.VISIBLE);
                numPick1.setVisibility(View.VISIBLE);
                numPick2.setVisibility(View.VISIBLE);
                numPick3.setVisibility(View.VISIBLE);
                timeText.setVisibility(View.GONE);
                counter.countdownStop();
            });
        }


        return view;
    }



    public class TimeDownCounter {

        private int hours,minutes,seconds;

        private long millisecondTime, startTime, totalTime = 0L;
        private Handler handler;
        private boolean stopped = false;

        public TimeDownCounter(int hours, int minutes, int seconds){
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public void countdownStart(){
            handler = new Handler();

            int HOURS_TO_MLS = 3600000;
            int MINUTES_TO_MLS = 60000;
            int SECONDS_TO_MLS = 1000;

            if (totalTime <= 0)
                totalTime = hours * HOURS_TO_MLS + minutes * MINUTES_TO_MLS + seconds * SECONDS_TO_MLS;

            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnableCountdown, 0);
        }

        public void countdownStop(){
            millisecondTime = 0L;
            startTime = 0L;
            totalTime = 0L;
            stopped = true;

            pause_btn.setVisibility(View.GONE);
            stop_btn.setVisibility(View.GONE);
            start_btn.setVisibility(View.VISIBLE);
            numPick1.setVisibility(View.VISIBLE);
            numPick2.setVisibility(View.VISIBLE);
            numPick3.setVisibility(View.VISIBLE);
            timeText.setVisibility(View.GONE);

            handler.removeCallbacks(runnableCountdown);

        }

        public void countdownPause(){
            handler.removeCallbacks(runnableCountdown);
        }

        private final Runnable runnableCountdown = new Runnable() {
            String text;

            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                millisecondTime = SystemClock.uptimeMillis() - startTime;
                totalTime = totalTime - millisecondTime;
                startTime = SystemClock.uptimeMillis();

                if (totalTime <= 0)
                    countdownStop();

                seconds = (int) (totalTime / 1000);
                minutes = seconds / 60;
                hours = minutes / 60;
                seconds = seconds % 60;
                minutes = minutes % 60;

                text = hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
                timeText.setText(text);
                if (!stopped)
                    handler.postDelayed(this, 0);
            }
        };

    }
}
