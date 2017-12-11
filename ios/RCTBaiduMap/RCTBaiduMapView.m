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
    BMKPointAnnotation* _annotation;
    NSMutableArray* _annotations;
    BMKLocationService *_server;
}

static RCTBaiduMapView *mapview = nil;

+ (RCTBaiduMapView *)shareInstance {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (!mapview) {
            mapview = [[RCTBaiduMapView alloc] init];
        }
    });
    return mapview;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.userTrackingMode = BMKUserTrackingModeNone;//设置定位的状态
        self.showsUserLocation = YES;//显示定位图层
        _server = [[BMKLocationService alloc]init];
        _server.delegate = self;
    }
    return self;
}

- (void)setStartLocation:(BOOL)startLocation {

    if (startLocation) {
        //启动LocationService
        [_server startUserLocationService];
    }
    else {
        //启动LocationService
        [_server stopUserLocationService];
    }
}

/**
 *用户方向更新后，会调用此函数
 *@param userLocation 新的用户位置
 */
- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
{
    [self updateLocationData:userLocation];
    NSLog(@"heading is %@",userLocation.heading);
}

/**
 *用户位置更新后，会调用此函数
 *@param userLocation 新的用户位置
 */
- (void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation
{
    NSLog(@"didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    [self updateLocationData:userLocation];
}

-(void)setZoom:(float)zoom {
    self.zoomLevel = zoom;
}

-(void)setCenterLatLng:(NSDictionary *)LatLngObj {
    double lat = [RCTConvert double:LatLngObj[@"lat"]];
    double lng = [RCTConvert double:LatLngObj[@"lng"]];
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(lat, lng);
    self.centerCoordinate = point;
    
    
}

-(void)setMarker:(NSDictionary *)option {
    NSLog(@"setMarker");
    if(option != nil) {
        if(_annotation == nil) {
            _annotation = [[BMKPointAnnotation alloc]init];
            [self addMarker:_annotation option:option];
        }
        else {
            [self updateMarker:_annotation option:option];
        }
    }
}

-(void)setMarkers:(NSArray *)markers {
    int markersCount = [markers count];
    if(_annotations == nil) {
        _annotations = [[NSMutableArray alloc] init];
    }
    if(markers != nil) {
        for (int i = 0; i < markersCount; i++)  {
            NSDictionary *option = [markers objectAtIndex:i];
            
            BMKPointAnnotation *annotation = nil;
            if(i < [_annotations count]) {
                annotation = [_annotations objectAtIndex:i];
            }
            if(annotation == nil) {
                annotation = [[BMKPointAnnotation alloc]init];
                [self addMarker:annotation option:option];
                [_annotations addObject:annotation];
            }
            else {
                [self updateMarker:annotation option:option];
            }
        }
        
        int _annotationsCount = [_annotations count];
        
        NSString *smarkersCount = [NSString stringWithFormat:@"%d", markersCount];
        NSString *sannotationsCount = [NSString stringWithFormat:@"%d", _annotationsCount];
        NSLog(smarkersCount);
        NSLog(sannotationsCount);
        
        if(markersCount < _annotationsCount) {
            int start = _annotationsCount - 1;
            for(int i = start; i >= markersCount; i--) {
                BMKPointAnnotation *annotation = [_annotations objectAtIndex:i];
                [self removeAnnotation:annotation];
                [_annotations removeObject:annotation];
            }
        }
        
        
    }
}

-(CLLocationCoordinate2D)getCoorFromMarkerOption:(NSDictionary *)option {
    double lat = [RCTConvert double:option[@"latitude"]];
    double lng = [RCTConvert double:option[@"longitude"]];
    CLLocationCoordinate2D coor;
    coor.latitude = lat;
    coor.longitude = lng;
    return coor;
}

-(void)addMarker:(BMKPointAnnotation *)annotation option:(NSDictionary *)option {
    [self updateMarker:annotation option:option];
    [self addAnnotation:annotation];
}

-(void)updateMarker:(BMKPointAnnotation *)annotation option:(NSDictionary *)option {
    CLLocationCoordinate2D coor = [self getCoorFromMarkerOption:option];
    NSString *title = [RCTConvert NSString:option[@"title"]];
    if(title.length == 0) {
        title = nil;
    }
    annotation.coordinate = coor;
    annotation.title = title;
}


- (void)setOverlayPoints:(NSArray *)overlayPoints {
    [self removeOverlays:self.overlays];
    NSLog(@"overlay  %@",overlayPoints);
    BMKMapPoint * temppoints = new BMKMapPoint[overlayPoints.count];
    for (NSInteger i = 0; i < overlayPoints.count; i ++) {
        NSDictionary *dic = overlayPoints[i];
        double x = [dic[@"x"] doubleValue];
        double y = [dic[@"y"] doubleValue];
        temppoints[i].x = x;
        temppoints[i].y = y;
            NSLog(@"x  %f    y  %f",x,y);
    }

    BMKPolyline* polyLine = [BMKPolyline polylineWithPoints:temppoints count:overlayPoints.count];
    [self addOverlay:polyLine];
}

@end
