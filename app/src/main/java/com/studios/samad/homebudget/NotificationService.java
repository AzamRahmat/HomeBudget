package com.studios.samad.homebudget;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Azam on 3/31/2016.
 */
public class NotificationService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("ME", "Notification started");
        String reminderString = intent.getExtras().getString("reminder");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {500,1000};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.bills)
                        .setContentTitle("Due Bill")
                        .setContentText(reminderString)
                        .setSound(alarmSound)
                        .setVibrate(v);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());

        Toast.makeText(context, "Alarm On", Toast.LENGTH_SHORT).show();
    }



}