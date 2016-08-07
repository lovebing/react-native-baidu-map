//
//  BaiduMapModule.m
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//


#import "BaiduMapModule.h"

@implementation BaiduMapModule

@synthesize bridge = _bridge;

static BMKGeoCodeSearch *geoCodeSearch;

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setMarker:(double)lat lng:(double)lng) {
    
}

RCT_EXPORT_METHOD(setMapType:(int)type) {
    [[self getBaiduMapView] setMapType:type];
}

RCT_EXPORT_METHOD(setZoom:(float)zoom) {
    [[self getBaiduMapView] setZoom:zoom];
}

RCT_EXPORT_METHOD(moveToCenter:(double)lat lng:(double)lng zoom:(double)zoom) {
    NSDictionary* center = @{
                             @"lat": @(lat),
                             @"lng": @(lng)
                             };
    [[self getBaiduMapView] setCenterLatLng:center];
    [[self getBaiduMapView] setZoom:zoom];
}

RCT_EXPORT_METHOD(reverseGeoCode:(double)lat lng:(double)lng) {
    
    [self getGeocodesearch].delegate = self;
    CLLocationCoordinate2D baiduCoor = CLLocationCoordinate2DMake(lat, lng);
    
    CLLocationCoordinate2D pt = (CLLocationCoordinate2D){baiduCoor.latitude, baiduCoor.longitude};
    
    BMKReverseGeoCodeOption *reverseGeoCodeSearchOption = [[BMKReverseGeoCodeOption alloc]init];
    reverseGeoCodeSearchOption.reverseGeoPoint = pt;
    
    BOOL flag = [[self getGeocodesearch] reverseGeoCode:reverseGeoCodeSearchOption];
    
    if(flag) {
        NSLog(@"逆向地理编码发送成功");
    }
    //[reverseGeoCodeSearchOption release];
}

RCT_EXPORT_METHOD(reverseGeoCodeGPS:(double)lat lng:(double)lng) {
    
    [self getGeocodesearch].delegate = self;
    CLLocationCoordinate2D baiduCoor = [self getBaiduCoor:lat lng:lng];
    
    CLLocationCoordinate2D pt = (CLLocationCoordinate2D){baiduCoor.latitude, baiduCoor.longitude};
    
    BMKReverseGeoCodeOption *reverseGeoCodeSearchOption = [[BMKReverseGeoCodeOption alloc]init];
    reverseGeoCodeSearchOption.reverseGeoPoint = pt;
    
    BOOL flag = [[self getGeocodesearch] reverseGeoCode:reverseGeoCodeSearchOption];
    
    if(flag) {
        NSLog(@"逆向地理编码发送成功");
    }
    //[reverseGeoCodeSearchOption release];
}

-(BMKGeoCodeSearch *)getGeocodesearch{
    if(geoCodeSearch == nil) {
        geoCodeSearch = [[BMKGeoCodeSearch alloc]init];
    }
    return geoCodeSearch;
}

- (void)onGetGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKGeoCodeResult *)result errorCode:(BMKSearchErrorCode)error{
    NSMutableDictionary *body = [self getEmptyBody];
    
    if (error == BMK_SEARCH_NO_ERROR) {
        NSString *latitude = [NSString stringWithFormat:@"%f", result.location.latitude];
        NSString *longitude = [NSString stringWithFormat:@"%f", result.location.longitude];
        body[@"latitude"] = latitude;
        body[@"longitude"] = longitude;
        body[@"errcode"] = @"0";
    }
    else {
        body[@"errcode"] = @"-1";
    }
    [self sendEvent:@"onGetGeoCodeResult" body:body];
    
}
-(void) onGetReverseGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKReverseGeoCodeResult *)result
                        errorCode:(BMKSearchErrorCode)error {
    
    NSMutableDictionary *body = [self getEmptyBody];
    
    if (error == BMK_SEARCH_NO_ERROR) {
        body[@"address"] = result.address;
        body[@"errcode"] = @"0";
    }
    else {
        body[@"errcode"] = @"-1";
    }
    [self sendEvent:@"onGetReverseGeoCodeResult" body:body];
    
    geoCodeSearch.delegate = nil;
}

-(NSMutableDictionary *)getEmptyBody {
    NSMutableDictionary *body = @{}.mutableCopy;
    return body;
}

-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body {
    [self.bridge.eventDispatcher sendDeviceEventWithName:name body:body];
}

-(CLLocationCoordinate2D)getBaiduCoor:(double)lat lng:(double)lng {
    CLLocationCoordinate2D coor = CLLocationCoordinate2DMake(lat, lng);
    NSDictionary* testdic = BMKConvertBaiduCoorFrom(coor,BMK_COORDTYPE_COMMON);
    testdic = BMKConvertBaiduCoorFrom(coor,BMK_COORDTYPE_GPS);
    CLLocationCoordinate2D baiduCoor = BMKCoorDictionaryDecode(testdic);
    
    return baiduCoor;
}

-(RCTBaiduMapView *) getBaiduMapView {
    return [RCTBaiduMapViewManager getBaiduMapView];
}
@end