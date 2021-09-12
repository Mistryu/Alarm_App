package com.learning.Clock_app.Adapters;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.learning.Clock_app.Helpers.AlarmModel;
import com.learning.Clock_app.Helpers.AlarmScheduler;
import com.learning.Clock_app.Helpers.DatabaseHelper;
import com.learning.Clock_app.Helpers.NotificationReceiver;
import com.learning.Clock_app.R;

import java.util.List;

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

    @SuppressLint({"SetTextI18n", "UnspecifiedImmutableFlag"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Here I assign values to the items that I get from database
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        AlarmModel alarmModel = databaseHelper.getOne(alarmModels.get(position).getId());
        int minute = alarmModel.getMinute();
        holder.time_tv.setText(alarmModel.getHour() + ":" + (minute < 10 ? "0" + minute : minute));
        holder.days_tv.setText(alarmModel.getDays());
        holder.label_tv.setText(alarmModel.getLabel());
        SwitchMaterial switchMaterial = holder.switch_material;
        switchMaterial.setActivated(true);

        switchMaterial.setOnClickListener(v -> {
            boolean is_active = switchMaterial.isChecked();
            int id = alarmModel.getId();
            databaseHelper.changeStatus(id, is_active ? 1 : 0);

            if (is_active){
                AlarmScheduler alarmScheduler = new AlarmScheduler(id, alarmModel.getHour(), alarmModel.getMinute(), alarmModel.getDays(), context);
                alarmScheduler.scheduleAlarm();
            }else {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(PendingIntent.getBroadcast(context, id, new Intent(context, NotificationReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
            }
        });
        holder.bind(alarmModels.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return alarmModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView time_tv;
        private final TextView days_tv;
        private final TextView label_tv;
        private final SwitchMaterial switch_material;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time_tv = itemView.findViewById(R.id.alarm_lay_time_tv);
            days_tv = itemView.findViewById(R.id.alarm_lay_days_tv);
            label_tv = itemView.findViewById(R.id.alarm_aly_label);
            switch_material = itemView.findViewById(R.id.alarm_aly_switch);
        }

        public void bind(final AlarmModel alarmModel, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(alarmModel));
        }
    }


}

