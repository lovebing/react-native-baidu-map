//
//  BaseModule.h
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridge.h>

#import <BaiduMapAPI_Base/BMKBaseComponent.h>
#import <BaiduMapAPI_Map/BMKMapComponent.h>
#import <BaiduMapAPI_Search/BMKSearchComponent.h>
#import <BaiduMapAPI_Utils/BMKUtilsComponent.h>

@interface BaseModule : RCTEventEmitter <RCTBridgeModule> {
    UINavigationController *navigationController;
    BMKMapManager* _mapManager;
}

-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body;

-(NSMutableDictionary *)getEmptyBody;

@end
