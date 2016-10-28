//
//  BaiduMapModule.m
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//


#import "BaiduMapModule.h"

@implementation BaiduMapModule {
    BMKPointAnnotation* _annotation;
}

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setMarker:(double)lat lng:(double)lng) {
    if(_annotation == nil) {
      _annotation = [[BMKPointAnnotation alloc]init];
    }
    else {
        [[self getBaiduMapView] removeAnnotation:_annotation];
    }

    CLLocationCoordinate2D coor;
    coor.latitude = lat;
    coor.longitude = lng;
    _annotation.coordinate = coor;
    [[self getBaiduMapView] addAnnotation:_annotation];
}

RCT_EXPORT_METHOD(setMapType:(int)type) {
    [[self getBaiduMapView] setMapType:type];
}

RCT_EXPORT_METHOD(setZoom:(float)zoom) {
    [[self getBaiduMapView] setZoomLevel:zoom];
}

RCT_EXPORT_METHOD(moveToCenter:(double)lat lng:(double)lng zoom:(float)zoom) {
    NSDictionary* center = @{
                             @"lat": @(lat),
                             @"lng": @(lng)
                             };
    [[self getBaiduMapView] setCenterLatLng:center];
    [[self getBaiduMapView] setZoomLevel:zoom];
}

-(RCTBaiduMapView *) getBaiduMapView {
    return [RCTBaiduMapViewManager getBaiduMapView];
}

@end
