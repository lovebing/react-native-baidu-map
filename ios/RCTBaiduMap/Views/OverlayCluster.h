//
//  OverlayCluster.h
//  Pods
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#ifndef OverlayCluster_h
#define OverlayCluster_h

#import "OverlayView.h"
#import "OverlayMarker.h"
#import "OverlayUtils.h"
#import <React/RCTBridge.h>
#import <React/RCTComponent.h>
#import <React/RCTConvert+CoreLocation.h>
#import "BMKClusterManager.h"
#import "ClusterAnnotation.h"

@interface OverlayCluster : OverlayView

@property (nonatomic, strong) BMKClusterManager *clusterManager;
@property (nonatomic, strong) NSMutableArray *clusters;
@property (nonatomic, strong) BMKMapView *mapView;

@end

#endif /* OverlayCluster_h */
