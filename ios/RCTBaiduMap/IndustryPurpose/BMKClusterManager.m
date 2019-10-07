//
//  BMKClusterManager.m
//  BMKObjectiveCDemo
//
//  Created by Baidu RD on 2018/3/6.
//  Copyright © 2015年 Baidu. All rights reserved.
//

#import "BMKClusterManager.h"

#define MAX_DISTANCE_IN_DP    200

@interface BMKClusterManager ()
@property (nonatomic, readonly) NSMutableArray *quadItems;
@property (nonatomic, readonly) BMKClusterQuadtree *quadtree;
@end

@implementation BMKClusterManager

#pragma mark - Initialization method
- (id)init {
    self = [super init];
    if (self) {
        
        _clusterCaches = [[NSMutableArray alloc] init];
        for (NSInteger i = 3; i < 22; i++) {
            [_clusterCaches addObject:[NSMutableArray array]];
        }
        
        _quadtree = [[BMKClusterQuadtree alloc] initWithRect:CGRectMake(0, 0, 1, 1)];
        _quadItems = [[NSMutableArray alloc] init];
    }
    return self;
}

- (void)addClusterItem:(CLLocationCoordinate2D) coordinate {
    BMKQuadItem *quadItem = [[BMKQuadItem alloc] init];
    quadItem.coordinate = coordinate;
    @synchronized(_quadtree) {
        [_quadItems addObject:quadItem];
        [_quadtree addItem:quadItem];
    }
}

#pragma mark - Clusters
- (void)clearClusterItems {
    @synchronized(_quadtree) {
        [_quadItems removeAllObjects];
        [_quadtree clearItems];
    }
}

- (NSArray *)getClusters:(CGFloat) zoomLevel {
    if (zoomLevel < 4 || zoomLevel > 22) {
        return nil;
    }
    NSMutableArray *results = [NSMutableArray array];
    
    CGFloat zoomSpecificSpan = MAX_DISTANCE_IN_DP / pow(2, zoomLevel) / 256;
    NSMutableSet *visitedCandidates = [NSMutableSet set];
    NSMutableDictionary *distanceToCluster = [NSMutableDictionary dictionary];
    NSMutableDictionary *itemToCluster = [NSMutableDictionary dictionary];
    
    @synchronized(_quadtree) {
        for (BMKQuadItem *candidate in _quadItems) {
            //candidate已经添加到另一cluster中
            if ([visitedCandidates containsObject:candidate]) {
                continue;
            }
            BMKCluster *cluster = [[BMKCluster alloc] init];
            cluster.coordinate = candidate.coordinate;
            
            CGRect searchRect = [self getRectWithPt:candidate.pt span:zoomSpecificSpan];
            NSMutableArray *items = (NSMutableArray *)[_quadtree searchInRect:searchRect];
            if (items.count == 1) {
                CLLocationCoordinate2D coor = candidate.coordinate;
                NSValue *value = [NSValue value:&coor withObjCType:@encode(CLLocationCoordinate2D)];
                [cluster.clusterAnnotations addObject:value];
                
                [results addObject:cluster];
                [visitedCandidates addObject:candidate];
                [distanceToCluster setObject:[NSNumber numberWithDouble:0] forKey:[NSNumber numberWithLongLong:candidate.hash]];
                continue;
            }
            
            for (BMKQuadItem *quadItem in items) {
                NSNumber *existDistache = [distanceToCluster objectForKey:[NSNumber numberWithLongLong:quadItem.hash]];
                CGFloat distance = [self getDistanceSquared:candidate.pt otherPoint:quadItem.pt];
                if (existDistache != nil) {
                    if (existDistache.doubleValue < distance) {
                        continue;
                    }
                    BMKCluster *existCluster = [itemToCluster objectForKey:[NSNumber numberWithLongLong:quadItem.hash]];
                    CLLocationCoordinate2D coor = quadItem.coordinate;
                    NSValue *value = [NSValue value:&coor withObjCType:@encode(CLLocationCoordinate2D)];
                    [existCluster.clusterAnnotations removeObject:value];
                }
                
                [distanceToCluster setObject:[NSNumber numberWithDouble:distance] forKey:[NSNumber numberWithLongLong:quadItem.hash]];
                CLLocationCoordinate2D coor = quadItem.coordinate;
                NSValue *value = [NSValue value:&coor withObjCType:@encode(CLLocationCoordinate2D)];
                [cluster.clusterAnnotations addObject:value];
                [itemToCluster setObject:cluster forKey:[NSNumber numberWithLongLong:quadItem.hash]];
            }
            [visitedCandidates addObjectsFromArray:items];
            [results addObject:cluster];
        }
    }
    return results;
}

- (CGRect)getRectWithPt:(CGPoint) pt  span:(CGFloat) span {
    CGFloat half = span / 2.f;
    return CGRectMake(pt.x - half, pt.y - half, span, span);
}

- (CGFloat)getDistanceSquared:(CGPoint) pt otherPoint:(CGPoint)otherPoint {
    return pow(pt.x - otherPoint.x, 2) + pow(pt.y - otherPoint.y, 2);
}


@end
