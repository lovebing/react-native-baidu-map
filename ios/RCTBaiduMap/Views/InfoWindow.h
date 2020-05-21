//
//  InfoWindow.h
//  RCTBaiduMap
//
//  Created by lovebing on 2020/5/16.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef InfoWindow_h
#define InfoWindow_h

#import <BaiduMapAPI_Map/BMKActionPaopaoView.h>
#import <React/RCTBridge.h>
#import <React/UIView+React.h>

@interface InfoWindow : UIView

@property (nonatomic, weak) RCTBridge *bridge;

@end

#endif /* InfoWindow_h */
