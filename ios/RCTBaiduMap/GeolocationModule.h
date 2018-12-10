//
//  GoelocationModule.h
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#ifndef GeolocationModule_h
#define GeolocationModule_h


#import <BMKLocationkit/BMKLocationComponent.h>
#import <BaiduMapAPI_Search/BMKGeoCodeSearch.h>
#import <BaiduMapAPI_Map/BMKOfflineMap.h>
#import "BaseModule.h"
#import "BaiduMapViewManager.h"

@interface GeolocationModule : BaseModule <BMKGeoCodeSearchDelegate> {
}
    
-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body;
-(NSMutableDictionary *)getEmptyBody;
    
    @end

#endif /* GeolocationModule_h */
