'use strict';

var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');

function DeviceInfo () {};

DeviceInfo.prototype = {

    getVersionName: function (success, error)
    {
        argscheck.checkArgs('fF', 'CsDeviceInfo.getVersionName', arguments);
        exec(success, error, 'CsDeviceInfo', 'getVersionName', []);
    },

};

module.exports = new DeviceInfo();
