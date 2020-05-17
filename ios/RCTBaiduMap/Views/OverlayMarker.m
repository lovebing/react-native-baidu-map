//
//  OverlayMarker.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayMarker.h"
#import <BaiduMapAPI_Map/BMKAnnotationView.h>
#import <BaiduMapAPI_Map/BMKActionPaopaoView.h>
#import "InfoWindow.h"

@implementation OverlayMarker {
    RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
    UIImageView *_imageView;
    BMKMapView *_mapView;
    UIImage *_iconImage;
}

- (NSString *)getType {
    return @"marker";
}

- (void)addToMap:(BMKMapView *)mapView {
    _mapView = mapView;
    [mapView addAnnotation:[self getAnnotation]];
}

- (void)update {
    [self updateAnnotation:[self getAnnotation]];
}

- (void)removeFromMap:(BMKMapView *)mapView {
    [mapView removeAnnotation:[self getAnnotation]];
}

- (BMKPointAnnotationPro *)getAnnotation {
    if (_annotation == nil) {
        _annotation = [[BMKPointAnnotationPro alloc] init];
        [self updateAnnotation:_annotation];
    }
    return _annotation;
}


- (void)updateAnnotation:(BMKPointAnnotationPro *)annotation {
    if(_title.length == 0) {
        annotation.title = nil;
    } else {
        annotation.title = _title;
    }
    _annotation.coordinate = [OverlayUtils getCoorFromOption:_location];
    if (_iconImage == nil && _icon != nil) {
        if (_reloadImageCancellationBlock) {
            _reloadImageCancellationBlock();
            _reloadImageCancellationBlock = nil;
        }
        _reloadImageCancellationBlock = [[_bridge moduleForName:@"ImageLoader"] loadImageWithURLRequest:_icon.request
                                                                      size:self.bounds.size
                                                                         scale:RCTScreenScale()
                                                                       clipped:YES
                                                                    resizeMode:RCTResizeModeCenter
                                                                 progressBlock:nil
                                                              partialLoadBlock:nil
                                                               completionBlock:^(NSError *error, UIImage *image) {
                                                                   if (error) {
                                                                       NSLog(@"download image error: %@", error);
                                                                      return;
                                                                   }
                                                                   dispatch_async(dispatch_get_main_queue(), ^{
                                                                      [self updateAnnotationView:annotation image:image];
                                                                       NSLog(@"download image success: %@", image);
                                                                   });
                                                               }];
    } else {
        annotation.annotationView.pinColor = [self getPinAnnotationColor];
    }
}

- (void)updateAnnotationView:(BMKPointAnnotationPro *) annotation image:(UIImage *)image {
    annotation.annotationView.image = image;
    _iconImage = image;
    NSLog(@"annotationView width: %f, height: %f", _icon.size.width, _icon.size.height);
    if (_icon.size.height > 0 && _icon.size.width > 0) {
        annotation.annotationView.image = NULL;
        CGRect frame = CGRectMake(-_icon.size.width / 2, -_icon.size.height / 2, _icon.size.width, _icon.size.height);
        if (_imageView == nil) {
            _imageView = [[UIImageView alloc] initWithImage:image];
            [annotation.annotationView addSubview:_imageView];
        }
        _imageView.frame = frame;
        
    } else {
        annotation.annotationView.image = image;
    }
    annotation.annotationView.pinColor = [self getPinAnnotationColor];
}

- (BMKPinAnnotationColor)getPinAnnotationColor {
    if ([_pinColor isEqualToString:@"red"]) {
        return BMKPinAnnotationColorRed;
    }
    if ([_pinColor isEqualToString:@"green"]) {
        return BMKPinAnnotationColorGreen;
    }
    if([_pinColor isEqualToString:@"purple"]) {
        return BMKPinAnnotationColorPurple;
    }
    return BMKPinAnnotationColorRed;
}

- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex {
    if ([subview isKindOfClass:[InfoWindow class]]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            _annotation.customPopView.frame = subview.bounds;
            [_annotation.customPopView addSubview:subview];
            [_annotation updatePaopaoView];
      });
    }
    [super insertReactSubview:subview atIndex:atIndex];
}

- (void)removeReactSubview:(UIView *)subview {
    NSLog(@"removeReactSubview");
    if ([subview isKindOfClass:[InfoWindow class]]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            _annotation.annotationView = nil;
        });
    }
    [super removeReactSubview:subview];
}

- (void)didSetProps:(NSArray<NSString *> *)props {
    NSLog(@"marker didSetProps:%@", props);
    [super didSetProps:props];
    [self update];
}

- (void)didUpdateReactSubviews {
    NSLog(@"didUpdateReactSubviews:%lu", (unsigned long)[self.reactSubviews count]);
    [super didUpdateReactSubviews];
}

@end
