//
//  OverlayClusterManager.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayClusterManager.h"

@implementation OverlayClusterManager

RCT_EXPORT_MODULE(BaiduMapOverlayCluster)

- (UIView *)view {
    OverlayCluster *cluster = [[OverlayCluster alloc] init];
    return cluster;
}

@end
