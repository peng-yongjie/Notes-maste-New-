package com.soulocean.Diary.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.soulocean.Diary.R;

/*使用方法
 NotificationChannel notificationCannel =new NotificationChannel(this);
         notificationCannel.CreateNotificationChannel();
         notificationCannel.sendChatMsg("111");
         notificationCannel.sendSubscribeMsg("222");
         */

/**
 * @author soulo
 */
public class NotificationChannel extends ContextWrapper {

    int i = 1;

    public NotificationChannel(Context base) {
        super(base);
    }

    public void CreateNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        android.app.NotificationChannel channel = new android.app.NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);//未读角标
        channel.enableVibration(true);//震动
        long[] pattern = {0, 200, 0, 300};
        channel.setVibrationPattern(pattern);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotificationMsg(String MSG) {

       /* Intent intent2 = new Intent(this, SettingsActivity.class);
        intent2.setAction(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);*/

        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.android.calendar");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, LaunchIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = manager.getNotificationChannel("chat");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            }
        }

        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("情侣课表")
                .setContentText(MSG)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.black))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    public void sendSubscribeMsg(String SBM) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("情侣课表2")
                .setContentText(SBM)
                .setNumber(i)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setAutoCancel(true)
                .setLights(Color.WHITE, 1000, 1000)//设置前置LED灯进行闪烁， 第一个为颜色值  第二个为亮的时长  第三个为暗的时长
                .build();
        manager.notify(2, notification);
        i++;

    }

}
