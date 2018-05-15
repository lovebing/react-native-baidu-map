//
//  RCTBaiduMapViewManager.m
//  RCTBaiduMap
//
//  Created by lovebing on Aug 6, 2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "RCTBaiduMapViewManager.h"
#import <BaiduMapAPI_Location/BMKLocationComponent.h>

@interface RCTBaiduMapViewManager() <BMKLocationServiceDelegate>{
  @private
  RCTBaiduMapView *_innerMapView;
}

@property(strong, nonatomic) BMKLocationService *locationService;
@property(strong, nonatomic) BMKMapView *mapView;

@end

@implementation RCTBaiduMapViewManager

RCT_EXPORT_MODULE(RCTBaiduMapView)

RCT_EXPORT_VIEW_PROPERTY(mapType, int)
RCT_EXPORT_VIEW_PROPERTY(zoom, float)
RCT_EXPORT_VIEW_PROPERTY(trafficEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(baiduHeatMapEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(marker, NSDictionary*)
RCT_EXPORT_VIEW_PROPERTY(markers, NSArray*)
RCT_EXPORT_VIEW_PROPERTY(allGesturesEnabled, BOOL)

RCT_EXPORT_VIEW_PROPERTY(onChange, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(center, CLLocationCoordinate2D, RCTBaiduMapView) {
  [_innerMapView setCenterCoordinate:json ? [RCTConvert CLLocationCoordinate2D:json] : defaultView.centerCoordinate];
}

RCT_CUSTOM_VIEW_PROPERTY(draggable, BOOL, RCTBaiduMapView) {
  NSLog(@"draggable value %@", json);
  view.scrollEnabled = json ? [RCTConvert BOOL:json] : NO;
}

RCT_EXPORT_METHOD(startLocate:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){
  @try{
    if(!_locationService) {
      _locationService = [[BMKLocationService alloc] init];
      _locationService.delegate = self;
    }
    
    [_locationService startUserLocationService];
    
    self.mapView.showsUserLocation = NO;
    self.mapView.userTrackingMode = BMKUserTrackingModeNone;
    self.mapView.showsUserLocation = YES;
    
    resolve(nil);
  } @catch(NSException *ex) {
    reject(ex.name, ex.reason, nil);
  }
}

+ (void)initSDK:(NSString*)key {
  BMKMapManager* _mapManager = [[BMKMapManager alloc]init];
  BOOL ret = [_mapManager start:key  generalDelegate:nil];
  if (!ret) {
    NSLog(@"===>manager start failed!");
  }
}

- (UIView *)view {//0x7fd59b129000 0x7fd59b129000
  _innerMapView = [[RCTBaiduMapView alloc] init];
  _innerMapView.delegate = self;
  
  // Location service
//  if(!_locationService) {
//    _locationService = [[BMKLocationService alloc] init];
//    _locationService.delegate = self;
//  }
  
  return _innerMapView;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

- (BMKMapView *)mapView {
  return _innerMapView;
}

- (void)mapview:(RCTBaiduMapView *)mapView
  onDoubleClick:(CLLocationCoordinate2D)coordinate {
  NSLog(@"onDoubleClick");
  NSDictionary* event = @{
                          @"type": @"onMapDoubleClick",
                          @"params": @{
                              @"latitude": @(coordinate.latitude),
                              @"longitude": @(coordinate.longitude)
                              }
                          };
  [self sendEvent:mapView params:event];
}

#pragma mark - BMKMapView delegate

- (void)mapView:(RCTBaiduMapView *)mapView
onClickedMapBlank:(CLLocationCoordinate2D)coordinate {
  NSLog(@"onClickedMapBlank");
  NSDictionary* event = @{
                          @"type": @"onMapClick",
                          @"params": @{
                              @"latitude": @(coordinate.latitude),
                              @"longitude": @(coordinate.longitude)
                              }
                          };
  [self sendEvent:mapView params:event];
}

- (void)mapViewDidFinishLoading:(RCTBaiduMapView *)mapView {
  NSDictionary* event = @{
                          @"type": @"onMapLoaded",
                          @"params": @{}
                          };
  [self sendEvent:mapView params:event];
}

- (void)mapView:(RCTBaiduMapView *)mapView
didSelectAnnotationView:(BMKAnnotationView *)view {
  NSDictionary* event = @{
                          @"type": @"onMarkerClick",
                          @"params": @{
                              @"title": [[view annotation] title],
                              @"position": @{
                                  @"latitude": @([[view annotation] coordinate].latitude),
                                  @"longitude": @([[view annotation] coordinate].longitude)
                                  }
                              }
                          };
  [self sendEvent:mapView params:event];
}

- (void) mapView:(RCTBaiduMapView *)mapView
 onClickedMapPoi:(BMKMapPoi *)mapPoi {
  NSLog(@"onClickedMapPoi");
  NSDictionary* event = @{
                          @"type": @"onMapPoiClick",
                          @"params": @{
                              @"name": mapPoi.text,
                              @"uid": mapPoi.uid,
                              @"latitude": @(mapPoi.pt.latitude),
                              @"longitude": @(mapPoi.pt.longitude)
                              }
                          };
  [self sendEvent:mapView params:event];
}

- (BMKAnnotationView *)mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>)annotation {
  if ([annotation isKindOfClass:[BMKPointAnnotation class]]) {
    BMKPinAnnotationView *newAnnotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"myAnnotation"];
    newAnnotationView.pinColor = BMKPinAnnotationColorPurple;
    newAnnotationView.animatesDrop = YES;
    return newAnnotationView;
  }
  return nil;
}

- (void)mapStatusDidChanged: (RCTBaiduMapView *)mapView   {
  NSLog(@"mapStatusDidChanged");
  CLLocationCoordinate2D targetGeoPt = [mapView getMapStatus].targetGeoPt;
  NSDictionary* event = @{
                          @"type": @"onMapStatusChange",
                          @"params": @{
                              @"target": @{
                                  @"latitude": @(targetGeoPt.latitude),
                                  @"longitude": @(targetGeoPt.longitude)
                                  },
                              @"zoom": @"",
                              @"overlook": @""
                              }
                          };
  [self sendEvent:mapView params:event];
}

- (void)sendEvent:(RCTBaiduMapView *) mapView params:(NSDictionary *) params {
  if (!mapView.onChange) {
    return;
  }
  mapView.onChange(params);
}

#pragma mark - BMKLocationServiceDelegate

- (void)willStartLocatingUser {
  NSLog(@"Location service will start locating user");
}

- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
{
  [self.mapView updateLocationData:userLocation];
  NSLog(@"heading is %@",userLocation.heading);
}

- (void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation {
      NSLog(@"didUpdateUserLocation lat %f, long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
  NSLog(@"is main thread %@", [NSThread isMainThread] ? @"YES" : @"NO");
  [self.mapView updateLocationData:userLocation];
  [self.mapView setCenterCoordinate:userLocation.location.coordinate];
  
  if(_locationService) {
    _locationService.delegate = nil;
    _locationService = nil;
  }
}

- (void)didStopLocatingUser {
  NSLog(@"stop locate");
}

- (void)didFailToLocateUserWithError:(NSError *)error {
  NSLog(@"location error");
  if(_locationService) {
    _locationService.delegate = nil;
    _locationService = nil;
  }
}

@end

