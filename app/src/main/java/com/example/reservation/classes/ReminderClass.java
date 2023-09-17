package com.example.reservation.classes;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ReminderClass extends AppCompatActivity {
    private  int MY_PERMISSIONS_REQUEST_ALARM;
    public void scheduleAppointmentNotification(Context context, long appointmentTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeDiff = appointmentTimeMillis - currentTimeMillis;

        // Calculate notification time (e.g., 15 minutes before the appointment)
        long notificationTimeMillis = appointmentTimeMillis - (15 * 60 * 1000);



            // Schedule a notification using AlarmManager
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
           PendingIntent pendingIntent = PendingIntent.getBroadcast(
                   context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
              try {
                  alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent);
              }catch (SecurityException e){

              }
              }
        }

}
