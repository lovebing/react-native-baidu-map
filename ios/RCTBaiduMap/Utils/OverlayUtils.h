//
//  OverlayUtils.h
//
//  Created by lovebing on 2019/10/6.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#ifndef OverlayUtils_h
#define OverlayUtils_h

#import <React/RCTConvert+CoreLocation.h>
#import <BaiduMapAPI_Map/BMKPinAnnotationView.h>
#import <BaiduMapAPI_Map/BMKPointAnnotation.h>
#import <BaiduMapAPI_Map/BMKOverlay.h>
#import <BaiduMapAPI_Map/BMKArcline.h>
#import <BaiduMapAPI_Map/BMKArclineView.h>
#import <BaiduMapAPI_Map/BMKCircle.h>
#import <BaiduMapAPI_Map/BMKCircleView.h>
#import <BaiduMapAPI_Map/BMKPolygon.h>
#import <BaiduMapAPI_Map/BMKPolygonView.h>
#import <BaiduMapAPI_Map/BMKPolyline.h>
#import <BaiduMapAPI_Map/BMKPolylineView.h>
#import <BaiduMapAPI_Map/BMKMapView.h>
#import "OverlayView.h"

@interface OverlayUtils:NSObject

+ (CLLocationCoordinate2D)getCoorFromOption:(NSDictionary *)option;
+ (void)updateCoords:(NSArray *)points result:(CLLocationCoordinate2D *)result;
+ (UIColor *)getColor:(NSString *)colorHexStr;

@end

#endif /* OverlayUtils_h */
