//
//  GoelocationModule.h
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#import <BaiduMapAPI_Location/BMKLocationService.h>

#import "BaseModule.h"
#import "RCTBaiduMapViewManager.h"

@interface GeolocationModule : BaseModule <BMKGeoCodeSearchDelegate> 

@end
