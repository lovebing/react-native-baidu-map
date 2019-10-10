//
//  OverlayPolyline.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayPolyline.h"
#import "OverlayUtils.h"

@implementation OverlayPolyline

- (NSString *)getType {
    return @"polyline";
}

- (void)addToMap:(BMKMapView *)mapView {
    NSLog(@"addToMap: %@", _points);
    [mapView addOverlay:[self getOverlay]];
}

- (void)update {
    [self updateOverlay:[self getOverlay]];
}

- (void)removeFromMap:(BMKMapView *)mapView {
    [mapView removeOverlay:[self getOverlay]];
}

- (BMKPolyline <BMKOverlay> *)getOverlay {
    if (_overlay == nil) {
        _overlay = [BMKPolyline new];
        [self updateOverlay:_overlay];
    }
    return _overlay;
}

- (void)updateOverlay:(BMKPolyline <BMKOverlay> *)overlay {
    [overlay setPolylineWithCoordinates:[OverlayUtils getCoords:_points] count:[_points count]];
}

@end
