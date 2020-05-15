//
//  BMKPointAnnotationPro.m
//
//  Created by lovebing on 2020/5/15.
//  Copyright © 2020 lovebing.net. All rights reserved.
//


#import "BMKPointAnnotationPro.h"

@implementation BMKPointAnnotationPro

static NSString *markerIdentifier = @"markerIdentifier";

- (instancetype)init {
    self = [super init];
    _annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:self reuseIdentifier:markerIdentifier];
    _annotationView.pinColor = BMKPinAnnotationColorPurple;
    _annotationView.animatesDrop = YES;
    return self;
}

@end
