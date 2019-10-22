//  GetDistanceModule.m
//  RCTBaiduMap
//
//  Created by sybil052 on 2017/5/5.
//  Riant 略有调整 2019/10/22
//  https://www.jianshu.com/p/bd8335c40aa3

#import "GetDistanceModule.h"
#import <UIKit/UIKit.h>
#import <BaiduMapAPI_Utils/BMKGeometry.h>
#import "RCTEventDispatcher.h"

@implementation GetDistanceModule

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE(BaiduGetDistanceModule);

RCT_EXPORT_METHOD(getLocationDistance:(double)lat1 lng1:(double)lng1 lat2:(double)lat2 lng2:(double)lng2
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject){

    BMKMapPoint point1 = BMKMapPointForCoordinate(CLLocationCoordinate2DMake(lat1,lng1));
    BMKMapPoint point2 = BMKMapPointForCoordinate(CLLocationCoordinate2DMake(lat2,lng2));
    CLLocationDistance distance = BMKMetersBetweenMapPoints(point1,point2);
    NSDictionary* coor = @{
                            @"distance": [NSNumber numberWithDouble:distance]
                          };
    resolve(coor);
}

@end
