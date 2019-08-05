package com.renlianiot.music;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;

import android.util.Log;
/**
 * This class echoes a string called from JavaScript.
 */
public class RLMusicPlugin extends CordovaPlugin {
    public static String TAG = "RLMusicPlugin";
    public static Activity CordovaActivity = null;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (CordovaActivity == null) {
            CordovaActivity = cordova.getActivity();
        }
        try {
             if (action.equals("startMusicService") || action.equals("setNotification")) {
                Intent intent = new Intent(cordova.getActivity(), com.renlianiot.music.PlayerMusicService.class);
                intent.putExtra("title", args.getString(0));
                intent.putExtra("content", args.getString(1));
                intent.putExtra("para", args.getString(2));
                cordova.getActivity().startService(intent);
                return true;
            }
            if (action.equals("coolMethod")) {
                String message = args.getString(0);
                this.coolMethod(message, callbackContext);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            Toast.makeText(cordova.getActivity(), message, Toast.LENGTH_SHORT).show();
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
