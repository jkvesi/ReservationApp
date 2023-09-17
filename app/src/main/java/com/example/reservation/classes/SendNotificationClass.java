package com.example.reservation.classes;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import androidx.annotation.NonNull;
import androidx.work.Worker;

import com.example.reservation.R;

import java.util.concurrent.TimeUnit;

public class SendNotificationClass extends Worker {
    private final Context context;


    public SendNotificationClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Send a notification here
        sendNotification();
        return Result.success();
    }

    private void sendNotification() {
        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.icono) // Set your notification icon
                .setContentTitle("Notification Title")
                .setContentText("Notification Content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create a unique notification ID
        int notificationId = 123;

        // Build the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
        OneTimeWorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(SendNotificationClass.class)
                .setInitialDelay(5000, TimeUnit.MILLISECONDS) // Delay in milliseconds (e.g., 5 seconds)
                .build();

// Enqueue the WorkRequest
        WorkManager.getInstance(context).enqueue(notificationWorkRequest);
    }
}
