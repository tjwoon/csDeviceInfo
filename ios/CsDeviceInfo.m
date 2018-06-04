#import "CsDeviceInfo.h"

#include <sys/types.h> // for fork()
#import "DTTJailbreakDetection.h"

// Config for Jailbroken device detection ------------------------------

static char *jb_existenceTests[] = {
    "/bin/bash",
    "/Applications/Cydia.app",
    "/private/var/lib/apt",
    "/Library/MobileSubstrate/MobileSubstrate.dylib",
    0
};

static NSString *jb_tmpFilePath = @"/csdeviceinfo_jb_test";


// Implementation ------------------------------------------------------

@implementation CsDeviceInfo

- (NSDictionary*)jailbreakDetectionDetails
{
    NSFileManager *fileManager = [NSFileManager defaultManager];

    NSMutableDictionary *results = [[NSMutableDictionary alloc] init];

    // Check via Jailbreak detection library
    [results setObject:[NSNumber numberWithBool:[DTTJailbreakDetection isJailbroken]]
             forKey:@"DTTJailbreakDetection"];

    // Check files/directories existence
    char **existenceTestPath;
    NSString *testPath;
    for(existenceTestPath=jb_existenceTests; *existenceTestPath; existenceTestPath++) {
        testPath = [NSString stringWithCString:*existenceTestPath encoding:NSUTF8StringEncoding];
        NSString *testName = @"fileExistence: ";
        testName = [testName stringByAppendingString:testPath];
        [results setObject:[NSNumber numberWithBool:[fileManager fileExistsAtPath:testPath]]
                 forKey:testName];
    }

    // Check if app is able to open Cydia
    [results
     setObject:[NSNumber numberWithBool:[[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"cydia://"]]]
     forKey:@"Cydia"];

    // Check if app is able to fork()
    pid_t forkResult = fork();
    if(forkResult == 0) exit(0);
    else if(forkResult != -1) {
        [results setObject:[NSNumber numberWithBool:YES] forKey:@"fork"];
    }

    // Check if app is able to write outside of the iOS app sandbox
    BOOL fileWritten = [@"temporary test"
                        writeToFile:jb_tmpFilePath
                        atomically:YES
                        encoding:kCFStringEncodingUTF8
                        error:NULL];
    [results setObject:[NSNumber numberWithBool:fileWritten] forKey:@"sandbox"];
    if(fileWritten) {
        [fileManager removeItemAtPath:jb_tmpFilePath error:NULL];
    }

    return results;
}

- (void)getAppId: (CDVInvokedUrlCommand*)command
{
    NSString *bundleId = [[NSBundle mainBundle] bundleIdentifier];
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                               messageAsString:bundleId];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)getVersionName: (CDVInvokedUrlCommand*)command
{
    NSString *versionName = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:versionName];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)isHackedDevice: (CDVInvokedUrlCommand*)command
{
    NSDictionary *detection = [self jailbreakDetectionDetails];
    for (NSString *key in detection) {
      if(((NSNumber*)detection[key]).boolValue) {
        [self.commandDelegate
            sendPluginResult:[CDVPluginResult
                              resultWithStatus:CDVCommandStatus_OK
                              messageAsBool:YES]
            callbackId:command.callbackId];
        return;
      }
    }

    [self.commandDelegate
        sendPluginResult:[CDVPluginResult
                          resultWithStatus:CDVCommandStatus_OK
                          messageAsBool:NO]
        callbackId:command.callbackId];
}

- (void)getHackDetectionDetails: (CDVInvokedUrlCommand*)command
{
    [self.commandDelegate
        sendPluginResult:[CDVPluginResult
                          resultWithStatus:CDVCommandStatus_OK
                          messageAsDictionary:[self jailbreakDetectionDetails]]
        callbackId:command.callbackId];
}

@end
