//
//  OverlayCircle.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "OverlayCircle.h"

@implementation OverlayCircle

- (NSString *)getType {
    return @"circle";
}

- (void)addToMap:(BMKMapView *)mapView {
    [self initOverlay];
    [self updateOverlay:_overlay];
    [mapView addOverlay:_overlay];
}

- (void)update {
    [self initOverlay];
    [self updateOverlay:_overlay];
}

- (void)removeFromMap:(BMKMapView *)mapView {
    [mapView removeOverlay:_overlay];
}

- (void)initOverlay {
    if (_overlay == nil) {
        _overlay = [[BMKCircle alloc] init];
    }
}

- (void)updateOverlay:(BMKCircle <BMKOverlay> *)overlay {
    NSLog(@"setCircleWithCenterCoordinate");
    [overlay setCircleWithCenterCoordinate:_centerCoordinate radius:_radius];
}

- (BOOL)ownOverlay:(id<BMKOverlay>)overlay {
    if ([overlay isKindOfClass:[BMKCircle class]]) {
        BMKCircle *source = (BMKCircle *) overlay;
        return source.radius == _overlay.radius
            && source.coordinate.latitude == _overlay.coordinate.latitude
            && source.coordinate.longitude == _overlay.coordinate.longitude;
    }
    return NO;
}

@end
