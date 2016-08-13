//
//  BaiduMapModule.h
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#ifndef BaiduMapModule_h
#define BaiduMapModule_h

#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#import "RCTBridge.h"

#import <BaiduMapAPI_Base/BMKBaseComponent.h>
#import <BaiduMapAPI_Map/BMKMapComponent.h>
#import <BaiduMapAPI_Search/BMKSearchComponent.h>
#import <BaiduMapAPI_Utils/BMKUtilsComponent.h>
#import <BaiduMapAPI_Location/BMKLocationService.h>

#import "RCTBaiduMapViewManager.h"

@interface BaiduMapModule : NSObject <RCTBridgeModule, BMKMapViewDelegate, BMKGeoCodeSearchDelegate, BMKLocationServiceDelegate> {
    UINavigationController *navigationController;   
    BMKMapManager* _mapManager; 
}

-(BMKGeoCodeSearch *)getGeocodesearch;

-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body;

-(NSMutableDictionary *)getEmptyBody;

-(CLLocationCoordinate2D)getBaiduCoor:(double)lat lng:(double)lng;

-(RCTBaiduMapView *) getBaiduMapView;
-(BMKLocationService *) getLocationService;

-(void)onGetCurrentLocationPosition:(BMKUserLocation *)userLocation;
@end

#endif /* BaiduMapModule_h */
