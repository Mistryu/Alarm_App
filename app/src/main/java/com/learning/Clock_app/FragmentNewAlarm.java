package com.learning.Clock_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import Clock_app.R;

public class FragmentNewAlarm extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_alarm, container, false);

        TimePicker timePicker = view.findViewById(R.id.alarm_timepic);
        timePicker.setIs24HourView(true);

        TextView days = view.findViewById(R.id.alarm_tv_days);

        FloatingActionButton back_btn = view.findViewById(R.id.alarm_btn_back);
        back_btn.setOnClickListener(v -> {
            ViewPager2 viewPager2 = Objects.requireNonNull(getActivity()).findViewById(R.id.main_vp2);
            viewPager2.setCurrentItem(0);
        });

        EditText label = view.findViewById(R.id.alarm_et_label);
        FloatingActionButton add_btn = view.findViewById(R.id.alarm_btn_add);
        add_btn.setOnClickListener(vi -> {
            AlarmModel alarmModel;

            try {
                alarmModel = new AlarmModel(-1, timePicker.getHour(), timePicker.getMinute(),
                        label.getText().toString(), days.getText().toString(), true);
                Toast.makeText(getActivity(), alarmModel.toString(), Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                alarmModel = new AlarmModel(-1, -1, -1, "ERROR", "ERROR", false);
            }

            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

            boolean success = databaseHelper.addOne(alarmModel);
            Toast.makeText(getActivity(), "Success =" + success, Toast.LENGTH_SHORT).show();

            ViewPager2 viewPager2 = Objects.requireNonNull(getActivity()).findViewById(R.id.main_vp2);
            viewPager2.setCurrentItem(0);
        });

        LinearLayout linearLayout = view.findViewById(R.id.alarm_ll_repeat);

        linearLayout.setOnClickListener(w -> {
            Dialog dialog = new Dialog(getContext());
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

        return view;
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
