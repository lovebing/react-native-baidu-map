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

- (void)addToMap:(BMKMapView *)mapView;
- (void)update;
- (void)removeFromMap:(BMKMapView *)mapView;

@end

#endif /* OverlayView_h */
