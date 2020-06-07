//
//  OverlayHeatMap.h
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/23.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef OverlayHeatMap_h
#define OverlayHeatMap_h

#import "OverlayView.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>

@interface OverlayHeatMap : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSArray *points;
@property (nonatomic, strong) NSDictionary *gradient;
@property (nonatomic, strong) BMKHeatMap *heatMap;

@end

#endif /* OverlayHeatMap_h */
