//
//  InfoWindowManager.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "InfoWindowManager.h"

@implementation InfoWindowManager

RCT_EXPORT_MODULE(BaiduMapOverlayInfoWindow)

- (UIView *)view {
    InfoWindow *infoWindow = [InfoWindow new];
    infoWindow.bridge = self.bridge;
    return infoWindow;
}

@end
