//
//  OverlayHeatMap.m
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/23.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#import "OverlayHeatMap.h"

@implementation OverlayHeatMap

- (NSString *)getType {
    return @"heatMap";
}

- (void)addToMap:(BMKMapView *)mapView {
  if (_heatMap == nil) {
    _heatMap = [[BMKHeatMap alloc]init];
  }
  [self update];
  [mapView addHeatMap:_heatMap]  ;
}

- (void)update {
  NSMutableArray *data = [NSMutableArray array];
  for (NSDictionary *point in _points) {
    BMKHeatMapNode *heatMapNode = [[BMKHeatMapNode alloc]init];
    heatMapNode.pt = [OverlayUtils getCoorFromOption:point];
    [data addObject:heatMapNode];
  }
  _heatMap.mData = data;
}

- (void)removeFromMap:(BMKMapView *)mapView {
  [mapView removeHeatMap];
}
@end
