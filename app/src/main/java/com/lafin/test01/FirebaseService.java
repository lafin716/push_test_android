package com.lafin.test01;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    private static final String[] topics = {"/topics/custom", "/topics/notify"};

    public FirebaseService() {
        super();
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    Log.i("FCM Token", task.getResult());
                    Toast.makeText(getApplicationContext(), task.getResult(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        makeNotification(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    // 팝업 띄우기
    public void makeNotification(RemoteMessage remoteMessage) {
        try {
            int notificationId = -1;
            Context mContext = getApplicationContext();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            String topic = remoteMessage.getFrom();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setVibrate(new long[] {200, 100, 200});
            }
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setContentText(message);

            if (topic.equals(topics[0])) {
                notificationId = 0;
            } else if (topic.equals(topics[1])) {
                notificationId = 1;
            }

            if (notificationId >= 0) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(notificationId, builder.build());
            }

        } catch (NullPointerException nullException) {
            Toast.makeText(getApplicationContext(), "알림에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("error Notify", nullException.toString());
        }
    }
}
