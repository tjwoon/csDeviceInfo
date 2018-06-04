package org.cloudsky.cordovaPlugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.Process;
import java.lang.Runtime;
import java.util.Iterator;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.scottyab.rootbeer.RootBeer;

public class DeviceInfo extends CordovaPlugin {

    static private final String ACTION_GETAPPID = "getAppId";
    static private final String ACTION_GETVERSIONNAME = "getVersionName";
    static private final String ACTION_ISHACKEDDEVICE = "isHackedDevice";
    static private final String ACTION_GETHACKDETECTIONDETAILS = "getHackDetectionDetails";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
    throws JSONException
    {
        if(action.equals(ACTION_GETAPPID)) {
            callbackContext.success(
                cordova.getActivity().getApplicationContext().getPackageName()
            );
            return true;
        } else if(action.equals(ACTION_GETVERSIONNAME)) {
            Activity act = cordova.getActivity();
            PackageManager packageManager = act.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(
                    act.getApplicationContext().getPackageName(),
                    0
                );
                callbackContext.success(packageInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        } else if(action.equals(ACTION_ISHACKEDDEVICE)) {
            checkIfRooted(callbackContext);
            return true;
        } else if(action.equals(ACTION_GETHACKDETECTIONDETAILS)) {
            callbackContext.success(rootDetection());
            return true;
        } else {
            return false;
        }
    }

    private JSONObject rootDetection ()
    {
        JSONObject results = new JSONObject();
        RootBeer rootBeer = new RootBeer(cordova.getActivity().getApplicationContext());

        // Same tests as rootBeer.isRootedWithoutBusyBoxCheck()
        try {
            results.put("detectRootManagementApps", rootBeer.detectRootManagementApps());
            results.put("detectPotentiallyDangerousApps", rootBeer.detectPotentiallyDangerousApps());
            results.put("checkForBinary(\"su\")", rootBeer.checkForBinary("su"));
            results.put("checkForDangerousProps", rootBeer.checkForDangerousProps());
            results.put("checkForRWPaths", rootBeer.checkForRWPaths());
            results.put("detectTestKeys", rootBeer.detectTestKeys());
            results.put("checkSuExists", rootBeer.checkSuExists());
            results.put("checkForRootNative", rootBeer.checkForRootNative());
        } catch (JSONException e) {}

        return results;
    }

    private void checkIfRooted (CallbackContext callbackContext)
    {
        JSONObject detection = rootDetection();
        Iterator<String> keys = detection.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if(detection.optBoolean(key)) {
                callbackContext.success(1);
                return;
            }
        }

        callbackContext.success(0);
    }

}
