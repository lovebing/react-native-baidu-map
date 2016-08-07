//
//  RCTBaiduMap.m
//  RCTBaiduMap
//
//  Created by lovebing on 4/17/2016.
//  Copyright © 2016 lovebing.org. All rights reserved.
//

#import "RCTBaiduMapView.h"

@implementation RCTBaiduMapView {
    BMKMapView* _mapView;
}

-(void)setZoom:(float)zoom {
    _mapView.zoomLevel = zoom;
}

-(void)setMapType:(int)mapType {
    BMKMapType type = NULL;
    
    switch(mapType) {
        case 0:
            type = BMKMapTypeNone;
            break;
        case 1:
            type = BMKMapTypeStandard;
            break;
        case 2:
            type = BMKMapTypeSatellite;
            break;
    }
    _mapView.mapType = type;
}

-(void)setCenterLatLng:(NSDictionary *)LatLngObj {
    double lat = [RCTConvert double:LatLngObj[@"lat"]];
    double lng = [RCTConvert double:LatLngObj[@"lng"]];
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(lat, lng);
    _mapView.centerCoordinate = point;
}

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
        super.backgroundColor = [UIColor clearColor];
        _mapView = [[BMKMapView alloc] initWithFrame:self.bounds];
        [self addSubview:_mapView];
    }
    return self;
}

- (instancetype)init {
    NSLog(@"init");
    if ((self = [super init])) {
        super.backgroundColor = [UIColor clearColor];
        _mapView = [[BMKMapView alloc] init];
        [self addSubview:_mapView];
    }
    return self;
}

@end
