//
//  RCTBaiduMap.m
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "BaiduMapView.h"

@implementation BaiduMapView {
    BMKMapView* _mapView;
    BMKPointAnnotation* _annotation;
    NSMutableArray* _annotations;
}

- (void)setZoom:(float)zoom {
    self.zoomLevel = zoom;
}

- (void)setZoomGesturesEnabled:(BOOL)zoomGesturesEnabled{
    NSLog(@"setZoomGesturesEnabled: %d", zoomGesturesEnabled);
    self.gesturesEnabled = zoomGesturesEnabled;
}

- (void)setScrollGesturesEnabled:(BOOL)scrollGesturesEnabled{
    NSLog(@"setScrollGesturesEnabled: %d", scrollGesturesEnabled);
    self.scrollEnabled = scrollGesturesEnabled;
}

- (void)setCenterLatLng:(NSDictionary *)LatLngObj {
    double lat = [RCTConvert double:LatLngObj[@"lat"]];
    double lng = [RCTConvert double:LatLngObj[@"lng"]];
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(lat, lng);
    self.centerCoordinate = point;
}

- (void)setLocationData:(NSDictionary *)locationData {
    NSLog(@"setLocationData");
    if (_userLocation == nil) {
        _userLocation = [[BMKUserLocation alloc] init];
    }
    CLLocationCoordinate2D coord = [OverlayUtils getCoorFromOption:locationData];
    CLLocation *location = [[CLLocation alloc] initWithLatitude:coord.latitude longitude:coord.longitude];
    _userLocation.location = location;
    [self updateLocationData:_userLocation];
}

- (void)insertReactSubview:(id <RCTComponent>)subview atIndex:(NSInteger)atIndex {
    NSLog(@"childrenCount:%d", _childrenCount);
    if ([subview isKindOfClass:[OverlayView class]]) {
        OverlayView *overlayView = (OverlayView *) subview;
        [overlayView addToMap:self];
        [super insertReactSubview:subview atIndex:atIndex];
    }
}

- (void)removeReactSubview:(id <RCTComponent>)subview {
    NSLog(@"removeReactSubview");
    if ([subview isKindOfClass:[OverlayView class]]) {
        OverlayView *overlayView = (OverlayView *) subview;
        [overlayView removeFromMap:self];
        NSLog(@"overlayView atIndex: %d", overlayView.atIndex);
    }
    [super removeReactSubview:subview];
}

- (void)didSetProps:(NSArray<NSString *> *) props {
    NSLog(@"didSetProps: %d", _childrenCount);
    [super didSetProps:props];
}

- (void)didUpdateReactSubviews {
    for (int i = 0; i < [self.reactSubviews count]; i++) {
        UIView * view = [self.reactSubviews objectAtIndex:i];
        if ([view isKindOfClass:[OverlayView class]]) {
            OverlayView *overlayView = (OverlayView *) view;
            [overlayView update];
        }
    }
    NSLog(@"didUpdateReactSubviews:%d", [self.reactSubviews count]);
}

- (OverlayView *)findOverlayView:(id<BMKOverlay>)overlay {
    for (int i = 0; i < [self.reactSubviews count]; i++) {
        UIView * view = [self.reactSubviews objectAtIndex:i];
        if ([view isKindOfClass:[OverlayView class]]) {
            OverlayView *overlayView = (OverlayView *) view;
            if ([overlayView ownOverlay:overlay]) {
                return overlayView;
            }
        }
    }
    return nil;
}

@end
