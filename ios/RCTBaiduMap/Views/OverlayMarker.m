//
//  OverlayMarker.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayMarker.h"

@implementation OverlayMarker {
    RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
    UIImageView *_imageView;
}

- (NSString *)getType {
    return @"marker";
}

- (void)addToMap:(BMKMapView *)mapView {
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
    if (_icon != nil) {
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
                                                                   }
                                                                   dispatch_async(dispatch_get_main_queue(), ^{
                                                                     [self updateAnnotationView:annotation image:image];
                                                                       NSLog(@"download image success: %@", image);
                                                                   });
                                                               }];
    }
}

- (void)updateAnnotationView:(BMKPointAnnotationPro *) annotation image:(UIImage *)image {
  annotation.annotationView.image = image;
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
}

@end
