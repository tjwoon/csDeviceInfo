Cordova DeviceInfo Plugin
=========================

This plugin contains various methods for retrieving miscellaneous device/app
info.


Installation
------------

### Using the Cordova CLI

`cordova plugins install org.cloudsky.cordovaplugins.deviceinfo`


Usage
-----

### Get App ID

```javascript
cloudSky.deviceInfo.getAppId(
    function (appId) {
        // Success callback.
        // `appId` is the package name on Android, bundle identifier on iOS.
    },
    function (err) {
        // Failure callback.
        // `err` is an error message.
    }
)
```


### Get App Version Name

```javascript
cloudSky.deviceInfo.getVersionName(
    function (versionName) {
        // Success callback.
        // `versionName` is the version name on Android (from the
        // AndroidManifest's <manifest> element, `android:versionName` attribute).
        // On iOS, this is the `CFBundleShortVersionString` value from the main
        // NSBundle.
    },
    function (err) {
        // Failure callback.
        // `err` is an error message.
    }
)
```


### Get Device Jailbreak/Root Status

Performs a __simplistic__ check on whether the device is rooted or jailbroken.

```javascript
cloudSky.deviceInfo.isHackedDevice(
    function (isHacked) {
        // Success callback.
        // `isHacked` is _truthy_ if the device is rooted/jailbroken, _falsy_
        // otherwise (meaning it can be true, false, 1, 0, ...).
    },
    function (err) {
        // Failure callback.
        // `err` is an error message.
    }
)
```
