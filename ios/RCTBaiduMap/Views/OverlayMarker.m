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
#import "OverlayMarkerIcon.h"

static NSMutableDictionary *ICON_MAGE_MAP;

@implementation OverlayMarker {
    RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
    UIImageView *_imageView;
    BMKMapView *_mapView;
    UIImage *_iconImage;
}

+ (void)initialize {
    ICON_MAGE_MAP = @{}.mutableCopy;
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
    annotation.coordinate = [OverlayUtils getCoorFromOption:_location];
    if ([@"drop" isEqualToString:self.animateType]) {
        annotation.annotationView.animatesDrop = YES;
    } else {
        annotation.annotationView.animatesDrop = NO;
    }
    if (_iconImage == nil && _icon != nil) {
        UIImage *image = [ICON_MAGE_MAP objectForKey:_icon.request.URL.absoluteString];
        if (image != nil) {
            dispatch_async(dispatch_get_main_queue(), ^{
               [self updateAnnotationView:annotation image:image];
            });
            return;
        }
        if (_reloadImageCancellationBlock) {
            _reloadImageCancellationBlock();
            _reloadImageCancellationBlock = nil;
        }
        NSLog(@"download %@", _icon.request.URL);
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
                                                                   ICON_MAGE_MAP[self.icon.request.URL.absoluteString] = image;
                                                                   dispatch_async(dispatch_get_main_queue(), ^{
                                                                      [self updateAnnotationView:annotation image:image];
                                                                       NSLog(@"download image success: %@", self.icon.request.URL.absoluteString);
                                                                   });
                                                               }];
    } else {
        annotation.annotationView.pinColor = [self getPinAnnotationColor];
    }
}

- (void)updateAnnotationView:(BMKPointAnnotationPro *) annotation image:(UIImage *)image {
    _iconImage = image;
    NSLog(@"annotationView width: %f, height: %f", _icon.size.width, _icon.size.height);
    if (_icon.size.height > 0 && _icon.size.width > 0) {
        annotation.annotationView.image = nil;
        CGRect frame = CGRectMake(0, 0, _icon.size.width, _icon.size.height);
        if (_imageView == nil) {
            _imageView = [[UIImageView alloc] initWithImage:image];
            [annotation.annotationView addSubview:_imageView];
        }
        _imageView.frame = frame;
        annotation.annotationView.frame = frame;
    } else {
        annotation.annotationView.image = image;
        annotation.annotationView.frame = CGRectMake(0, 0, CGImageGetWidth(image.CGImage), CGImageGetHeight(image.CGImage));
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
    } else if([subview isKindOfClass:[OverlayMarkerIcon class]]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        _annotation.annotationView.image = nil;
        _annotation.annotationView.frame = subview.frame;
        [_annotation.annotationView addSubview:subview];
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
    } else if([subview isKindOfClass:[OverlayMarkerIcon class]]) {
      [_annotation.annotationView removeReactSubview:subview];
    }
    [super removeReactSubview:subview];
}

- (void)didUpdateReactSubviews {
    NSLog(@"didUpdateReactSubviews:%lu", (unsigned long)[self.reactSubviews count]);
    [super didUpdateReactSubviews];
}

@end
