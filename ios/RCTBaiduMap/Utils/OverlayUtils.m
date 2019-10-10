//
//  OverlayUtils.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/6.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "OverlayUtils.h"
#import <BaiduMapAPI_Map/BMKOverlay.h>

@implementation OverlayUtils

+ (CLLocationCoordinate2D)getCoorFromOption:(NSDictionary *)option {
    double lat = [RCTConvert double:option[@"latitude"]];
    double lng = [RCTConvert double:option[@"longitude"]];
    CLLocationCoordinate2D coor;
    coor.latitude = lat;
    coor.longitude = lng;
    return coor;
}

+ (CLLocationCoordinate2D *)getCoords:(NSArray *)points {
    NSUInteger size = [points count];
    CLLocationCoordinate2D coords[size];
    memset(coords, 0, size);
    for (NSUInteger i = 0; i < size; i++) {
        CLLocationCoordinate2D coord = [self getCoorFromOption:points[i]];
        coords[i] = coord;
        NSLog(@"points:%f, %f", coord.latitude, coord.longitude);
    }
    return coords;
}

@end
