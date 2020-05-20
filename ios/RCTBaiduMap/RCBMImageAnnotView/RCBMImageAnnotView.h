//
//  RCBMImageAnnotView.h
//  react-native-baidu-map
//
//  Created by n on 2019/12/9.
//

#import <UIKit/UIKit.h>
#import <BaiduMapAPI_Map/BMKPointAnnotation.h>
#import <BaiduMapAPI_Map/BMKAnnotationView.h>
@class RCTImageSource;
@class RCTBridge;

NS_ASSUME_NONNULL_BEGIN

@interface RCBMImageAnnotView : BMKAnnotationView
@property (nonatomic,strong) RCTBridge * bridge;
@property (nonatomic,strong) RCTImageSource * source;

@end

NS_ASSUME_NONNULL_END
