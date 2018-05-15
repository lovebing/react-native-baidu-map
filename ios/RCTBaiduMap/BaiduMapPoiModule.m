//
//  BaiduMapPoiModule.m
//  RCTBaiduMap
//
//  Created by Uncle Charlie on 14/4/2018.
//  Copyright © 2018 lovebing.org. All rights reserved.
//
#import "BaiduMapPoiModule.h"
#import <BaiduMapAPI_Search/BMKSearchComponent.h>

static const int DEFAULT_PAGE_CAPACITY = 10;

@interface BaiduMapPoiModule() <BMKPoiSearchDelegate>

@property(nonatomic, strong) BMKPoiSearch *poiSearch;

@end

@implementation BaiduMapPoiModule

RCT_EXPORT_MODULE();

- (BMKPoiSearch *)getPoiSearch {
  if(!_poiSearch) {
    _poiSearch = [BMKPoiSearch new];
    _poiSearch.delegate = self;
    return self.poiSearch;
  }
  
  return self.poiSearch;
}

- (NSArray<NSString *> *)supportedEvents {
  return @[@"PoiSearch"];
}

RCT_EXPORT_METHOD(searchZone:(NSArray<NSNumber*>*)center pageCapicity:(int)capacity
                  promiseResolver:(RCTPromiseResolveBlock) resolve
                  promiseRejecter:(RCTPromiseRejectBlock)reject){
  self.poiSearch = [self getPoiSearch];
  BMKNearbySearchOption *nearbySearchOption = [BMKNearbySearchOption new];
  nearbySearchOption.pageIndex = 1;
  nearbySearchOption.pageCapacity = capacity <= 0 ? DEFAULT_PAGE_CAPACITY : capacity;
  // TODO: Check center array is valid
  nearbySearchOption.location = CLLocationCoordinate2D{center[0].doubleValue, center[1].doubleValue};
  
  BOOL flag = [self.poiSearch poiSearchNearBy:nearbySearchOption];
  
  resolve(@(flag));
}

#pragma mark - BMKPoiSearchDelegate

- (void)onGetPoiResult:(BMKPoiSearch *)searcher result:(BMKPoiResult *)poiResult errorCode:(BMKSearchErrorCode)errorCode {
  [self sendEvent:@"PoiSearch" body:@{@"result": poiResult}];
}

@end
