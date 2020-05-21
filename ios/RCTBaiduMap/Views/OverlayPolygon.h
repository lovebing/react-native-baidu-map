//
//  OverlayPolygon.h
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef OverlayPolygon_h
#define OverlayPolygon_h

#import "OverlayView.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>

@interface OverlayPolygon : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSArray *points;
@property (nonatomic, strong) BMKPolygon <BMKOverlay> *overlay;

@end

#endif /* OverlayPolygon_h */
