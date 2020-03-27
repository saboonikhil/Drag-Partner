package com.drag.partner.util;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.drag.partner.MainActivity;
import com.drag.partner.R;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String pickup, drop, startTime;
        pickup = remoteMessage.getData().get("pickup");
        drop = remoteMessage.getData().get("drop");
        startTime = remoteMessage.getData().get("startTime");

        if (isTokenValid()) {
            sendNotification(pickup, drop, startTime);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void sendNotification(String pickup, String drop, String startTime) {
        try {
            Calendar calendar = Calendar.getInstance();
            if (startTime != null) {
                Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
                calendar.setTime(displayTime);
                calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE, 30);
                startTime = new SimpleDateFormat("EEE, MMM d, h:mm a").format(calendar.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String message = "You have a new request from " + pickup + " to " + drop + " for " + startTime + ". Tap to bid fare!";

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setContentTitle("New Cab Request")
                .setTicker("New Cab Request")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_notification))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = new Random().nextInt(9999 - 1000) + 1000;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "New Cab Request", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(message);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(id, notificationBuilder.build());
    }

    private boolean isTokenValid() {
        SharedPreferences pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        String exp = pref.getString("expires", "");
        long time = System.currentTimeMillis();
        long expires;
        try {
            expires = Long.parseLong(exp);
        } catch (NumberFormatException nfe) {
            expires = 0;
        }
        return time < expires;
    }
}