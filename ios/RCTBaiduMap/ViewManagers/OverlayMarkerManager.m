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

- (UIView *)view {
    OverlayMarker *marker = [OverlayMarker new];
    marker.bridge = self.bridge;
    return marker;
}

@end
