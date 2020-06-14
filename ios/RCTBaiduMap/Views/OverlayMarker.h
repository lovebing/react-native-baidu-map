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
#import <React/RCTImageSource.h>
#import <React/RCTImageLoader.h>
#import <BaiduMapAPI_Map/BMKAnnotationView.h>
#import <BaiduMapAPI_Map/BMKAnnotation.h>
#import <React/RCTComponent.h>
#import "BMKPointAnnotationPro.h"
#import <React/UIView+React.h>
#import "InfoWindow.h"

@interface OverlayMarker : OverlayView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSDictionary *location;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) RCTImageSource *icon;
@property (nonatomic, strong) BMKPointAnnotationPro *annotation;
@property (nonatomic, strong) NSString *animateType;
@property (nonatomic, strong) NSString *pinColor;

@property (nonatomic, strong) BMKActionPaopaoView *paopaoView;

@property (nonatomic, copy) RCTBubblingEventBlock onClick;

@end

#endif /* OverlayMarker_h */
