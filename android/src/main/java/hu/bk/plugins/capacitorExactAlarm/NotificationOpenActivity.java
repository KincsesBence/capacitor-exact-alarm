package hu.bk.plugins.capacitorExactAlarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * A simple, transparent activity launched when a user taps the alarm notification.
 * Its job is to communicate the alarmId back to the CapacitorAlarmPlugin
 * via its static instance and then close immediately.
 */
public class NotificationOpenActivity extends Activity {

    private static final String TAG = "NotificationOpenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int alarmId = intent.getIntExtra("alarmId", -1);

        Log.d(TAG, "Notification tapped. Received alarmId: " + alarmId);

        // 1. Attempt to get the static plugin instance
        capacitorExactAlarmPlugin plugin = capacitorExactAlarmPlugin.getInstance();

        if (plugin != null && alarmId != -1) {
            // Case 1: App is running (foreground or background). Plugin instance exists.
            // Call the public handler method on the main plugin
            plugin.handleNotificationTap(intent);
            Log.d(TAG, "Event sent to plugin via static handler.");
        } else if (alarmId != -1) {
            // Case 2: App is closed (killed). Plugin instance is null.
            // We must force the main Capacitor Activity to launch/relaunch
            // with the notification intent data so the bridge can initialize
            // and process the intent.

            Context context = getApplicationContext();
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

            if (launchIntent != null) {
                // Ensure the flags match the original notification intent handling
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Copy the alarmId extra to the launch intent
                launchIntent.putExtra("alarmId", alarmId);

                context.startActivity(launchIntent);
                Log.d(TAG, "Plugin instance was null. Relaunching main activity with intent.");
            } else {
                Log.e(TAG, "Could not get launch intent for package.");
            }
        } else {
            Log.e(TAG, "Failed to handle notification tap: Plugin instance is null and alarmId is invalid.");
        }

        // Important: Always finish the activity immediately.
        finish();
    }
}
