package com.renlianiot.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerMusicService extends Service {
    private final static String TAG = RLMusicPlugin.TAG;
    private MediaPlayer mMediaPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), MResource.getIdByName(getApplication().getPackageName(), "raw", "ding"));
        mMediaPlayer.setLooping(true);
    }


    public static final String ONCLICK = "com.app.onclick";


    private BroadcastReceiver receiverOnclick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ONCLICK)) {
                //Intent newIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(newIntent);
            }
        }
    };

    public static PlayerMusicService playerMusicService = null;
    private static void startNotification(String title, String content, String para) {
            if (playerMusicService == null) {
                Log.e(TAG, "playerMusicService null");
                return;
            }
             Log.d(TAG, "title:" + title + " content:" + content + " para:" + para);
            //静态注册
            Intent intent2 =new Intent (playerMusicService.getApplicationContext(), NotificationClickReceiver.class);
            if (para != null) {
                intent2.putExtra("para", para);
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(playerMusicService.getApplicationContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

            //动态注册
            // IntentFilter filter_click = new IntentFilter();
            // filter_click.addAction(ONCLICK);
            // //注册广播
            // registerReceiver(receiverOnclick, filter_click);
            // Intent Intent_pre = new Intent(ONCLICK);
            // //得到PendingIntent
            // PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, Intent_pre, 0);



            NotificationManager notificationManager= (NotificationManager) playerMusicService.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification=new NotificationCompat.Builder(playerMusicService.getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    // .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setSmallIcon(MResource.getIdByName(playerMusicService.getApplication().getPackageName(), "mipmap","ic_launcher"))
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            int  notifiId=1;
    //      notificationManager.notify(notifiId,notification);
            playerMusicService.startForeground(notifiId, notification);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (playerMusicService == null) {
            playerMusicService = this;
        } else {
            if (intent.getStringExtra("title") != null) {//只是修改通知栏内容
                 startNotification(intent.getStringExtra("title"), intent.getStringExtra("content"), intent.getStringExtra("para"));
                 return START_STICKY;
            }
        }


        //playerMusicService.stopForeground(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();


        startNotification(intent.getStringExtra("title"), intent.getStringExtra("content"), intent.getStringExtra("para"));
        return START_STICKY;
    }


    private void startPlayMusic(){
        if(mMediaPlayer != null){

            PowerManager pm= (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myservice");
            wl.acquire();

            mMediaPlayer.start();
        }
    }


    private void stopPlayMusic(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        playerMusicService = null;
        // 重启
        Intent intent = new Intent(getApplicationContext(),PlayerMusicService.class);
        startService(intent);
    }
}
