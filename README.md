Cordova DeviceInfo Plugin
=========================

This plugin contains various methods for retrieving miscellaneous device/app
info.


Installation
------------

### Using the Cordova CLI

`cordova plugins add org.cloudsky.cordovaplugins.deviceinfo`


Usage
-----

### Get App ID

```javascript
cloudSky.deviceInfo.getAppId(
    function (appId) {
        // Success callback.
        // `appId` is the package name on Android, bundle identifier on iOS,
        // `ProductId` on Windows.
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
        // On Windows, this is the version number built from components
        // <major>.<minor>.<build>.<revision> of the WinJS PackageId class
        // (https://msdn.microsoft.com/en-us/library/windows/apps/windows.applicationmodel.packageid.aspx#properties)
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
        // NOTE: `isHacked` is always false on Windows - no checking is
        // performed on this platform...
    },
    function (err) {
        // Failure callback.
        // `err` is an error message.
    }
)
```


Troubleshooting
---------------

### Android support library build error

If you get the following error when building on Android:

```
* What went wrong:
Execution failed for task ':processArmv7ReleaseManifest'.
> Manifest merger failed : Attribute meta-data#android.support.VERSION@value value=(26.0.0-alpha1) from [com.android.support:support-v4:26.0.0-alpha1] AndroidManifest.xml:27:9-38
  	is also present at [com.android.support:appcompat-v7:25.3.0] AndroidManifest.xml:27:9-31 value=(25.3.0).
  	Suggestion: add 'tools:replace="android:value"' to <meta-data> element at AndroidManifest.xml:25:5-27:41 to override.
```

Try adding this to your `cordova/platforms/android/build.gradle` file, replacing
the `useVersion` with the ones you need:

```
dependencies {
    configurations.all {
        resolutionStrategy.eachDependency { DependencyResolveDetails details ->
            def requested = details.requested
            if (requested.group == 'com.android.support') {
                if (requested.name == 'appcompat-v7') {
                    details.useVersion '23.+'
                } else if (requested.name == 'support-v4') {
                    details.useVersion '26.+'
                } else if (requested.name == 'support-compat') {
                    details.useVersion '25.0.1'
                }
            }
        }
    }
}
```
