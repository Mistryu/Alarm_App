package com.learning.Clock_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import Clock_app.R;

public class ActivityNewAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        TimePicker timePicker = findViewById(R.id.alarm_timepic);
        timePicker.setIs24HourView(true);

        TextView days = findViewById(R.id.alarm_tv_days);

        FloatingActionButton back_btn = findViewById(R.id.alarm_btn_back);
        back_btn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        EditText label = findViewById(R.id.alarm_et_label);
        FloatingActionButton add_btn = findViewById(R.id.alarm_btn_add);
        add_btn.setOnClickListener(vi -> {
            AlarmModel alarmModel;

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String label_txt = label.getText().toString();
            String days_txt = days.getText().toString();

            try {
                alarmModel = new AlarmModel(-1, hour, minute, label_txt, days_txt, true);
                Toast.makeText(this, alarmModel.toString(), Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                alarmModel = new AlarmModel(-1, -1, -1, "ERROR", "ERROR", false);
            }

            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            boolean success = databaseHelper.addOne(alarmModel);
            Toast.makeText(this, "Success =" + success, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("hour", hour);
            intent.putExtra("minute", minute);
            intent.putExtra("label", label_txt);
            intent.putExtra("days", days_txt);
            startActivity(intent);
        });

        LinearLayout linearLayout = findViewById(R.id.alarm_ll_repeat);

        linearLayout.setOnClickListener(w -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_pick_days);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            CheckBox cbMo = dialog.findViewById(R.id.checkbox_Mo);
            CheckBox cbTu = dialog.findViewById(R.id.checkbox_Tu);
            CheckBox cbWe = dialog.findViewById(R.id.checkbox_We);
            CheckBox cbTh = dialog.findViewById(R.id.checkbox_Th);
            CheckBox cbFr = dialog.findViewById(R.id.checkbox_Fr);
            CheckBox cbSa = dialog.findViewById(R.id.checkbox_Sa);
            CheckBox cbSu = dialog.findViewById(R.id.checkbox_Su);

            List<CheckBox> checkDays = Arrays.asList(cbMo, cbTu, cbWe, cbTh, cbFr, cbSa, cbSu);

            FloatingActionButton ok_btn = dialog.findViewById(R.id.dialog_btn_ok);
            ok_btn.setOnClickListener(wie -> {
                changeDays(days, checkDays);
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void changeDays(TextView days, List<CheckBox> checkDays){
        StringBuilder week_day = new StringBuilder();
        for (CheckBox cb: checkDays) {
            week_day.append(cb.isChecked() ? cb.getText().toString().substring(0,3) + " ": "");
        }
        days.setText(week_day.toString().equals( "Mon Tue Wed Thu Fri Sat Sun ") ? "All Week" : week_day.toString());
    }
}

//TODO: If all days are selected than say Days: all else normal
// Also change the switch button to look better.
// If it all works good than make the delete function and is_Active to change from click.
// After everything works and you can add and remove stuff freely focus on making the alarm work.
//

//TODO: FragmentNewAlarm trun ito separate fragment that is supposed to give reult
//put AlarmModel alarmModel or it's properties into it and use method on dataReceived or sth like it in FragmentAlarms
