'use strict';

var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');

function DeviceInfo () {};

DeviceInfo.prototype = {

    getVersionName: function (success, error)
    {
        argscheck.checkArgs('fF', 'DeviceInfo.getVersionName', arguments);
        exec(success, error, 'DeviceInfo', 'getVersionName', []);
    },

};

module.exports = new DeviceInfo();
