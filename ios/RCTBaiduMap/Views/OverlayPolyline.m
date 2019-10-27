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
        _overlay = [[BMKPolyline alloc] init];
    }
}

- (void)updateOverlay:(BMKPolyline <BMKOverlay> *)overlay {
    CLLocationCoordinate2D coords[[_points count]];
    memset(coords, 0, [_points count]);
    [OverlayUtils updateCoords:_points result:coords];
    [overlay setPolylineWithCoordinates:coords count:[_points count]];
}

- (BOOL)ownOverlay:(id<BMKOverlay>)overlay {
    if ([overlay isKindOfClass:[BMKPolyline class]]) {
        BMKPolyline *source = (BMKPolyline *) overlay;
        return source.points == _overlay.points;
    }
    return NO;
}

@end
