"use strict"

cordova.commandProxy.add("CsDeviceInfo", {
    getAppId: function (successCb, failCb)
    {
        setTimeout(function () {
            try {
                successCb(Windows.ApplicationModel.Package.current.id.ProductId)
            } catch (e) {
                failCb(e)
            }
        }, 0)
    },

    getVersionName: function (successCb, failCb)
    {
        setTimeout(function () {
            try {
                var version = Windows.ApplicationModel.Package.current.id.version
                successCb(
                    ""  +version.major
                    +"."+version.minor
                    +"."+version.build
                    +"."+version.revision
                )
            } catch (e) {
                failCb(e)
            }
        }, 0)
    },

    isHackedDevice: function (successCb, failCb)
    {
        // Assume Windows devices are not "hacked"...
        setTimeout(function () { successCb(false) }, 0)
    },
})
