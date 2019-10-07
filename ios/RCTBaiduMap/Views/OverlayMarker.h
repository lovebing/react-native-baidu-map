//
//  OverlayMarker.h
//  Pods
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#ifndef OverlayMarker_h
#define OverlayMarker_h

#import "OverlayView.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>
#import <React/RCTConvert+CoreLocation.h>

@interface OverlayMarker : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSDictionary *location;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) BMKPointAnnotation *annotation;

@end

#endif /* OverlayMarker_h */
