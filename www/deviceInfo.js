'use strict';

var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');

function DeviceInfo () {};

DeviceInfo.prototype = {

    getAppId: function (success, error)
    {
        argscheck.checkArgs('fF', 'CsDeviceInfo.getAppId', arguments);
        exec(success, error, 'CsDeviceInfo', 'getAppId', []);
    },

    getVersionName: function (success, error)
    {
        argscheck.checkArgs('fF', 'CsDeviceInfo.getVersionName', arguments);
        exec(success, error, 'CsDeviceInfo', 'getVersionName', []);
    },

    isHackedDevice: function (success, error)
    {
        argscheck.checkArgs('fF', 'CsDeviceInfo.isHackedDevice', arguments);
        exec(success, error, 'CsDeviceInfo', 'isHackedDevice', []);
    },

    getHackDetectionDetails: function (success, error)
    {
        argscheck.checkArgs('fF', 'CsDeviceInfo.getHackDetectionDetails', arguments);
        exec(success, error, 'CsDeviceInfo', 'getHackDetectionDetails', []);
    },

};

module.exports = new DeviceInfo();
