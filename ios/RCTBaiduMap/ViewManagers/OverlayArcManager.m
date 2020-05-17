//
//  OverlayArcManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "OverlayArcManager.h"

@implementation OverlayArcManager

RCT_EXPORT_MODULE(BaiduMapOverlayArc)

RCT_EXPORT_VIEW_PROPERTY(points, NSArray*)
RCT_EXPORT_VIEW_PROPERTY(stroke, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(dash, BOOL)

- (UIView *)view {
    OverlayArc *arc = [OverlayArc new];
    arc.bridge = self.bridge;
    return arc;
}

@end
