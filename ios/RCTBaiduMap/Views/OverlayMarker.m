//
//  OverlayMarker.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayMarker.h"
#import "BMKPointAnnotationPro.h"
#import <BaiduMapAPI_Map/BMKAnnotationView.h>
#import "RCBMImageAnnotView.h"

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

- (BMKPointAnnotationPro *)getAnnotation {
    if (_annotation == nil) {
        _annotation = [[BMKPointAnnotationPro alloc] init];
        [self updateAnnotation:_annotation];
    }
    return _annotation;
}

- (void)updateAnnotation:(BMKPointAnnotationPro *)annotation {
    CLLocationCoordinate2D coor = [OverlayUtils getCoorFromOption:_location];
    if(_title.length == 0) {
        annotation.title = nil;
    } else {
        annotation.title = _title;
    }
    annotation.coordinate = coor;

    annotation.getAnnotationView = ^BMKAnnotationView * _Nonnull(BMKPointAnnotation * _Nonnull annotation) {
        RCBMImageAnnotView *annotV = [[RCBMImageAnnotView alloc] initWithAnnotation:annotation reuseIdentifier:@"dontCare"];
        annotV.bridge = self.bridge;
        NSLog(@"self.icon:%@",self.icon);
        annotV.source = self.icon;
        return annotV;
    };
}

@end
