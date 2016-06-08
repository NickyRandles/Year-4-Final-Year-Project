package com.project.nicky.medicalmanager.Home;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import com.project.nicky.medicalmanager.Patient.AddMedicationsActivity;
import com.project.nicky.medicalmanager.R;

public class AppointmentService extends Service {
    public AppointmentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), AddMedicationsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Medication Reminder")
                .setContentText("Just a friendly reminder to take your medication")
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(sound)
                .build();

        manager.notify(1, notification);
    }

}