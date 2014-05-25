#import <Cordova/CDV.h>

@interface CsDeviceInfo : CDVPlugin

- (void)getVersionName: (CDVInvokedUrlCommand*)command;

@end
