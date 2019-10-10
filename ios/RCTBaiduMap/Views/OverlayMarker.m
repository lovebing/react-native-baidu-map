//
//  OverlayMarker.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayMarker.h"

@implementation OverlayMarker

- (NSString *)getType {
    return @"marker";
}

- (void)addToMap:(BMKMapView *)mapView {
    [mapView addAnnotation:[self getAnnotation]];
}

- (void)update {
    [self updateAnnotation:[self getAnnotation]];
}

- (void)removeFromMap:(BMKMapView *)mapView {
    [mapView removeAnnotation:[self getAnnotation]];
}

- (BMKPointAnnotation *)getAnnotation {
    if (_annotation == nil) {
        _annotation = [[BMKPointAnnotation alloc] init];
        [self updateAnnotation:_annotation];
    }
    return _annotation;
}

- (void)updateAnnotation:(BMKPointAnnotation *)annotation {
    CLLocationCoordinate2D coor = [OverlayUtils getCoorFromOption:_location];
    if(_title.length == 0) {
        annotation.title = nil;
    } else {
        annotation.title = _title;
    }
    annotation.coordinate = coor;
}

@end
