package com.learning.Clock_app.Helpers;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.learning.Clock_app.MainActivity;

import com.learning.Clock_app.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent my_intent = new Intent(context, MainActivity.class);
        my_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final int ID = intent.getIntExtra("ID", 0);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                ID, my_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

//        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +context.getPackageName()+"/"+R.raw.alarm_sound);

        // Perform the operation associated with our pendingIntent
        Notification notification = new NotificationCompat.Builder(context, "Alarm")
                .setContentTitle("Click to cancel")
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
//                .setSound(alarmSound)
                .build();

        final MediaPlayer song = MediaPlayer.create(context, R.raw.alarm_sound);
        song.start();

        AlarmScheduler alarmScheduler = new AlarmScheduler(ID, intent.getIntExtra("hour", 12),
                intent.getIntExtra("minute", 0), intent.getStringExtra("days"), context);
        alarmScheduler.scheduleAlarm();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(ID, notification);

    }
}
