//
//  OverlayCluster.m
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#import "OverlayCluster.h"

@implementation OverlayCluster

- (instancetype)init {
    _clusterManager = [[BMKClusterManager alloc] init];
    _clusters = [[NSMutableArray alloc] init];
    self = [super init];
    return self;
}

- (void)addToMap:(BMKMapView *)mapView {
    _mapView = mapView;
}

- (void)update {
}

- (void)removeFromMap:(BMKMapView *)mapView {
}

- (void)insertReactSubview:(id <RCTComponent>)subview atIndex:(NSInteger)atIndex {
    if ([subview isKindOfClass:[OverlayMarker class]]) {
        OverlayMarker *marker = (OverlayMarker *) subview;
        [_clusters addObject:marker];
    }
}

- (void)removeReactSubview:(id <RCTComponent>)subview {
    NSLog(@"removeReactSubview");
    if ([subview isKindOfClass:[OverlayMarker class]]) {
        OverlayMarker *marker = (OverlayMarker *) subview;
        [_clusters removeObject:marker];
    }
}

- (void)didUpdateReactSubviews {
    [_clusterManager clearClusterItems];
    for (int i = 0; i < [_clusters count]; i++) {
        OverlayMarker *marker = (OverlayMarker *) [_clusters objectAtIndex:i];
        [_clusterManager addClusterItem:[OverlayUtils getCoorFromOption:marker.location]];
    }
    NSInteger clusterZoom = (NSInteger) _mapView.zoomLevel;
    @synchronized(_clusterManager.clusterCaches) {
        NSMutableArray *clusters = [_clusterManager.clusterCaches objectAtIndex:(clusterZoom - 3)];
        if (clusters.count > 0) {
            [_mapView removeAnnotations:_mapView.annotations];
            [_mapView addAnnotations:clusters];
        } else {
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
                __block NSArray *array = [self.clusterManager getClusters:clusterZoom];
                dispatch_async(dispatch_get_main_queue(), ^{
                    for (BMKCluster *item in array) {
                        ClusterAnnotation *annotation = [[ClusterAnnotation alloc] init];
                        annotation.coordinate = item.coordinate;
                        annotation.size = item.size;
                        annotation.title = [NSString stringWithFormat:@"我是%ld个", item.size];
                        [clusters addObject:annotation];
                    }
                    [_mapView addAnnotations:clusters];
                });
            });
        }
    }
}

@end

