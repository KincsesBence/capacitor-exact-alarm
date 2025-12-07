package hu.bk.plugins.capacitorExactAlarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.content.BroadcastReceiver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONObject;

import java.util.Calendar;

@CapacitorPlugin(name = "capacitorAlarm")
public class capacitorExactAlarmPlugin extends Plugin {

    private static capacitorExactAlarmPlugin instance;
    private BroadcastReceiver alarmReceiver;

    private AlarmStorage alarmStorage;

    private PluginCall currentCall;

    private ActivityResultLauncher<Intent> soundPickerLauncher;

    @Override
    public void load() {
        super.load();
        instance = this;
        alarmStorage = AlarmStorage.getInstance(getContext());
        setActivity();
        setUpAlarmBroadcast();

        Intent launchIntent = getActivity().getIntent();
        if (launchIntent != null && launchIntent.hasExtra("alarmId") &&
                !launchIntent.getBooleanExtra("handledByPlugin", false)) {
            int alarmId = launchIntent.getIntExtra("alarmId", -1);
            if (alarmId != -1) {
                // Process the notification tap immediately upon plugin load
                Log.d(getLogTag(), "Processing launch intent on load() for alarmId: " + alarmId);
                handleNotificationTap(launchIntent);
            }
        }
    }

    /*@Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);

        // Check if the intent came from our notification launch (which copies the extra)
        if (intent.hasExtra("alarmId") && !intent.getBooleanExtra("handledByPlugin", false)) {
            int alarmId = intent.getIntExtra("alarmId", -1);
            Log.d(getLogTag(), "handleOnNewIntent alarmId: " + alarmId);
            if (alarmId != -1) {
                handleNotificationTap(intent);
            }
        }
    }*/

    public void handleNotificationTap(Intent intent) {
        int alarmId = intent.getIntExtra("alarmId", -1);
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");
        String soundName = intent.getStringExtra("soundName");
        String data = intent.getStringExtra("data");

        JSObject ret = new JSObject();
        ret.put("alarmId", alarmId);
        ret.put("title", title);
        ret.put("msg", msg);
        ret.put("soundName", soundName);

        if (data == null || data.length() == 0) {
            ret.put("data",new JSONObject());
        } else {
            try {
                ret.put("data",new JSONObject(data));
            }catch (Exception e){
                Log.e("handleNotificationTap", "invalid JSON" );
            }
        }

        Log.d("handleNotificationTap", "alarmId: "+alarmId);
        // Emit the event to the web view. The web side must listen for 'alarmNotificationTapped'.
        notifyListeners("alarmNotificationTapped", ret,true);

    }

    public static capacitorExactAlarmPlugin getInstance() {
        return instance;
    }

    public void setUpAlarmBroadcast(){
        alarmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                JSObject data = new JSObject();
                data.put("alarmId", intent.getIntExtra("alarmId", -1));
                data.put("title", intent.getStringExtra("title"));
                data.put("msg", intent.getStringExtra("msg"));
                data.put("soundName", intent.getStringExtra("soundName"));
                data.put("timestamp", intent.getLongExtra("timestamp", 0));

                if (data == null || data.length() == 0) {
                    data.put("data",new JSONObject());
                } else {
                    try {
                        data.put("data",new JSONObject(intent.getStringExtra("data")));
                    }catch (Exception e){
                        Log.e("handleNotificationTap", "invalid JSON" );
                    }
                }

                notifyListeners("alarmTriggered", data);
            }
        };

        IntentFilter filter = new IntentFilter("CAPACITOR_ALARM_EVENT");
        Context context = getContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(alarmReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }

    }

    public void setActivity(){
        soundPickerLauncher =
        bridge.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    JSObject res = new JSObject();
                    res.put("uri", uri.toString());
                    currentCall.resolve(res);
                } else {
                    currentCall.reject("Cancelled");
                }
                currentCall = null;
            }
        });
    }

    @PluginMethod
    public void setAlarm(PluginCall call) {

        long timestamp = call.getLong("timestamp", 0L);
        long repeatInterval = call.getDouble("repeatInterval", 0.0).longValue();
        Log.d("setAlarm",  "call params: " + call.getData().toString());
        String title = call.getString("title", "Alarm");
        String msg = call.getString("msg", "Time’s up!");
        String soundName = call.getString("soundName");
        JSObject dataObject = call.getObject("data");
        String icon = call.getString("icon");
        String dismissText = call.getString("dismissText","Dismiss");
        String missedText = call.getString("missedText","Missed Alarm:");
        JSObject calendarObj = call.getObject("calendar");

        Log.d("setAlarm", "calendar!" + timestamp);


        long currentTimestamp = System.currentTimeMillis();

        if (repeatInterval > 0) {
            Log.d("setAlarm", "repeatInterval!" + repeatInterval);
            timestamp = currentTimestamp + repeatInterval;
        }

        if (calendarObj != null) {
            timestamp = computeNextCalendarTimestamp(calendarObj);
            Log.d("setAlarm", "calendar!" + timestamp);
        }

        String data="{}";
        if(dataObject!=null){
            data = dataObject.toString();
        }


        if (timestamp <= 0) {
            call.reject("Invalid timestamp: "+timestamp);
            return;
        }


        if (timestamp <= currentTimestamp) {
            call.reject("expired timestamp");
            return;
        }

        int alarmId = (int) (System.currentTimeMillis() & 0xfffffff);

        Context context = getContext();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_" + alarmId);
        intent.putExtra("soundName", soundName);
        intent.putExtra("alarmId", alarmId);
        intent.putExtra("title", title);
        intent.putExtra("msg", msg);
        intent.putExtra("data", data);
        intent.putExtra("icon", icon);
        intent.putExtra("dismissText", dismissText);
        intent.putExtra("missedText", missedText);

        if (calendarObj != null) {
            intent.putExtra("calendar", calendarObj.toString());
        }

        if (repeatInterval > 0) {
            intent.putExtra("repeatInterval", repeatInterval);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timestamp,
                pendingIntent
        );

        JSObject alarmData = new JSObject();
        alarmData.put("id", alarmId);
        alarmData.put("timestamp", timestamp);
        alarmData.put("title", title);
        alarmData.put("msg", msg);
        alarmData.put("soundName", soundName);
        alarmData.put("data", data);


        alarmStorage.addAlarm(alarmData);

        JSObject result = new JSObject();
            result.put("id", alarmId);
            result.put("timestamp", timestamp);
            result.put("title", title);
            result.put("msg", msg);
            result.put("soundName", soundName);
            result.put("data", data);
        call.resolve(result);

        Log.d("setAlarm", "Alarm set!"+timestamp);
    }

    public static long computeNextCalendarTimestamp(JSObject calObj) {

        Calendar c = Calendar.getInstance();

        boolean hasWeekday = calObj.has("weekday");
        boolean hasDay = calObj.has("day");

        // Base: today’s date
        c.set(Calendar.SECOND, calObj.getInteger("second", 0));
        c.set(Calendar.MILLISECOND, 0);

        if (calObj.has("hour")) c.set(Calendar.HOUR_OF_DAY, calObj.getInteger("hour"));
        if (calObj.has("minute")) c.set(Calendar.MINUTE, calObj.getInteger("minute"));

        // Weekly recurrence (weekday: 1–7, where 1 = Sunday or use ISO if you prefer)
        if (hasWeekday) {
            int targetDay = calObj.getInteger("weekday");
            c.set(Calendar.DAY_OF_WEEK, targetDay);

            // If time has passed today, go to next week
            if (c.getTimeInMillis() <= System.currentTimeMillis()) {
                c.add(Calendar.WEEK_OF_YEAR, 1);
            }
            return c.getTimeInMillis();
        }

        // Monthly recurrence
        if (hasDay) {
            int targetDay = calObj.getInteger("day");
            c.set(Calendar.DAY_OF_MONTH, targetDay);

            if (c.getTimeInMillis() <= System.currentTimeMillis()) {
                c.add(Calendar.MONTH, 1);
            }
            return c.getTimeInMillis();
        }

        // Daily recurrence
        if (c.getTimeInMillis() <= System.currentTimeMillis()) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        return c.getTimeInMillis();
    }


    @PluginMethod
    public void cancelAllAlarm(PluginCall call){
        Context context = getContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        JSArray alarms = alarmStorage.getAlarms();

        for (int i = 0; i < alarms.length(); i++) {

            try {
                Object item = alarms.get(i); // may throw JSONException
                if (item instanceof JSObject) {
                    JSObject obj = (JSObject) item;
                    int alarmId = obj.optInt("id");

                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.setAction("ALARM_" + alarmId);
                    intent.putExtra("alarmId", alarmId);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context,
                            alarmId,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    // Cancel the alarm
                    alarmManager.cancel(pendingIntent);
                }
            } catch (Exception e) {
                // Skip any invalid items
                e.printStackTrace();
                continue;
            }
        }

        try {
            alarms = new JSArray("[]");
        } catch (Exception e) {
            e.printStackTrace();
        }

        alarmStorage.clearAlarms();
        call.resolve();
    }

    @PluginMethod
    public void cancelAlarm(PluginCall call){
        int alarmId = call.getInt("alarmId", -1);
        if (alarmId == -1) {
            call.reject("Invalid alarm id "+alarmId);
            return;
        }

        Context context = getContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Recreate PendingIntent to cancel the alarm
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_" + alarmId);
        intent.putExtra("alarmId", alarmId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Cancel the alarm
        alarmManager.cancel(pendingIntent);

        alarmStorage.removeAlarm(alarmId);

        call.resolve();
    }


    @PluginMethod
    public void getAlarms(PluginCall call) {
        JSObject result = new JSObject();
        JSArray alarms = alarmStorage.getAlarms();
        result.put("alarms", alarms);
        call.resolve(result);
    }

    @PluginMethod
    public void stopAlarm(PluginCall call) {
        Context ctx = getContext();
        Intent stop = new Intent(ctx, AlarmService.class);
        stop.setAction("STOP_ALARM");
        ctx.startService(stop);

        call.resolve();
    }

    @PluginMethod
    public void requestExactAlarmPermission(PluginCall call) {
        Context context = getContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        call.resolve();
    }


    @PluginMethod
    public void requestNotificationPermission(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Context context = getContext();
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        12345 // request code
                );
            }
        }
        call.resolve();
    }

    @PluginMethod
    public void checkNotificationPermission(PluginCall call){
        Context context = getContext();

        Boolean hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;

        JSObject result = new JSObject();
        result.put("hasPermission", hasPermission);
        call.resolve(result);
    }

    @PluginMethod
    public void checkExactAlarmPermission(PluginCall call){
        Context context = getContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Boolean hasPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms();

        JSObject result = new JSObject();
        result.put("hasPermission", hasPermission);
        call.resolve(result);
    }

    @PluginMethod
    public void pickAlarmSound(PluginCall call) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);

        currentCall=call;
        soundPickerLauncher.launch(intent);
    }

    // Call this from your alarm receiver / service when alarm triggers
    public void notifyAlarmTriggered(Intent intent) {
        int alarmId = intent.getIntExtra("alarmId", -1);
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");
        String soundName = intent.getStringExtra("soundName");
        String data = intent.getStringExtra("data");

        JSObject ret = new JSObject();
        ret.put("alarmId", alarmId);
        ret.put("title", title);
        ret.put("msg", msg);
        ret.put("soundName", soundName);

        if (data == null || data.length() == 0) {
            ret.put("data",new JSONObject());
        } else {
            try {
                ret.put("data",new JSONObject(data));
            }catch (Exception e){
                Log.e("handleNotificationTap", "invalid JSON" );
            }
        }

        notifyListeners("alarmTriggered", ret);
    }
}
