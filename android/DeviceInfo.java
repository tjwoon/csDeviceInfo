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

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.scottyab.rootbeer.RootBeer;

public class DeviceInfo extends CordovaPlugin {

    static private final String ACTION_GETAPPID = "getAppId";
    static private final String ACTION_GETVERSIONNAME = "getVersionName";
    static private final String ACTION_ISHACKEDDEVICE = "isHackedDevice";

    // Root checking config
    static private final String[] ROOT_PACKAGES = {
        "com.noshufou.android.su",
        "com.thirdparty.superuser",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.zachspong.temprootremovejb",
        "com.ramdroid.appquarantine",
        "david.lahuta.superuser.free.pro",
    };
    static private final String[] ROOT_FILES = {
        "/system/bin/su",
        "/system/xbin/su",
        "/sbin/su",
        "/system/su",
        "/system/bin/.ext/.su",
        "/system/usr/we-need-root/su-backup",
        "/system/xbin/mu",
    };
    static private final String[] ROOT_DIRECTORYPERMISSIONS = {
        "/",
        "/data",
        "/dev",
        "/etc",
        "/proc",
        "/sbin",
        "/system",
        "/system/bin",
        "/system/sbin",
        "/system/xbin",
        "/sys",
        "/vendor/bin",
    };
    static private final String[] ROOT_COMMANDS = {
        "su",
        "busybox",
    };

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
        } else {
            return false;
        }
    }

    private void checkIfRooted (CallbackContext callbackContext)
    {
        Boolean rootBeerRooted = false;
        RootBeer rootBeer = new RootBeer(cordova.getActivity().getApplicationContext());
        if (rootBeer.isRootedWithoutBusyBoxCheck()) rootBeerRooted = true;

        if(  rootBeerRooted
          || hasRootPackages()
          || hasRootFiles()
          || hasRootPermissions()
          || hasRootCommands()
        ) {
            callbackContext.success(1);
        } else {
            callbackContext.success(0);
        }
    }

    private boolean hasRootPackages ()
    {
        Activity act = cordova.getActivity();
        PackageManager packageManager = act.getPackageManager();
        for(String pkg: ROOT_PACKAGES) {
            try {
                packageManager.getPackageInfo(pkg, 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {}
        }
        return false;
    }

    private boolean hasRootFiles ()
    {
        for(String path: ROOT_FILES) {
            File f = new File(path);
            if(f.exists()) return true;
        }
        return false;
    }

    private boolean hasRootPermissions ()
    {
        for(String path: ROOT_DIRECTORYPERMISSIONS) {
            File f = new File(path);
            if(f.canWrite()) return true;
        }
        return false;
    }

    private boolean hasRootCommands ()
    {
        Runtime runtime = Runtime.getRuntime();
        for(String cmd: ROOT_COMMANDS) {
            try {
                Process p = runtime.exec(cmd);
                p.destroy();
                return true;
            } catch (IOException e) {}
        }
        return false;
    }
}
