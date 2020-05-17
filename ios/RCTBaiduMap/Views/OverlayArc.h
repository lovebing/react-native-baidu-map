//
//  OverlayArc.h
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef OverlayArc_h
#define OverlayArc_h

#import "OverlayView.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>

@interface OverlayArc : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSArray *points;
@property (nonatomic) BOOL dash;
@property (nonatomic, strong) BMKArcline <BMKOverlay> *overlay;

@end

#endif /* OverlayArc_h */
