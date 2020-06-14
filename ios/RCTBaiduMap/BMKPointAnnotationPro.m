//
//  BMKPointAnnotationPro.m
//
//  Created by lovebing on 2020/5/15.
//  Copyright © 2020 lovebing.net. All rights reserved.
//


#import "BMKPointAnnotationPro.h"

@implementation BMKPointAnnotationPro {
    BMKActionPaopaoView *_paopaoView;
}

static NSString *markerIdentifier = @"markerIdentifier";

- (instancetype)init {
    self = [super init];
    _annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:self reuseIdentifier:markerIdentifier];
    _customPopView = [[UIView alloc] init];
    _paopaoView = [[BMKActionPaopaoView alloc] initWithCustomView:_customPopView];
    return self;
}

- (void)updatePaopaoView {
    _paopaoView.frame = _customPopView.frame;
    _annotationView.paopaoView = _paopaoView;
}

@end
