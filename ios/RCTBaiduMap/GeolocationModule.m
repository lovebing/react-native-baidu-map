//
//  GoelocationModule.m
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#import "GeolocationModule.h"


@implementation GeolocationModule {
    BMKPointAnnotation* _annotation;
}

@synthesize bridge = _bridge;

static BMKGeoCodeSearch *geoCodeSearch;

RCT_EXPORT_MODULE(BaiduGeolocationModule);

RCT_EXPORT_METHOD(getBaiduCoorFromGPSCoor:(double)lat lng:(double)lng
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSLog(@"getBaiduCoorFromGPSCoor");
    CLLocationCoordinate2D baiduCoor = [self getBaiduCoor:lat lng:lng];
    
    NSDictionary* coor = @{
                           @"latitude": @(baiduCoor.latitude),
                           @"longitude": @(baiduCoor.longitude)
                           };
    
    resolve(coor);
}

RCT_EXPORT_METHOD(geocode:(NSString *)city addr:(NSString *)addr) {
    
    [self getGeocodesearch].delegate = self;
    
    BMKGeoCodeSearchOption *geoCodeSearchOption = [[BMKGeoCodeSearchOption alloc]init];
    
    geoCodeSearchOption.city= city;
    geoCodeSearchOption.address = addr;
    
    BOOL flag = [[self getGeocodesearch] geoCode:geoCodeSearchOption];
    
    if(flag) {
        NSLog(@"geo检索发送成功");
    } else{
        NSLog(@"geo检索发送失败");
    }
}

RCT_EXPORT_METHOD(reverseGeoCode:(double)lat lng:(double)lng) {
    
    [self getGeocodesearch].delegate = self;
    CLLocationCoordinate2D baiduCoor = CLLocationCoordinate2DMake(lat, lng);
    
    CLLocationCoordinate2D pt = (CLLocationCoordinate2D){baiduCoor.latitude, baiduCoor.longitude};
    
    BMKReverseGeoCodeSearchOption *reverseGeoCodeSearchOption = [[BMKReverseGeoCodeSearchOption alloc]init];
    reverseGeoCodeSearchOption.location = pt;
    
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
    
    BMKReverseGeoCodeSearchOption *reverseGeoCodeSearchOption = [[BMKReverseGeoCodeSearchOption alloc]init];
    reverseGeoCodeSearchOption.location = pt;
    
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

- (void)onGetGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKGeoCodeSearchResult *)result errorCode:(BMKSearchErrorCode)error {
    NSMutableDictionary *body = [self getEmptyBody];
    
    if (error == BMK_SEARCH_NO_ERROR) {
        NSString *latitude = [NSString stringWithFormat:@"%f", result.location.latitude];
        NSString *longitude = [NSString stringWithFormat:@"%f", result.location.longitude];
        body[@"latitude"] = latitude;
        body[@"longitude"] = longitude;
    }
    else {
        body[@"errcode"] = [NSString stringWithFormat:@"%d", error];
        body[@"errmsg"] = [self getSearchErrorInfo:error];
    }
    [self sendEvent:@"onGetGeoCodeResult" body:body];
    
}
- (void)onGetReverseGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKReverseGeoCodeSearchResult *)result errorCode:(BMKSearchErrorCode)error {
    
    NSMutableDictionary *body = [self getEmptyBody];
    
    if (error == BMK_SEARCH_NO_ERROR) {
        // 使用离线地图之前，需要先初始化百度地图
        [[BMKMapView alloc] initWithFrame:CGRectZero];
        // 离线地图api或去citycode
        BMKOfflineMap *offlineMap = [[BMKOfflineMap alloc] init];
        NSArray *cityCodeArr = [offlineMap searchCity:result.addressDetail.city];
        if (cityCodeArr.count) {
            BMKOLSearchRecord *searchRecord = cityCodeArr.firstObject;
            body[@"cityCode"] = @(searchRecord.cityID).stringValue;
            searchRecord = nil;
            
        }
        cityCodeArr = nil;
        offlineMap = nil;
        
        body[@"latitude"] = [NSString stringWithFormat:@"%f", result.location.latitude];
        body[@"longitude"] = [NSString stringWithFormat:@"%f", result.location.longitude];
        body[@"address"] = result.address;
        body[@"province"] = result.addressDetail.province;
        body[@"city"] = result.addressDetail.city;
        body[@"district"] = result.addressDetail.district;
        body[@"streetName"] = result.addressDetail.streetName;
        body[@"streetNumber"] = result.addressDetail.streetNumber;
    }
    else {
        body[@"errcode"] = [NSString stringWithFormat:@"%d", error];
        body[@"errmsg"] = [self getSearchErrorInfo:error];
    }
    [self sendEvent:@"onGetReverseGeoCodeResult" body:body];
    
    geoCodeSearch.delegate = nil;
}
-(NSString *)getSearchErrorInfo:(BMKSearchErrorCode)error {
    NSString *errormsg = @"未知";
    switch (error) {
        case BMK_SEARCH_AMBIGUOUS_KEYWORD:
            errormsg = @"检索词有岐义";
            break;
        case BMK_SEARCH_AMBIGUOUS_ROURE_ADDR:
            errormsg = @"检索地址有岐义";
            break;
        case BMK_SEARCH_NOT_SUPPORT_BUS:
            errormsg = @"该城市不支持公交搜索";
            break;
        case BMK_SEARCH_NOT_SUPPORT_BUS_2CITY:
            errormsg = @"不支持跨城市公交";
            break;
        case BMK_SEARCH_RESULT_NOT_FOUND:
            errormsg = @"没有找到检索结果";
            break;
        case BMK_SEARCH_ST_EN_TOO_NEAR:
            errormsg = @"起终点太近";
            break;
        case BMK_SEARCH_KEY_ERROR:
            errormsg = @"key错误";
            break;
        case BMK_SEARCH_NETWOKR_ERROR:
            errormsg = @"网络连接错误";
            break;
        case BMK_SEARCH_NETWOKR_TIMEOUT:
            errormsg = @"网络连接超时";
            break;
        case BMK_SEARCH_PERMISSION_UNFINISHED:
            errormsg = @"还未完成鉴权，请在鉴权通过后重试";
            break;
        case BMK_SEARCH_INDOOR_ID_ERROR:
            errormsg = @"室内图ID错误";
            break;
        case BMK_SEARCH_FLOOR_ERROR:
            errormsg = @"室内图检索楼层错误";
            break;
        default:
            break;
    }
    return errormsg;
}
    
-(CLLocationCoordinate2D)getBaiduCoor:(double)lat lng:(double)lng {
    CLLocationCoordinate2D coor = CLLocationCoordinate2DMake(lat, lng);
    BMKLocationCoordinateType srctype = BMKLocationCoordinateTypeWGS84;
    BMKLocationCoordinateType destype = BMKLocationCoordinateTypeBMK09MC;
    CLLocationCoordinate2D baiduCoor = [BMKLocationManager BMKLocationCoordinateConvert:coor SrcType:srctype DesType:destype];
    return baiduCoor;
}
    
-(NSMutableDictionary *)getEmptyBody {
    NSMutableDictionary *body = @{}.mutableCopy;
    return body;
}
    
-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body {
    [self.bridge.eventDispatcher sendDeviceEventWithName:name body:body];
}

@end
