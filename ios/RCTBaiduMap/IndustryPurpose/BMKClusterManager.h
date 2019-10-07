//
//  BMKClusterManager.h
//  BMKObjectiveCDemo
//
//  Created by Baidu RD on 2018/3/6.
//  Copyright © 2015年 Baidu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BMKClusterQuadtree.h"

@interface BMKClusterManager : NSObject
@property (nonatomic, strong) NSMutableArray *clusterCaches;
- (void)clearClusterItems;
- (NSArray*)getClusters:(CGFloat)zoomLevel;
- (void)addClusterItem:(CLLocationCoordinate2D) coordinate;
@end
