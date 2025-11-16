package hu.bk.plugins.capacitorExactAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmDismissReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmDismissReceiver", "Notification dismissed → stopping alarm");

        // Stop AlarmService
        Intent stopIntent = new Intent(context, hu.bk.plugins.capacitorExactAlarm.AlarmService.class);
        stopIntent.setAction("STOP_ALARM");
        context.startService(stopIntent);

        // Open app
        Intent openIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        if (openIntent != null) {
            openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(openIntent);
        }
    }
}