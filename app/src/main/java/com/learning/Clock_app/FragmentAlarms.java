package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import Clock_app.R;

public class FragmentAlarms extends Fragment {

    private static final String TAG = "AlarmsFragment";
//    private MyInterfaceListener listener;
    private List<AlarmModel> alarmModelList;
    private AlarmsListAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        FloatingActionButton add_btn = view.findViewById(R.id.alarms_btn_add);

        Log.d(TAG, "onCreateView: Started");

        add_btn.setOnClickListener(v -> {
//            ViewPager2 viewPager2 = Objects.requireNonNull(getActivity()).findViewById(R.id.main_vp2);
//            viewPager2.setCurrentItem(3);
            addFragNewAlarm();
            });


        RecyclerView alarms_rv  = view.findViewById(R.id.alarms_rv_recycler);
        alarmModelList = getAlarms();

        adapter = new AlarmsListAdapter(this.getContext(), alarmModelList);
        alarms_rv.setAdapter(adapter);
        alarms_rv.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return view;
    }

    private List<AlarmModel> getAlarms() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
        return databaseHelper.getEveryone();
    }

    public void addFragNewAlarm() {
        FragmentManager childFragMan = getChildFragmentManager();

        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        FragmentNewAlarm fragmentNewAlarm = new FragmentNewAlarm();
        childFragTrans.add(R.id.main_vp2, fragmentNewAlarm);
        childFragTrans.addToBackStack("B");
        childFragTrans.commit();

    }
}
