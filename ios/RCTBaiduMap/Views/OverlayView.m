//
//  OverlayView.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayView.h"

@implementation OverlayView

- (void)addToMap:(BMKMapView *)mapView {
   
}

- (void)removeFromMap:(BMKMapView *)mapView {
    
}

- (BOOL)ownOverlay:(id<BMKOverlay>)overlay {
    return NO;
}

- (void)update {
  
}

- (void)didSetProps:(NSArray<NSString *> *)props {
    NSLog(@"overlay didSetProps:%@", props);
    [super didSetProps:props];
    [self update];
}

- (void)setStroke:(NSDictionary *)stroke {
    self.lineWidth = [RCTConvert double:stroke[@"width"]];
    self.strokeColor = [RCTConvert NSString:stroke[@"color"]];
    NSLog(@"setStroke: color=%@, width=%f", self.strokeColor, self.lineWidth);
}

@end
