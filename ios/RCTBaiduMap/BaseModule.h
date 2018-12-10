//
//  BaseModule.h
//  RCTBaiduMap
//
//  Created by lovebing on 2016/10/28.
//  Copyright © 2016年 lovebing.org. All rights reserved.
//

#ifndef BaseModule_h
#define BaseModule_h

#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTBridge.h>


@interface BaseModule : NSObject <RCTBridgeModule> {
    UINavigationController *navigationController;
}

-(void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body;

-(NSMutableDictionary *)getEmptyBody;

@end

#endif /* BaseModule_h */
