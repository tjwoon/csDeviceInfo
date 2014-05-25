#import "CsDeviceInfo.h"

@implementation CsDeviceInfo

- (void)getVersionName: (CDVInvokedUrlCommand*)command
{
    NSString *versionName = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:versionName];
    [self success: result callbackId: command.callbackId];
}

@end
