//
//  RCBMImageAnnotView.m
//  react-native-baidu-map
//
//  Created by n on 2019/12/9.
//

#import "RCBMImageAnnotView.h"
#import "RCTImageView.h"
#import <React/RCTImageSource.h>
#import <React/UIView+React.h>

// RCTImageView的属性：
//@property (nonatomic, copy) NSArray<RCTImageSource *> *imageSources;

@implementation RCBMImageAnnotView
-(id)initWithAnnotation:(id<BMKAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
    if(self){
    }
    return self;
}


- (void)setSource:(RCTImageSource *)source{
    NSAssert(self.bridge, @"must setBridge first");
    if (source) {
        _source = source;
    } else {
        _source = [self.class defaultSource];//source可能为nil，这时提供默认值
    }

    CGRect frame = CGRectMake(0, 0, self.source.size.width,self.source.size.height);

    RCTImageView *iv = [[RCTImageView alloc] initWithBridge:self.bridge];
    [iv setImageSources:@[self.source]];
    [iv reactSetFrame:frame];

    [self addSubview:iv];
    self.bounds = frame;
}

+ (RCTImageSource *)defaultSource {
    NSURL *url = [[NSBundle mainBundle]URLForResource:@"rn-baidu-map-default_marker_icon@2x.png" withExtension:@""];
    NSURLRequest *req = [[NSURLRequest alloc]initWithURL:url];
    CGSize size = CGSizeMake(30, 30);
    CGFloat scale = 2;
    RCTImageSource * s = [[RCTImageSource alloc]initWithURLRequest:req size:size scale:scale];
    return s;
}
@end

