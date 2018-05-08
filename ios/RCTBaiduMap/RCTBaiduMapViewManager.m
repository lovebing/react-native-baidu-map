//
//  RCTBaiduMapViewManager.m
//  RCTBaiduMap
//
//  Created by lovebing on Aug 6, 2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "RCTBaiduMapViewManager.h"
#import <BaiduMapAPI_Location/BMKLocationComponent.h>

@interface RCTBaiduMapViewManager() <BMKLocationServiceDelegate>

@property(strong, nonatomic) BMKLocationService *locationService;

@end

@implementation RCTBaiduMapViewManager;

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
  [view setCenterCoordinate:json ? [RCTConvert CLLocationCoordinate2D:json] : defaultView.centerCoordinate];

  // TODO: display user's location
//  if(!_locationService) {
//    _locationService = [BMKLocationService new];
//    _locationService.delegate = self;
//  }
//  [_locationService startUserLocationService];
  
  view.showsUserLocation = NO;
  view.userTrackingMode = BMKUserTrackingModeNone;
  view.showsUserLocation = YES;
}

RCT_CUSTOM_VIEW_PROPERTY(draggable, BOOL, RCTBaiduMapView) {
  view.scrollEnabled = false;
}

+ (void)initSDK:(NSString*)key {
  BMKMapManager* _mapManager = [[BMKMapManager alloc]init];
  BOOL ret = [_mapManager start:key  generalDelegate:nil];
  if (!ret) {
    NSLog(@"===>manager start failed!");
  }
}

- (UIView *)view {
  RCTBaiduMapView* mapView = [[RCTBaiduMapView alloc] init];
  
//  BMKLocationViewDisplayParam *displayParam = [BMKLocationViewDisplayParam new];
//  displayParam.isRotateAngleValid = NO;
//  displayParam.isAccuracyCircleShow = NO;
//  displayParam.locationViewImgName = @"icon";
//  [mapView updateLocationViewWithParam:displayParam];
  
  mapView.delegate = self;
  mapView.showsUserLocation = NO;
  mapView.userTrackingMode = BMKUserTrackingModeNone;
  mapView.showsUserLocation = YES;
  
  return mapView;
}

- (void)mapview:(BMKMapView *)mapView
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

- (void)mapView:(BMKMapView *)mapView
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

- (void)mapViewDidFinishLoading:(BMKMapView *)mapView {
  NSDictionary* event = @{
                          @"type": @"onMapLoaded",
                          @"params": @{}
                          };
  [self sendEvent:mapView params:event];
}

- (void)mapView:(BMKMapView *)mapView
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

- (void) mapView:(BMKMapView *)mapView
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

- (void)mapStatusDidChanged: (BMKMapView *)mapView   {
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

- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
{
  [(RCTBaiduMapView *)self.view updateLocationData:userLocation];
  NSLog(@"heading is %@",userLocation.heading);
}

- (void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation {
      NSLog(@"didUpdateUserLocation lat %f, long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
  [(RCTBaiduMapView *)[self view] updateLocationData:userLocation];
  if(_locationService) {
    [_locationService stopUserLocationService];
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
    [_locationService stopUserLocationService];
    _locationService.delegate = nil;
    _locationService = nil;
  }
}

@end

