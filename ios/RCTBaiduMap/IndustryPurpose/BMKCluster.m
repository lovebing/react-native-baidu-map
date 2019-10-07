//
//  BMKClusterItem.m
//  BMKObjectiveCDemo
//
//  Created by Baidu RD on 2018/3/6.
//  Copyright © 2015年 Baidu. All rights reserved.
//

#import "BMKCluster.h"

@implementation BMKCluster

#pragma mark - Initialization method
- (id)init {
    self = [super init];
    if (self) {
        _clusterAnnotations = [[NSMutableArray alloc] init];
    }
    return self;
}

#pragma mark - View life cycle
- (NSUInteger)size {
    return _clusterAnnotations.count;
}

@end
