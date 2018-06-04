#import <Cordova/CDV.h>

@interface CsDeviceInfo : CDVPlugin

- (void)getAppId: (CDVInvokedUrlCommand*)command;
- (void)getVersionName: (CDVInvokedUrlCommand*)command;
- (void)isHackedDevice: (CDVInvokedUrlCommand*)command;
- (void)getHackDetectionDetails: (CDVInvokedUrlCommand*)command;

@end
