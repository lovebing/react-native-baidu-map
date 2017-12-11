//
//  RCTBaiduMap.h
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#ifndef RCTBaiduMapView_h
#define RCTBaiduMapView_h


#import <React/RCTViewManager.h>
#import <React/RCTConvert+CoreLocation.h>
#import <BaiduMapAPI_Map/BMKMapView.h>
#import <BaiduMapAPI_Map/BMKMapComponent.h>
#import <BaiduMapAPI_Map/BMKPinAnnotationView.h>
#import <BaiduMapAPI_Map/BMKPointAnnotation.h>
#import <UIKit/UIKit.h>
#import <BaiduMapAPI_Location/BMKLocationComponent.h>

@interface RCTBaiduMapView : BMKMapView <BMKMapViewDelegate,BMKLocationServiceDelegate>

@property (nonatomic, copy) RCTBubblingEventBlock onChange;

+ (RCTBaiduMapView *)shareInstance;

-(void)setZoom:(float)zoom;
-(void)setCenterLatLng:(NSDictionary *)LatLngObj;
-(void)setMarker:(NSDictionary *)Options;


- (void)setStartLocation:(BOOL)startLocation;

@end

#endif
