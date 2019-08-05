package com.renlianiot.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (RLMusicPlugin.CordovaActivity == null) {
        	Log.e(RLMusicPlugin.TAG, "RLMusicPlugin.CordovaActivity null");
        	return;
        }
        Log.i("TAG", "userClick:我被点击啦！！！ " + intent.getStringExtra("para"));
        Intent newIntent = new Intent(context, RLMusicPlugin.CordovaActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}