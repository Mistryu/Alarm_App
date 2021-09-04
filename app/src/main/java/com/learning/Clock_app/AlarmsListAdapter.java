package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import Clock_app.R;

public class AlarmsListAdapter extends RecyclerView.Adapter<AlarmsListAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AlarmModel alarmModel);
    }

    private final Context context;
    private final List<AlarmModel> alarmModels;
    private final OnItemClickListener listener;

    public AlarmsListAdapter(Context ct, List<AlarmModel> aM, OnItemClickListener listener) {
        this.context = ct;
        this.alarmModels = aM;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alarm_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Here I assign values to the items that I get from database
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        AlarmModel alarmModel = databaseHelper.getOne(position + 1);
        int minute = alarmModel.getMinute();
        holder.time_tv.setText(alarmModel.getHour() + ":" + (minute < 10 ? "0" + minute : minute));
        holder.days_tv.setText(alarmModel.getDays());
        holder.switch_material.setActivated(true);
        holder.bind(alarmModels.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return alarmModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView time_tv;
        private final TextView days_tv;
        private final SwitchMaterial switch_material;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time_tv = itemView.findViewById(R.id.alarm_lay_time_tv);
            days_tv = itemView.findViewById(R.id.alarm_lay_days_tv);
            switch_material = itemView.findViewById(R.id.alarm_aly_switch);
        }

        public void bind(final AlarmModel alarmModel, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(alarmModel));

        }
    }


}

