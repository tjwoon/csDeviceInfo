package org.cloudsky.cordovaPlugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class DeviceInfo extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
    throws JSONException
    {
        if(action.equals("getVersionName")) {
            Activity act = cordova.getActivity();
            PackageManager packageManager = act.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(
                    act.getApplicationContext().getPackageName(),
                    0
                );
                callbackContext.success(packageInfo.versionName);
            } catch (NameNotFoundException e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        } else {
            return false;
        }
    }
}
