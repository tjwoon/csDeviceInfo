#import "CsDeviceInfo.h"

#include <sys/types.h> // for fork()

// Config for Jailbroken device detection ------------------------------

#define JAILBREAK_RETURN_YES_IF_RESULT()                     \
    if(result) {                                             \
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId]; \
        return;                                              \
    }

static char *jb_existenceTests[] = {
    "/bin/bash",
    "/Applications/Cydia.app",
    "/private/var/lib/apt",
    "/Library/MobileSubstrate/MobileSubstrate.dylib",
    0
};

static NSString *jb_tmpFilePath = @"/ge_starbuddy_jb_test";


// Implementation ------------------------------------------------------

@implementation CsDeviceInfo

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
    CDVPluginResult
        *result = nil,
        *resultYes = [CDVPluginResult
                      resultWithStatus:CDVCommandStatus_OK
                      messageAsBool:YES],
        *resultNo = [CDVPluginResult
                     resultWithStatus:CDVCommandStatus_OK
                     messageAsBool:NO];

    NSFileManager *fileManager = [NSFileManager defaultManager];

    // Check files/directories existence
    char **existenceTestPath;
    NSString *testPath;
    for(existenceTestPath=jb_existenceTests; *existenceTestPath; existenceTestPath++) {
        testPath = [NSString stringWithCString:existenceTestPath encoding:NSUTF8StringEncoding];
        if([fileManager fileExistsAtPath:testPath]) {
            result = resultYes;
            break;
        }
    }

    JAILBREAK_RETURN_YES_IF_RESULT()

    // Check if app is able to open Cydia
    if([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"cydia://"]]) {
        result = resultYes;
    }

    JAILBREAK_RETURN_YES_IF_RESULT()

    // Check if app is able to fork()
    pid_t forkResult = fork();
    if(forkResult == 0) exit(0);
    else if(forkResult != -1) result = resultYes;

    JAILBREAK_RETURN_YES_IF_RESULT()

    // Check if app is able to write outside of the iOS app sandbox
    BOOL fileWritten = [@"temporary test"
                        writeToFile:jb_tmpFilePath
                        atomically:YES
                        encoding:kCFStringEncodingUTF8
                        error:NULL];
    if(fileWritten) {
        result = resultYes;
        [fileManager removeItemAtPath:jb_tmpFilePath error:NULL];
    }

    JAILBREAK_RETURN_YES_IF_RESULT()

    // Not jailbroken as far as we can tell...
    [self.commandDelegate sendPluginResult:resultNo callbackId:command.callbackId];
}

@end
