//
//  OverlayView.h
//  Pods
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#ifndef OverlayView_h
#define OverlayView_h

#import <Foundation/Foundation.h>
#import <BaiduMapAPI_Map/BMKMapView.h>
#import <BaiduMapAPI_Map/BMKPointAnnotation.h>

@interface OverlayView : UIView

@property NSInteger atIndex;

@property(nonatomic) NSString *fillColor;
@property(nonatomic) NSString *strokeColor;
@property(nonatomic) double lineWidth;

- (void)addToMap:(BMKMapView *)mapView;
- (void)update;
- (void)removeFromMap:(BMKMapView *)mapView;
- (BOOL)ownOverlay:(id<BMKOverlay>)overlay;

@end

#endif /* OverlayView_h */
