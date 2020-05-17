//
//  OverlayCircle.h
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef OverlayCircle_h
#define OverlayCircle_h

#import "OverlayView.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>

@interface OverlayCircle : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic) double radius;
@property (nonatomic) CLLocationCoordinate2D centerCoordinate;
@property (nonatomic, strong) BMKCircle <BMKOverlay> *overlay;

@end
#endif /* OverlayCircle_h */
