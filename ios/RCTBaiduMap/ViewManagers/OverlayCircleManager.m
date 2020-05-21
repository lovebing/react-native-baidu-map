//
//  OverlayCircleManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "OverlayCircleManager.h"

@implementation OverlayCircleManager

RCT_EXPORT_MODULE(BaiduMapOverlayCircle)

RCT_EXPORT_VIEW_PROPERTY(radius, double)
RCT_EXPORT_VIEW_PROPERTY(stroke, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(fillColor, NSString*)

RCT_CUSTOM_VIEW_PROPERTY(center, CLLocationCoordinate2D, OverlayCircle) {
    [view setCenterCoordinate:json ? [RCTConvert CLLocationCoordinate2D:json] : defaultView.centerCoordinate];
}

- (UIView *)view {
    OverlayCircle *circle = [OverlayCircle new];
    circle.bridge = self.bridge;
    return circle;
}

@end
