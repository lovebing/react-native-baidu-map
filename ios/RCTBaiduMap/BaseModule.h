//
//  BaseModule.h
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#ifndef BaseModule_h
#define BaseModule_h

#import "React/RCTBridgeModule.h"
#import "React/RCTEventDispatcher.h"
#import "React/RCTBridge.h"

#import <BaiduMapAPI_Base/BMKBaseComponent.h>
#import <BaiduMapAPI_Map/BMKMapComponent.h>
#import <BaiduMapAPI_Search/BMKSearchComponent.h>
#import <BaiduMapAPI_Utils/BMKUtilsComponent.h>

@interface BaseModule : NSObject <RCTBridgeModule> {
    UINavigationController *navigationController;
    BMKMapManager* _mapManager;
}

-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body;

-(NSMutableDictionary *)getEmptyBody;

@end

#endif /* BaseModule_h */
