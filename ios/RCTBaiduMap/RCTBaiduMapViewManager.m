//
//  RCTBaiduMapViewManager.m
//  RCTBaiduMap
//
//  Created by lovebing on Aug 6, 2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "RCTBaiduMapViewManager.h"

static RCTBaiduMapView* _mapView;

@implementation RCTBaiduMapViewManager;

RCT_EXPORT_MODULE(RCTBaiduMapView)

RCT_EXPORT_VIEW_PROPERTY(mapType, int)
RCT_EXPORT_VIEW_PROPERTY(zoom, float)
RCT_EXPORT_VIEW_PROPERTY(trafficEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(baiduHeatMapEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(marker, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(markers, NSArray*)

RCT_EXPORT_VIEW_PROPERTY(onChange, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(center, CLLocationCoordinate2D, RCTBaiduMapView) {
    [view setCenterCoordinate:json ? [RCTConvert CLLocationCoordinate2D:json] : defaultView.centerCoordinate];
}


+(void)initSDK:(NSString*)key {
    
    BMKMapManager* _mapManager = [[BMKMapManager alloc]init];
    BOOL ret = [_mapManager start:key  generalDelegate:nil];
    if (!ret) {
        NSLog(@"manager start failed!");
    }
}

- (UIView *)view {
    NSLog(@"RCTBaiduMapView");
    if(_mapView != nil) {
        [_mapView removeFromSuperview];
    }
    _mapView = [[RCTBaiduMapView alloc] init];
    _mapView.delegate = self;
    return _mapView;
}

-(void)mapview:(RCTBaiduMapView *)mapView
 onDoubleClick:(CLLocationCoordinate2D)coordinate {
    NSLog(@"onDoubleClick");
    NSDictionary* event = @{
                            @"type": @"onMapDoubleClick",
                            @"params": @{
                                    @"latitude": @(coordinate.latitude),
                                    @"longitude": @(coordinate.longitude)
                                    }
                            };
    [self sendEvent:mapView event:event];
}

-(void)mapView:(RCTBaiduMapView *)mapView
onClickedMapBlank:(CLLocationCoordinate2D)coordinate {
    NSLog(@"onClickedMapBlank");
    NSDictionary* event = @{
                            @"type": @"onMapClick",
                            @"params": @{
                                    @"latitude": @(coordinate.latitude),
                                    @"longitude": @(coordinate.longitude)
                                    }
                            };
    [self sendEvent:mapView event:event];
}

-(void)mapViewDidFinishLoading:(RCTBaiduMapView *)mapView {
    NSDictionary* event = @{
                            @"type": @"onMapLoaded",
                            @"params": @{}
                            };
    [self sendEvent:mapView event:event];
}


- (void) mapView:(RCTBaiduMapView *)mapView
 onClickedMapPoi:(BMKMapPoi *)mapPoi {
    NSLog(@"onClickedMapPoi");
    NSDictionary* event = @{
                            @"type": @"onClickedMapPoi",
                            @"params": @{
                                    @"title": mapPoi.text,
                                    @"position": @{
                                            @"latitude": @(mapPoi.pt.latitude),
                                            @"longitude": @(mapPoi.pt.longitude)
                                            }
                                    }
                            };
    [self sendEvent:mapView event:event];
}

- (BMKAnnotationView *)mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>)annotation {
    if ([annotation isKindOfClass:[BMKPointAnnotation class]]) {
        BMKPinAnnotationView *newAnnotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"myAnnotation"];
        newAnnotationView.pinColor = BMKPinAnnotationColorPurple;
        newAnnotationView.animatesDrop = YES;
        return newAnnotationView;
    }
    return nil;
}

-(void)mapStatusDidChanged: (RCTBaiduMapView *)mapView	 {
    NSLog(@"mapStatusDidChanged");
    CLLocationCoordinate2D targetGeoPt = [mapView getMapStatus].targetGeoPt;
    NSDictionary* event = @{
                            @"type": @"onMapStatusChange",
                            @"params": @{
                                    @"target": @{
                                            @"latitude": @(targetGeoPt.latitude),
                                            @"longitude": @(targetGeoPt.longitude)
                                            },
                                    @"zoom": @"",
                                    @"overlook": @""
                                    }
                            };
    [self sendEvent:mapView event:event];
    
}

-(void)sendEvent:(RCTBaiduMapView *)mapView event:(NSDictionary*)event {
    if (!mapView.onChange) {
        return;
    }
    mapView.onChange(event);
}

+(RCTBaiduMapView *) getBaiduMapView {
    return _mapView;
}

@end
