package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        RecyclerView alarms_rv  = view.findViewById(R.id.alarms_rv_recycler);
        alarmModelList = new DatabaseHelper(this.getContext()).getEveryone();

        adapter = new AlarmsListAdapter(this.getContext(), alarmModelList, alarmModel ->
                Toast.makeText(getContext(), alarmModel.getHour() + " " + alarmModel.getMinute(), Toast.LENGTH_SHORT).show());

        alarms_rv.setAdapter(adapter);
        alarms_rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        onIntent(requireActivity().getIntent());

        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void onIntent(Intent intent){
        if (intent != null){
            alarmModelList.add(new AlarmModel(-1, intent.getIntExtra("hour", 12), intent.getIntExtra("minute", 0),
                    intent.getStringExtra("label"),intent.getStringExtra("days"), true));
            adapter.notifyDataSetChanged();
        }
    }
}
