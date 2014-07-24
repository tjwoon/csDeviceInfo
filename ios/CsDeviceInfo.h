#import <Cordova/CDV.h>

@interface CsDeviceInfo : CDVPlugin

- (void)getVersionName: (CDVInvokedUrlCommand*)command;
- (void)isHackedDevice: (CDVInvokedUrlCommand*)command;

@end
