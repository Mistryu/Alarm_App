package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Clock_app.R;

public class FragmentAlarms extends Fragment {

    private static final String TAG = "AlarmsFragment";
    private AlarmsListAdapter adapter;
    private List<AlarmModel> alarmModelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);
        FloatingActionButton add_btn = view.findViewById(R.id.alarms_btn_add);
        Log.d(TAG, "onCreateView: Started");

        add_btn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ActivityNewAlarm.class)));

        RecyclerView alarms_rv = view.findViewById(R.id.alarms_rv_recycler);
        DatabaseHelper db = new DatabaseHelper(this.getContext());
        alarmModelList = db.getEveryone();

        adapter = new AlarmsListAdapter(this.getContext(), alarmModelList, alarmModel -> {
            Intent intent = new Intent(getActivity(), ActivityNewAlarm.class);
            intent.putExtra("ID", alarmModel.getId());
            intent.putExtra("hour", alarmModel.getHour());
            intent.putExtra("minute", alarmModel.getMinute());
            intent.putExtra("days", alarmModel.getDays());
            intent.putExtra("label", alarmModel.getLabel());
            startActivity(intent);
        });

        alarms_rv.setAdapter(adapter);
        alarms_rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAlarmToList(AlarmModel alarmModel) {
        alarmModelList.add(alarmModel);
        adapter.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void deleteFromAlarmList(int id){
        alarmModelList.removeIf(alarmModel -> alarmModel.getId() == id);
        adapter.notifyDataSetChanged();
    }
}
