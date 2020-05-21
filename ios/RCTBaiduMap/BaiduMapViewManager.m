//
//  RCTBaiduMapViewManager.m
//  RCTBaiduMap
//
//  Created by lovebing on Aug 6, 2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "BaiduMapViewManager.h"
#import "BMKPointAnnotationPro.h"

@implementation BaiduMapViewManager;

static NSString *markerIdentifier = @"markerIdentifier";
static NSString *clusterIdentifier = @"clusterIdentifier";

RCT_EXPORT_MODULE(BaiduMapView)

RCT_EXPORT_VIEW_PROPERTY(mapType, int)
RCT_EXPORT_VIEW_PROPERTY(zoom, float)
RCT_EXPORT_VIEW_PROPERTY(showsUserLocation, BOOL);
RCT_EXPORT_VIEW_PROPERTY(showMapPoi, BOOL);
RCT_EXPORT_VIEW_PROPERTY(scrollGesturesEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(zoomGesturesEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(trafficEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(baiduHeatMapEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(clusterEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(markers, NSArray*)
RCT_EXPORT_VIEW_PROPERTY(locationData, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(onChange, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(center, CLLocationCoordinate2D, BaiduMapView) {
    [view setCenterCoordinate:json ? [RCTConvert CLLocationCoordinate2D:json] : defaultView.centerCoordinate];
}

+ (void)initSDK:(NSString*)key {
    BMKMapManager* _mapManager = [[BMKMapManager alloc]init];
    [[BMKLocationAuth sharedInstance] checkPermisionWithKey:key authDelegate:nil];
    BOOL ret = [_mapManager start:key  generalDelegate:nil];
    if (!ret) {
        NSLog(@"manager start failed!");
    }
}

- (UIView *)view {
    BaiduMapView* mapView = [[BaiduMapView alloc] init];
    mapView.delegate = self;
    return mapView;
}

- (void)mapview:(BMKMapView *)mapView onDoubleClick:(CLLocationCoordinate2D)coordinate {
    NSLog(@"onDoubleClick");
    NSDictionary* event = @{
                            @"type": @"onMapDoubleClick",
                            @"params": @{
                                    @"latitude": @(coordinate.latitude),
                                    @"longitude": @(coordinate.longitude)
                                    }
                            };
    [self sendEvent:mapView params:event];
}

- (void)mapView:(BMKMapView *)mapView onClickedMapBlank:(CLLocationCoordinate2D)coordinate {
    NSLog(@"onClickedMapBlank");
    NSDictionary* event = @{
                            @"type": @"onMapClick",
                            @"params": @{
                                    @"latitude": @(coordinate.latitude),
                                    @"longitude": @(coordinate.longitude)
                                    }
                            };
    [self sendEvent:mapView params:event];
}

- (void)mapViewDidFinishLoading:(BMKMapView *)mapView {
    NSDictionary* event = @{
                            @"type": @"onMapLoaded",
                            @"params": @{}
                            };
    [self sendEvent:mapView params:event];
}

- (void)mapView:(BMKMapView *)mapView didSelectAnnotationView:(BMKAnnotationView *)view {
    NSString *title = [[view annotation] title];
    if (title == nil) {
        title = @"";
    }
    NSDictionary* event = @{
                            @"type": @"onMarkerClick",
                            @"params": @{
                                    @"title": title,
                                    @"position": @{
                                            @"latitude": @([[view annotation] coordinate].latitude),
                                            @"longitude": @([[view annotation] coordinate].longitude)
                                            }
                                    }
                            };
    if ([mapView isKindOfClass:[BaiduMapView class]]) {
        BaiduMapView *baiduMapView = (BaiduMapView *) mapView;
        OverlayMarker *overlayMaker = [baiduMapView findOverlayMaker:[view annotation]];
        if (overlayMaker != nil) {
            overlayMaker.onClick(event);
            NSLog(@"OverlayMarker found");
        } else {
            NSLog(@"OverlayMarker not found");
        }
    }
    [self sendEvent:mapView params:event];
}

- (void)mapView:(BMKMapView *)mapView onClickedMapPoi:(BMKMapPoi *)mapPoi {
    NSLog(@"onClickedMapPoi");
    NSDictionary* event = @{
                            @"type": @"onMapPoiClick",
                            @"params": @{
                                    @"name": mapPoi.text,
                                    @"uid": mapPoi.uid,
                                    @"latitude": @(mapPoi.pt.latitude),
                                    @"longitude": @(mapPoi.pt.longitude)
                                    }
                            };
    [self sendEvent:mapView params:event];
}

- (BMKAnnotationView *)mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>)annotation {
    NSLog(@"viewForAnnotation");
    if ([annotation isKindOfClass:[ClusterAnnotation class]]) {
        ClusterAnnotation *cluster = (ClusterAnnotation*)annotation;
        BMKPinAnnotationView *annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:clusterIdentifier];
        UILabel *annotationLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 22, 22)];
        annotationLabel.textColor = [UIColor whiteColor];
        annotationLabel.font = [UIFont systemFontOfSize:11];
        annotationLabel.textAlignment = NSTextAlignmentCenter;
        annotationLabel.hidden = NO;
        annotationLabel.text = [NSString stringWithFormat:@"%ld", cluster.size];
        annotationLabel.backgroundColor = [UIColor greenColor];
        annotationView.alpha = 0.8;
        [annotationView addSubview:annotationLabel];
        
        if (cluster.size == 1) {
            annotationLabel.hidden = YES;
            annotationView.pinColor = BMKPinAnnotationColorRed;
        }
        if (cluster.size > 20) {
            annotationLabel.backgroundColor = [UIColor redColor];
        } else if (cluster.size > 10) {
            annotationLabel.backgroundColor = [UIColor purpleColor];
        } else if (cluster.size > 5) {
            annotationLabel.backgroundColor = [UIColor blueColor];
        } else {
            annotationLabel.backgroundColor = [UIColor greenColor];
        }
        [annotationView setBounds:CGRectMake(0, 0, 22, 22)];
        annotationView.draggable = YES;
        annotationView.annotation = annotation;
        return annotationView;
    } else if ([annotation isKindOfClass:[BMKPointAnnotationPro class]]) {
        BMKPointAnnotationPro *annotationPro = (BMKPointAnnotationPro *) annotation;
        NSLog(@"BMKPointAnnotationPro");
        return annotationPro.annotationView;
    } else if ([annotation isKindOfClass:[BMKPointAnnotation class]]) {
        BMKPinAnnotationView *annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:markerIdentifier];
        annotationView.pinColor = BMKPinAnnotationColorPurple;
        annotationView.animatesDrop = YES;
        NSLog(@"BMKPointAnnotation");
        return annotationView;
    }
    return nil;
}

- (BMKOverlayView *)mapView:(BMKMapView *)mapView viewForOverlay:(id<BMKOverlay>)overlay {
    NSLog(@"viewForOverlay");
    BaiduMapView *baidumMapView = (BaiduMapView *) mapView;
    OverlayView *overlayView = [baidumMapView findOverlayView:overlay];
    if (overlayView == nil) {
        return nil;
    }
    NSLog(@"fillColor: %@", overlayView.fillColor);
    if ([overlay isKindOfClass:[BMKArcline class]]) {
        NSLog(@"BMKArcline");
        OverlayArc *arc = (OverlayArc *) overlayView;
        BMKArclineView *arclineView = [[BMKArclineView alloc] initWithArcline:overlay];
        arclineView.strokeColor = [OverlayUtils getColor:overlayView.strokeColor];
        arclineView.lineDash = arc.dash;
        arclineView.lineWidth = overlayView.lineWidth;
        return arclineView;
    } else if([overlay isKindOfClass:[BMKCircle class]]) {
        BMKCircleView *circleView = [[BMKCircleView alloc] initWithCircle:overlay];
        circleView.strokeColor = [OverlayUtils getColor:overlayView.strokeColor];
        circleView.lineWidth = overlayView.lineWidth;
        circleView.fillColor = [OverlayUtils getColor:overlayView.fillColor];
        return circleView;
    } else if([overlay isKindOfClass:[BMKPolyline class]]) {
        BMKPolylineView *polylineView = [[BMKPolylineView alloc] initWithPolyline:overlay];
        polylineView.strokeColor = [OverlayUtils getColor:overlayView.strokeColor];
        polylineView.lineWidth = overlayView.lineWidth;
        NSLog(@"BMKPolylineView: strokeColor: %@", polylineView.strokeColor);
        return polylineView;
    } else if([overlay isKindOfClass:[BMKPolygon class]]) {
        BMKPolygonView *polygonView = [[BMKPolygonView alloc] initWithPolygon:overlay];
        polygonView.strokeColor = [OverlayUtils getColor:overlayView.strokeColor];
        polygonView.lineWidth = overlayView.lineWidth;
        polygonView.fillColor = [OverlayUtils getColor:overlayView.fillColor];
        return polygonView;
    }
    return nil;
}

- (void)mapStatusDidChanged: (BMKMapView *)mapView {
    CLLocationCoordinate2D targetGeoPt = [mapView getMapStatus].targetGeoPt;
    BMKCoordinateRegion region = mapView.region;
    NSDictionary* event = @{
                            @"type": @"onMapStatusChange",
                            @"params": @{
                                    @"target": @{
                                            @"latitude": @(targetGeoPt.latitude),
                                            @"longitude": @(targetGeoPt.longitude)
                                            },
                                    @"latitudeDelta": @(region.span.latitudeDelta),
                                    @"longitudeDelta": @(region.span.longitudeDelta),
                                    @"zoom": @(mapView.zoomLevel),
                                    @"overlook": @""
                                    }
                            };
    [self sendEvent:mapView params:event];
}

- (void)sendEvent:(BaiduMapView *)mapView params:(NSDictionary *)params {
    if (!mapView.onChange) {
        return;
    }
    mapView.onChange(params);
}

@end
