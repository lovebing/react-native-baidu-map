//
//  BMKPointAnnotationPro.h
//
//  Created by lovebing on 2020/5/15.
//  Copyright © 2020 lovebing.net. All rights reserved.
//

#ifndef BMKPointAnnotationPro_h
#define BMKPointAnnotationPro_h

#import <BaiduMapAPI_Map/BMKPinAnnotationView.h>
#import <BaiduMapAPI_Map/BMKPointAnnotation.h>
#import <UIKit/UIKit.h>

@interface BMKPointAnnotationPro : BMKPointAnnotation

@property (nonatomic, strong) BMKPinAnnotationView *annotationView;

@end

#endif /* BMKPointAnnotationPro_h */
