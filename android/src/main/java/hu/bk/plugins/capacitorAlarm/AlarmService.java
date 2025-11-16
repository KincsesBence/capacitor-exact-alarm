package hu.bk.plugins.capacitorAlarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AlarmService extends Service {

    private MediaPlayer player;
    private Vibrator vibrator;

    public boolean uriExists(Context context,Uri uri) {
        try {

            // Try opening the stream
            InputStream is = context.getContentResolver().openInputStream(uri);

            if (is != null) {
                is.close();
                return true;
            }

            return false;
        } catch (FileNotFoundException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "STOP_ALARM".equals(intent.getAction())) {
            stopAlarm();
            stopSelf();
            return START_NOT_STICKY;
        }

        int alarmId = intent.getIntExtra("alarmId", -1);
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");
        String soundName = intent.getStringExtra("soundName");
        String data = intent.getStringExtra("data");
        String icon = intent.getStringExtra("icon");
        String dismissText = intent.getStringExtra("dismissText");
        String missedText = intent.getStringExtra("missedText");


        Uri soundUri = Uri.parse(soundName);

        Log.d("AlarmService", "onStartCommand: "+soundName+" "+soundUri.toString());

        // Try default alarm
        if (!uriExists(this,soundUri)) {
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Log.d("AlarmService", "onStartCommand: Try default alarm");
        }

        // try fallback to ringtone
        if (!uriExists(this,soundUri)) {
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Log.d("AlarmService", "onStartCommand: Try fallback to ringtone");
        }

        player = new MediaPlayer();
        try {
            player.setDataSource(this, soundUri);
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.prepare();
            player.start();
            player.setOnCompletionListener(mp -> {
                Log.d("AlarmService", "Sound finished playing");
                stopSelf();
                NotificationHelper.showNotification(this,missedText + title ,msg,icon);
            });

        } catch (Exception e) {
            Log.e("AlarmService", "Error playing sound: " + e.getMessage());
        }

        // Vibrate the phone
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 500, 250, 500};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect effect = VibrationEffect.createWaveform(pattern, 0);
                vibrator.vibrate(effect);
            } else {
                vibrator.vibrate(pattern, 0);
            }
        }

        // Start foreground to keep service alive
        startForeground(1,
            NotificationHelper.buildServiceNotification(
                this,
                alarmId,
                title,
                msg,
                soundUri.toString(),
                data,
                icon,
                dismissText
            )
        );

        return START_STICKY;
    }

    private void stopAlarm() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }

        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        stopAlarm();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
