//
//  OverlayHeatMapManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/23.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "OverlayHeatMapManager.h"

@implementation OverlayHeatMapManager

RCT_EXPORT_MODULE(BaiduMapOverlayHeatMap)

RCT_EXPORT_VIEW_PROPERTY(points, NSArray*)
RCT_EXPORT_VIEW_PROPERTY(gradient, NSDictionary*)

- (UIView *)view {
    OverlayHeatMap *heatMap = [OverlayHeatMap new];
    heatMap.bridge = self.bridge;
    return heatMap;
}

@end
