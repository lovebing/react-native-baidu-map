//
//  OverlayPolygon.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "OverlayPolygon.h"

@implementation OverlayPolygon


- (NSString *)getType {
    return @"polygon";
}

- (void)addToMap:(BMKMapView *)mapView {
    [self initOverlay];
    [self updateOverlay:_overlay];
    NSLog(@"arc addToMap");
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
        _overlay = [[BMKPolygon alloc] init];
    }
}

- (void)updateOverlay:(BMKPolygon <BMKOverlay> *)overlay {
    CLLocationCoordinate2D coords[[_points count]];
    memset(coords, 0, [_points count]);
    [OverlayUtils updateCoords:_points result:coords];
    [overlay setPolygonWithCoordinates:coords count:[_points count]];
}

- (BOOL)ownOverlay:(id<BMKOverlay>)overlay {
    if ([overlay isKindOfClass:[BMKPolygon class]]) {
        BMKPolygon *source = (BMKPolygon *) overlay;
        return source.points == _overlay.points;
    }
    return NO;
}

@end
