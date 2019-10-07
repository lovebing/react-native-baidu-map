//
//  BMKClusterQuadtree.h
//  BMKObjectiveCDemo
//
//  Created by Baidu RD on 2018/3/6.
//  Copyright © 2015年 Baidu. All rights reserved.
//
#import <Foundation/Foundation.h>
#import "BMKCluster.h"

@interface BMKQuadItem : NSObject
@property (nonatomic, readonly) CGPoint pt;
@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@end

@interface BMKClusterQuadtree : NSObject

//四叉树区域
@property (nonatomic, assign) CGRect rect;
//所包含BMKQuadItem
@property(nonatomic, readonly) NSMutableArray *quadItems;
- (id)initWithRect:(CGRect) rect;
//添加item
- (void)addItem:(BMKQuadItem*) quadItem;
//清除items
- (void)clearItems;
//获取rect范围内的BMKQuadItem
- (NSArray*)searchInRect:(CGRect) searchRect;

@end

