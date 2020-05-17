//
//  OverlayPolygonManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.org. All rights reserved.
//

#import "OverlayPolygonManager.h"

@implementation OverlayPolygonManager

RCT_EXPORT_MODULE(BaiduMapOverlayPolygon)

RCT_EXPORT_VIEW_PROPERTY(points, NSArray*)
RCT_EXPORT_VIEW_PROPERTY(stroke, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(fillColor, NSString*)

- (UIView *)view {
    OverlayPolygon *polygon = [OverlayPolygon new];
    polygon.bridge = self.bridge;
    return polygon;
}

@end
