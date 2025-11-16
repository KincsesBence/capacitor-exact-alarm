package hu.bk.plugins.capacitorAlarm;

import android.content.Context;
import android.content.SharedPreferences;
import com.getcapacitor.JSArray;
import org.json.JSONObject;

public class AlarmStorage {

    private static volatile AlarmStorage instance;

    private final Context context;
    private JSArray alarms = new JSArray();

    // Private constructor ➜ prevents direct instantiation
    private AlarmStorage(Context context) {
        this.context = context.getApplicationContext();
        loadAlarms();
    }

    // Public getter for singleton instance
    public static AlarmStorage getInstance(Context context) {
        if (instance == null) {                       // First check (no locking)
            synchronized (AlarmStorage.class) {
                if (instance == null) {               // Second check (with locking)
                    instance = new AlarmStorage(context);
                }
            }
        }
        return instance;
    }

    public JSArray getAlarms() {
        loadAlarms();
        return alarms;
    }

    public void setAlarms(JSArray alarms) {
        this.alarms = alarms;
        saveAlarms();
    }

    public void addAlarm(Object alarmObj) {
        alarms.put(alarmObj);
        saveAlarms();
    }

    public void clearAlarms() {
        alarms = new JSArray();
        saveAlarms();
    }

    public void removeAlarm(int alarmId) {
        try {
            for (int i = 0; i < alarms.length(); i++) {
                try {
                    JSONObject item = alarms.getJSONObject(i);
                    int currentAlarmId = item.getInt("id");
                    if (currentAlarmId == alarmId) {
                        alarms.remove(i);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            saveAlarms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAlarms() {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
            prefs.edit().putString("alarms", alarms.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadAlarms();
    }

    private void loadAlarms() {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
            String json = prefs.getString("alarms", "[]");
            alarms = new JSArray(json);
        } catch (Exception e) {
            alarms = new JSArray();
            e.printStackTrace();
        }
    }

    public void updateAlarmTimeStamp(int alarmId, long newTimestamp){
        try {
            for (int i = 0; i < alarms.length(); i++) {
                try {
                    JSONObject item = alarms.getJSONObject(i);
                    int currentAlarmId = item.getInt("id");
                    if (currentAlarmId == alarmId) {
                        item.put("timestamp", newTimestamp);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            saveAlarms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
