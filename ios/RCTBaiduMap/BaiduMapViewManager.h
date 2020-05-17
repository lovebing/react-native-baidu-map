//
//  RCTBaiduMapViewManager.h
//  RCTBaiduMap
//
//  Created by lovebing on Aug 6, 2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#ifndef RCTBaiduMapViewManager_h
#define RCTBaiduMapViewManager_h

#import <BMKLocationkit/BMKLocationComponent.h>
#import "BaiduMapView.h"
#import "OverlayUtils.h"
#import "BMKPointAnnotationPro.h"
#import "OverlayArc.h"

@interface BaiduMapViewManager : RCTViewManager<BMKMapViewDelegate>

+ (void)initSDK:(NSString *)key;

- (void)sendEvent:(BaiduMapView *) mapView params:(NSDictionary *) params;

@end

#endif /* RCTBaiduMapViewManager_h */
