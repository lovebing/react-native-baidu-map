//
//  ClusterAnnotation.h
//  react-native-baidu-map
//
//  Created by lovebing on 2019/10/7.
//  Copyright © 2019年 lovebing.org. All rights reserved.
//

#ifndef ClusterAnnotation_h
#define ClusterAnnotation_h

#import <BaiduMapAPI_Map/BMKPointAnnotation.h>

@interface ClusterAnnotation : BMKPointAnnotation

@property (nonatomic, assign) NSInteger size;

@end

#endif /* ClusterAnnotation_h */
