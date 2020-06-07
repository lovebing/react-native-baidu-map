//
//  OverlayMarkerIconManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/6/7.
//  Copyright © 2020 lovebing.org. All rights reserved.
//

#import "OverlayMarkerIconManager.h"

@implementation OverlayMarkerIconManager

RCT_EXPORT_MODULE(BaiduMapOverlayMarkerIcon)

- (UIView *)view {
    OverlayMarkerIcon *markerIcon = [OverlayMarkerIcon new];
    markerIcon.bridge = self.bridge;
    return markerIcon;
}

@end
