//
//  BMKPointAnnotationPro.h
//  CodePush
//
//  Created by n on 2019/12/9.
//

#import <BaiduMapAPI_Map/BMKPointAnnotation.h>
#import <BaiduMapAPI_Map/BMKAnnotationView.h>


NS_ASSUME_NONNULL_BEGIN
//为什么是Pro呢？因为它在创建时就关联了View
@interface BMKPointAnnotationPro : BMKPointAnnotation

@property (nonatomic,copy) BMKAnnotationView *(^getAnnotationView)(BMKPointAnnotation *annotation);

@end

NS_ASSUME_NONNULL_END
