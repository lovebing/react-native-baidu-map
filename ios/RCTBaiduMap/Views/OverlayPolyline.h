//
//  OverlayPolyline.h
//  Pods
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#ifndef OverlayPolyline_h
#define OverlayPolyline_h

#import "OverlayView.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>

@interface OverlayPolyline : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSArray *points;
@property (nonatomic, strong) BMKPolyline <BMKOverlay> *overlay;

@end
#endif /* OverlayPolyline_h */
