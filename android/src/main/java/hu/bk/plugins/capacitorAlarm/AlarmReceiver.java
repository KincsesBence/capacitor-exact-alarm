package hu.bk.plugins.capacitorAlarm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmStorage alarmStorage = AlarmStorage.getInstance(context);

        int alarmId = intent.getIntExtra("alarmId", -1);
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");
        String soundName = intent.getStringExtra("soundName");
        String data = intent.getStringExtra("data");
        String icon = intent.getStringExtra("icon");
        String dismissText = intent.getStringExtra("dismissText");
        String missedText = intent.getStringExtra("missedText");

        Log.d("AlarmReceiver", "Alarm fired: " + alarmId);

        Intent eventIntent = new Intent("CAPACITOR_ALARM_EVENT");
        eventIntent.putExtra("alarmId", alarmId);
        eventIntent.putExtra("title", title);
        eventIntent.putExtra("msg", msg);
        eventIntent.putExtra("soundName", soundName);
        eventIntent.putExtra("data", data);

        // broadcast only inside this app (private)
        eventIntent.setPackage(context.getPackageName());

        context.sendBroadcast(eventIntent);

        // Start the foreground service to handle alarm
        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarmId", alarmId);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("msg", msg);
        serviceIntent.putExtra("soundName", soundName);
        serviceIntent.putExtra("data", data);
        serviceIntent.putExtra("icon", icon);
        serviceIntent.putExtra("dismissText", dismissText);
        serviceIntent.putExtra("missedText", missedText);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }

        long interval = intent.getLongExtra("repeatInterval", 0);
        String calendarJson = intent.getStringExtra("calendar");


        if (interval > 0 || calendarJson != null) {
            long nextTime=0;

            if(interval > 0){
                nextTime = System.currentTimeMillis() + interval;
            }

            if(calendarJson != null){
                try{
                    JSObject calObj = new JSObject(calendarJson);
                    nextTime = capacitorAlarmPlugin.computeNextCalendarTimestamp(calObj);
                } catch (Exception e) {
                    Log.e("AlarmReceiver", "Failed to reschedule", e);
                }
            }

            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent nextIntent = new Intent(context, AlarmReceiver.class);
            nextIntent.setAction("ALARM_" + alarmId);  // ✅ same unique action
            nextIntent.putExtra("alarmId", alarmId);
            nextIntent.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmId,
                    nextIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextTime,
                    pendingIntent
            );
            Log.e("AlarmReceiver", "Alarm rescheduled");

            alarmStorage.updateAlarmTimeStamp(alarmId,nextTime);

        }else{
            alarmStorage.removeAlarm(alarmId);
        }

    }


}
