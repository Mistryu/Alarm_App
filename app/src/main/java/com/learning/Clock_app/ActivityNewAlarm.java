package com.learning.Clock_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.learning.Clock_app.Helpers.DatabaseHelper;

import java.util.Arrays;
import java.util.List;

public class ActivityNewAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        TimePicker timePicker = findViewById(R.id.alarm_timepic);
        timePicker.setIs24HourView(true);

        TextView days = findViewById(R.id.alarm_tv_days);
        EditText label = findViewById(R.id.alarm_et_label);
        FloatingActionButton add_btn = findViewById(R.id.alarm_btn_add);

        findViewById(R.id.alarm_btn_back).setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        Intent intentFromMain = getIntent();

        boolean clicked_from_rv = intentFromMain != null && intentFromMain.hasExtra("hour");
        if (clicked_from_rv) {
            timePicker.setHour(intentFromMain.getIntExtra("hour", 12));
            timePicker.setMinute(intentFromMain.getIntExtra("minute", 0));
            days.setText(intentFromMain.getStringExtra("days"));
            label.setText(intentFromMain.getStringExtra("label"));

            add_btn.setImageResource(R.drawable.ic_check);

            FloatingActionButton delete_btn = findViewById(R.id.alarm_btn_delete);
            delete_btn.setVisibility(View.VISIBLE);

            delete_btn.setOnClickListener(view -> {
                Intent intent = new Intent(this, MainActivity.class);
                DatabaseHelper db = new DatabaseHelper(this);

                int delete_id = intentFromMain.getIntExtra("ID", -1);
                db.deleteOne(delete_id);

                intent.putExtra("delete", delete_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }


        add_btn.setOnClickListener(vi -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String label_txt = label.getText().toString();
            String days_txt = days.getText().toString();
            if (label_txt.length() > 42)
                Toast.makeText(this, "Label must be under 42 characters!", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(this, MainActivity.class);

                if (clicked_from_rv) {
                    DatabaseHelper db = new DatabaseHelper(this);
                    int delete_id = intentFromMain.getIntExtra("ID", -1);
                    db.deleteOne(delete_id);
                    intent.putExtra("delete", delete_id);
                }
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.putExtra("label", label_txt);
                intent.putExtra("days", days_txt);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
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

            dialog.findViewById(R.id.dialog_btn_ok).setOnClickListener(wie -> {
                if (changeDays(days, checkDays))
                    dialog.dismiss();
            });
            dialog.show();
        });
    }

    private boolean changeDays(TextView days, List<CheckBox> checkDays) {
        StringBuilder week_day = new StringBuilder();
        int count = 0;
        for (CheckBox cb : checkDays) {
            week_day.append(cb.isChecked() ? getResources().getStringArray(R.array.days_array)[count].substring(0, 3) + " " : "");
            count++;
        }
        String week_day_str = week_day.toString();
        if (!week_day_str.equals("")) {
            days.setText(week_day.toString().equals("Mon Tue Wed Thu Fri Sat Sun ") ? "All Week" : week_day_str);
            return true;
        } else {
            Toast.makeText(this, "You need to have at least one day selected!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}