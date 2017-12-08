//
//  RouteModule.h
//  RCTBaiduMap
//
//  Created by Run on 2017/12/4.
//  Copyright © 2017年 lovebing.org. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseModule.h"
#import <React/RCTConvert+CoreLocation.h>
#import <BaiduMapAPI_Search/BMKSearchComponent.h>
#import "RCTBaiduMapView.h"

@interface RouteModule : BaseModule <BMKRouteSearchDelegate>

@end
