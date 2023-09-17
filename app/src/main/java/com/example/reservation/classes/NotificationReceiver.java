package com.example.reservation.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.reservation.R;

public class NotificationReceiver extends BroadcastReceiver {

    int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, "Appointment Reminder", "Your appointment is coming up!");
    }

    private void showNotification(Context context, String title, String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.icono)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}

