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

- (void)setGestures:(BOOL)gestures{
    NSLog(@"setGesturesEnabled: %d", gestures);
    self.gesturesEnabled=gestures;
}
- (void)setScroll:(BOOL)scroll{
    NSLog(@"setScrollEnabled: %d", scroll);
    self.scrollEnabled=scroll;

}

- (void)setCenterLatLng:(NSDictionary *)LatLngObj {
    double lat = [RCTConvert double:LatLngObj[@"lat"]];
    double lng = [RCTConvert double:LatLngObj[@"lng"]];
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(lat, lng);
    self.centerCoordinate = point;
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

@end
