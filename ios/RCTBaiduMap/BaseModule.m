//
//  BaseModule.m
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#import "BaseModule.h"

@implementation BaseModule

@synthesize bridge = _bridge;

- (NSArray<NSString *> *)supportedEvents{
  return nil;
}

- (NSMutableDictionary *)getEmptyBody {
    NSMutableDictionary *body = @{}.mutableCopy;
    return body;
}

- (void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body {
  [self sendEventWithName:name body:body];
}

@end
