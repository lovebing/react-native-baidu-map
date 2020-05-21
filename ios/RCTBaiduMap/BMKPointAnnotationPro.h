//
//  BMKPointAnnotationPro.h
//
//  Created by lovebing on 2020/5/15.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef BMKPointAnnotationPro_h
#define BMKPointAnnotationPro_h

#import <BaiduMapAPI_Map/BMKPinAnnotationView.h>
#import <BaiduMapAPI_Map/BMKPointAnnotation.h>
#import <React/UIView+React.h>
#import <BaiduMapAPI_Map/BMKAnnotationView.h>
#import <BaiduMapAPI_Map/BMKActionPaopaoView.h>
#import "InfoWindow.h"

@interface BMKPointAnnotationPro : BMKPointAnnotation

@property (nonatomic, strong) BMKPinAnnotationView *annotationView;
@property (nonatomic, strong) UIView *customPopView;

- (void)updatePaopaoView;

@end

#endif /* BMKPointAnnotationPro_h */
