//
//  OverlayMarkerManager.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayMarkerManager.h"

@implementation OverlayMarkerManager

RCT_EXPORT_MODULE(BaiduMapOverlayMarker)
RCT_EXPORT_VIEW_PROPERTY(location, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(title, NSString*)
RCT_EXPORT_VIEW_PROPERTY(icon, RCTImageSource*)
RCT_EXPORT_VIEW_PROPERTY(animateType, NSString*)
RCT_EXPORT_VIEW_PROPERTY(pinColor, NSString*)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTBubblingEventBlock)

- (UIView *)view {
    OverlayMarker *marker = [OverlayMarker new];
    marker.bridge = self.bridge;
    return marker;
}

@end
