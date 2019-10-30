//
//  BaiduMapManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2019/10/27.
//  Copyright © 2019 lovebing.org. All rights reserved.
//

#import "BaiduMapManager.h"

@implementation BaiduMapManager

RCT_EXPORT_MODULE(BaiduMapManager)

RCT_EXPORT_METHOD(initSDK:(NSString *)key) {
    if (_mapManager != nil) {
        return;
    }
    _mapManager = [[BMKMapManager alloc]init];
    [[BMKLocationAuth sharedInstance] checkPermisionWithKey:key authDelegate:nil];
    BOOL ret = [_mapManager start:key  generalDelegate:nil];
    if (!ret) {
        NSLog(@"manager start failed!");
    }
}

@end
