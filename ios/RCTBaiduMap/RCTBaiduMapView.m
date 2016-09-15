//
//  RCTBaiduMap.m
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "RCTBaiduMapView.h"

@implementation RCTBaiduMapView {
    BMKMapView* _mapView;
    BMKPointAnnotation* _annotation;
}

-(void)setZoom:(float)zoom {
    self.zoomLevel = zoom;
}

-(void)setCenterLatLng:(NSDictionary *)LatLngObj {
    double lat = [RCTConvert double:LatLngObj[@"lat"]];
    double lng = [RCTConvert double:LatLngObj[@"lng"]];
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(lat, lng);
    self.centerCoordinate = point;
}

-(void)setMarker:(NSDictionary *)Options {
    NSLog(@"setMarker");
    if(Options != nil) {
        double lat = [RCTConvert double:Options[@"latitude"]];
        double lng = [RCTConvert double:Options[@"longitude"]];
        
        if(_annotation == nil) {
            _annotation = [[BMKPointAnnotation alloc]init];
        }
        else {
            [self removeAnnotation:_annotation];
        }
        
        CLLocationCoordinate2D coor;
        coor.latitude = lat;
        coor.longitude = lng;
        _annotation.coordinate = coor;
        [self addAnnotation:_annotation];
    }
}

@end