//
//  BaiduMapManager.h
//  RCTBaiduMap
//
//  Created by lovebing on 2019/10/27.
//  Copyright © 2019 lovebing.org. All rights reserved.
//


#ifndef BaiduMapManager_h
#define BaiduMapManager_h

#import <React/RCTBridgeModule.h>
#import <BMKLocationkit/BMKLocationComponent.h>
#import <BaiduMapAPI_Map/BMKMapView.h>
#import <BaiduMapAPI_Utils/BMKUtilsComponent.h>

@interface BaiduMapManager : NSObject<RCTBridgeModule>

@property(atomic) BMKMapManager* mapManager;

@end

#endif
