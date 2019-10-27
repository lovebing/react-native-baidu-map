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
    return CLLocationCoordinate2DMake(lat, lng);
}

+ (void)updateCoords:(NSArray *)points result:(CLLocationCoordinate2D *)result {
    NSUInteger size = [points count];
    for (NSUInteger i = 0; i < size; i++) {
        CLLocationCoordinate2D coord = [self getCoorFromOption:points[i]];
        result[i] = coord;
    }
}

+ (UIColor *)getColor:(NSString *)colorHexStr {
    CGFloat alpha, red, blue, green;
    switch ([colorHexStr length]) {
        case 6:
            alpha = 1.0f;
            red = [self getColorValue:colorHexStr start:0 length:2];
            green = [self getColorValue:colorHexStr start:2 length:2];
            blue = [self getColorValue:colorHexStr start:4 length:2];
            break;
        case 8:
            alpha = [self getColorValue:colorHexStr start:0 length:2];
            red = [self getColorValue:colorHexStr start:2 length:2];
            green = [self getColorValue:colorHexStr start:4 length:2];
            blue = [self getColorValue:colorHexStr start:6 length:2];
            break;
        default:
            return nil;
    }
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

+ (CGFloat)getColorValue: (NSString *)hexStr start:(NSUInteger)start length:(NSUInteger)length {
    NSString *value = [hexStr substringWithRange: NSMakeRange(start, length)];
    unsigned hexValue;
    [[NSScanner scannerWithString: value] scanHexInt: &hexValue];
    return hexValue / 255.0;
}

@end
