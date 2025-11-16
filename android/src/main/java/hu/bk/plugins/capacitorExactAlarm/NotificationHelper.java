package hu.bk.plugins.capacitorExactAlarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "alarm_channel";
    private static final String CHANNEL_NAME = "Alarm Notifications";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) return;

            NotificationChannel existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (existingChannel != null) return;

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 250, 500});

            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String title, String message, String icon) {
        int iconId = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());

        if (iconId == 0) {
            // fallback to app icon
            iconId = android.R.drawable.ic_lock_idle_alarm;
        }

        createNotificationChannel(context);

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 250, 500})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    public static Notification buildServiceNotification(Context context,Integer alarmId,String title,String message,String soundName,String data,String icon,String dismissText) {
        // Intent to stop the alarm service

        createNotificationChannel(context);

        Intent stopIntent = new Intent(context, AlarmService.class);
        stopIntent.setAction("STOP_ALARM");

        Log.d("buildServiceNotification", "alarmId: "+alarmId+" "+title+" "+message);

        PendingIntent stopPendingIntent = PendingIntent.getService(
                context,
                1,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Intent to open the app (Notification click action)
        Intent openIntent = new Intent(context, NotificationOpenActivity.class);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add an extra to the intent to signal the app was opened from this alarm
        openIntent.putExtra("soundName", soundName);
        openIntent.putExtra("alarmId", alarmId);
        openIntent.putExtra("title", title);
        openIntent.putExtra("msg", message);
        openIntent.putExtra("data", data);

        int iconId = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());

        if (iconId == 0) {
            // fallback to app icon
            iconId = android.R.drawable.ic_lock_idle_alarm;
        }

        PendingIntent openAppPendingIntent = PendingIntent.getActivity(
                context,
                alarmId,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // DISMISS (swipe) action
        Intent dismissIntent = new Intent(context, AlarmDismissReceiver.class);
        dismissIntent.setAction("ALARM_NOTIFICATION_DISMISSED");

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(
                context,
                3,
                dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );



        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(iconId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(openAppPendingIntent)
                .setDeleteIntent(dismissPendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, dismissText, stopPendingIntent)
                .build();
    }
}
