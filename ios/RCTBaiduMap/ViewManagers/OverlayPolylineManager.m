//
//  OverlayPolylineManager.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayPolylineManager.h"

@implementation OverlayPolylineManager

RCT_EXPORT_MODULE(BaiduMapOverlayPolyline)

RCT_EXPORT_VIEW_PROPERTY(points, NSArray*)
RCT_EXPORT_VIEW_PROPERTY(lineWidth, double)
RCT_EXPORT_VIEW_PROPERTY(strokeColor, NSString*)


- (UIView *)view {
    OverlayPolyline *polyline = [OverlayPolyline new];
    polyline.bridge = self.bridge;
    return polyline;
}

@end


