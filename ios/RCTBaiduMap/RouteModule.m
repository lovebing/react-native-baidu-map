//
//  RouteModule.m
//  RCTBaiduMap
//
//  Created by Run on 2017/12/4.
//  Copyright © 2017年 lovebing.org. All rights reserved.
//

#import "RouteModule.h"

@implementation RouteModule

@synthesize bridge = _bridge;

static BMKRouteSearch *routeSearch;

-(BMKRouteSearch *)routeSearch{
    if(!routeSearch) {
        routeSearch = [[BMKRouteSearch alloc]init];
    }
    return routeSearch;
}

RCT_EXPORT_MODULE(RouteModule)

RCT_EXPORT_METHOD(onRoutePlan:(NSString*)name startCityName:(NSString*)sCityName end:(NSString*)endName endCityName:(NSString*)eCityName routeType:(NSInteger)type) {
    [self routeSearch].delegate = self;
    BMKPlanNode* start = [[BMKPlanNode alloc]init];
    start.name = name;
    start.cityName = sCityName;
    BMKPlanNode* end = [[BMKPlanNode alloc]init];
    end.name = endName;
    end.cityName = eCityName;
    [self routOption:start end:end routeType:type];
}

RCT_EXPORT_METHOD(onRoutPlanByCoordinate:(NSDictionary *)startPoint end:(NSDictionary *)endPoint routeType:(NSInteger)type) {
    //latitude=29.693694&longitude=121.389699
    [self routeSearch].delegate = self;
    BMKPlanNode* start = [[BMKPlanNode alloc]init];
     CLLocationCoordinate2D coor = [self getCoorFromMarkerOption:startPoint];
    start.pt = coor;
    BMKPlanNode* end = [[BMKPlanNode alloc]init];
    CLLocationCoordinate2D endCoor = [self getCoorFromMarkerOption:endPoint];
    end.pt = endCoor;
    [self routOption:start end:end routeType:type];
}


-(CLLocationCoordinate2D)getCoorFromMarkerOption:(NSDictionary *)option {
    double lat = [RCTConvert double:option[@"latitude"]];
    double lng = [RCTConvert double:option[@"longitude"]];
    CLLocationCoordinate2D coor;
    coor.latitude = lat;
    coor.longitude = lng;
    return coor;
}


- (void)routOption:(BMKPlanNode *)start end:(BMKPlanNode *)end routeType:(NSInteger)type {
    BOOL flag ;
    BMKDrivingRoutePlanOption *drivingRouteSearchOption = [[BMKDrivingRoutePlanOption alloc]init];
    drivingRouteSearchOption.from = start;
    drivingRouteSearchOption.to = end;
    drivingRouteSearchOption.drivingRequestTrafficType = BMK_DRIVING_REQUEST_TRAFFICE_TYPE_NONE;//不获取路况信息
    flag = [[self routeSearch] drivingSearch:drivingRouteSearchOption];
    if(type == 1) {
        BMKMassTransitRoutePlanOption *massTransitRouteOption = [[BMKMassTransitRoutePlanOption alloc]init];
        massTransitRouteOption.from = start;
        massTransitRouteOption.to = end;
        flag = [[self routeSearch] massTransitSearch:massTransitRouteOption];
    }
    if (type == 2) {
        BMKRidingRoutePlanOption *option = [[BMKRidingRoutePlanOption alloc]init];
        option.from = start;
        option.to = end;
        flag = [[self routeSearch]  ridingSearch:option];
        
    }
    if(type == 3) {
        BMKWalkingRoutePlanOption *walkingRouteOption = [[BMKWalkingRoutePlanOption alloc] init];
        walkingRouteOption.from = start;
        walkingRouteOption.to = end;
        flag = [[self routeSearch] walkingSearch:walkingRouteOption];
    }
    
    if(flag)
    {
        NSLog(@"%ld检索发送成功",type);
    }
    else
    {
        NSLog(@"%ld检索发送失败",type);
    }
}

#pragma  mark  ---- Driving ---
- (void)onGetDrivingRouteResult:(BMKRouteSearch *)searcher result:(BMKDrivingRouteResult *)result errorCode:(BMKSearchErrorCode)error {
    NSLog(@"onGetDrivingRouteResult error:%d", (int)error);
       NSMutableDictionary *body = [self getEmptyBody];
    if (error == BMK_SEARCH_NO_ERROR) {
        //成功获取结果
        BMKDrivingRouteLine* plan = (BMKDrivingRouteLine*)[result.routes objectAtIndex:0];
        // 计算路线方案中的路段数目
        NSInteger size = [plan.steps count];
        int planPointCounts = 0;
        
//        NSMutableArray *array = [NSMutableArray array];
        for (int i = 0; i < size; i++) {
            BMKDrivingStep* transitStep = [plan.steps objectAtIndex:i];
//            if(i==0){
//                RouteAnnotation* item = [[RouteAnnotation alloc]init];
//                item.coordinate = plan.starting.location;
//                item.title = @"起点";
//                item.type = 0;
//                [array addObject:item];
//
//            }
//            if(i==size-1){
//                RouteAnnotation* item = [[RouteAnnotation alloc]init];
//                item.coordinate = plan.terminal.location;
//                item.title = @"终点";
//                item.type = 1;
//               [array addObject:item];
//            }
//            //添加annotation节点
//            RouteAnnotation* item = [[RouteAnnotation alloc]init];
//            item.coordinate = transitStep.entrace.location;
//            item.title = transitStep.entraceInstruction;
//            item.degree = transitStep.direction * 30;
//            item.type = 4;
//            [array addObject:item];
            
            NSLog(@"%@   %@    %@", transitStep.entraceInstruction, transitStep.exitInstruction, transitStep.instruction);
            
            //轨迹点总数累计
            planPointCounts += transitStep.pointsCount;
        }
//        // 添加途经点
//        if (plan.wayPoints) {
//            for (BMKPlanNode* tempNode in plan.wayPoints) {
//                RouteAnnotation* item = [[RouteAnnotation alloc]init];
//                item = [[RouteAnnotation alloc]init];
//                item.coordinate = tempNode.pt;
//                item.type = 5;
//                item.title = tempNode.name;
//                [array addObject:item];
//            }
//        }
        //轨迹点
        BMKMapPoint * temppoints = new BMKMapPoint[planPointCounts];
         NSMutableArray *points = [NSMutableArray arrayWithCapacity:planPointCounts];
        int i = 0;
        for (int j = 0; j < size; j++) {
            BMKDrivingStep* transitStep = [plan.steps objectAtIndex:j];
            int k=0;
            for(k=0;k<transitStep.pointsCount;k++) {
                double x = transitStep.points[k].x;
                double y = transitStep.points[k].y;
                [points addObject:@{@"x":@(x),@"y":@(y)}];
                temppoints[i].x = transitStep.points[k].x;
                temppoints[i].y = transitStep.points[k].y;
                i++;
            }
        }
        body[@"dates"] = [NSString stringWithFormat:@"%d",plan.duration.dates];
        body[@"hours"] = [NSString stringWithFormat:@"%d",plan.duration.hours];
        body[@"minutes"] = [NSString stringWithFormat:@"%d",plan.duration.minutes];
        body[@"seconds"] = [NSString stringWithFormat:@"%d",plan.duration.seconds];
        body[@"points"] = points;
        body[@"count"] = [NSString stringWithFormat:@"%d", planPointCounts];
    } else {
        body[@"errcode"] = [NSString stringWithFormat:@"%d", error];
        body[@"errmsg"] = [self getSearchErrorInfo:error];
    }
    [self sendEvent:@"onGetPlanSearch" body:body];
    searcher.delegate = nil;
}

#pragma  mark  ---- Walking ---
- (void)onGetWalkingRouteResult:(BMKRouteSearch *)searcher result:(BMKWalkingRouteResult *)result errorCode:(BMKSearchErrorCode)error {
    NSLog(@"onGetWalkingRouteResult error:%d", (int)error);
    NSMutableDictionary *body = [self getEmptyBody];
    if (error == BMK_SEARCH_NO_ERROR) {
        //成功获取结果
        BMKWalkingRouteLine* plan = (BMKWalkingRouteLine*)[result.routes objectAtIndex:0];
        // 计算路线方案中的路段数目
        NSInteger size = [plan.steps count];
          NSLog(@"%ld",size);
        int planPointCounts = 0;
        //        NSMutableArray *array = [NSMutableArray array];
        for (int i = 0; i < size; i++) {
            BMKWalkingStep* transitStep = [plan.steps objectAtIndex:i];
            //            if(i==0){
            //                RouteAnnotation* item = [[RouteAnnotation alloc]init];
            //                item.coordinate = plan.starting.location;
            //                item.title = @"起点";
            //                item.type = 0;
            //                [array addObject:item];
            //
            //            }
            //            if(i==size-1){
            //                RouteAnnotation* item = [[RouteAnnotation alloc]init];
            //                item.coordinate = plan.terminal.location;
            //                item.title = @"终点";
            //                item.type = 1;
            //               [array addObject:item];
            //            }
            //            //添加annotation节点
            //            RouteAnnotation* item = [[RouteAnnotation alloc]init];
            //            item.coordinate = transitStep.entrace.location;
            //            item.title = transitStep.entraceInstruction;
            //            item.degree = transitStep.direction * 30;
            //            item.type = 4;
            //            [array addObject:item];
            
            NSLog(@"%@   %@    %@", transitStep.entraceInstruction, transitStep.exitInstruction, transitStep.instruction);
            
            //轨迹点总数累计
            planPointCounts += transitStep.pointsCount;
        }
        //        // 添加途经点
        //        if (plan.wayPoints) {
        //            for (BMKPlanNode* tempNode in plan.wayPoints) {
        //                RouteAnnotation* item = [[RouteAnnotation alloc]init];
        //                item = [[RouteAnnotation alloc]init];
        //                item.coordinate = tempNode.pt;
        //                item.type = 5;
        //                item.title = tempNode.name;
        //                [array addObject:item];
        //            }
        //        }
        //轨迹点
        BMKMapPoint * temppoints = new BMKMapPoint[planPointCounts];
        int i = 0;
        NSMutableArray *points = [NSMutableArray arrayWithCapacity:planPointCounts];
        for (int j = 0; j < size; j++) {
            BMKWalkingStep* transitStep = [plan.steps objectAtIndex:j];
            int k=0;
            for(k=0;k<transitStep.pointsCount;k++) {
                double x = transitStep.points[k].x;
                double y = transitStep.points[k].y;
                [points addObject:@{@"x":@(x),@"y":@(y)}];
                temppoints[i].x = transitStep.points[k].x;
                temppoints[i].y = transitStep.points[k].y;
                i++;
            }
        }
        body[@"dates"] = [NSString stringWithFormat:@"%d",plan.duration.dates];
        body[@"hours"] = [NSString stringWithFormat:@"%d",plan.duration.hours];
        body[@"minutes"] = [NSString stringWithFormat:@"%d",plan.duration.minutes];
        body[@"seconds"] = [NSString stringWithFormat:@"%d",plan.duration.seconds];
        body[@"points"] = points;
        body[@"count"] = [NSString stringWithFormat:@"%d", planPointCounts];
    } else {
        body[@"errcode"] = [NSString stringWithFormat:@"%d", error];
        body[@"errmsg"] = [self getSearchErrorInfo:error];
    }
    [self sendEvent:@"onGetPlanSearch" body:body];
    searcher.delegate = nil;
}

#pragma  mark  ---- Riding ---
- (void)onGetRidingRouteResult:(BMKRouteSearch *)searcher result:(BMKRidingRouteResult *)result errorCode:(BMKSearchErrorCode)error {
    NSLog(@"onGetRidingRouteResult error:%d", (int)error);
    NSMutableDictionary *body = [self getEmptyBody];
    if (error == BMK_SEARCH_NO_ERROR) {
        //成功获取结果
        BMKRidingRouteLine* plan = (BMKRidingRouteLine*)[result.routes objectAtIndex:0];
        // 计算路线方案中的路段数目
        NSInteger size = [plan.steps count];
        int planPointCounts = 0;
        //        NSMutableArray *array = [NSMutableArray array];
        NSLog(@"%ld",size);
        for (int i = 0; i < size; i++) {
            BMKRidingStep* transitStep = [plan.steps objectAtIndex:i];
            //            if(i==0){
            //                RouteAnnotation* item = [[RouteAnnotation alloc]init];
            //                item.coordinate = plan.starting.location;
            //                item.title = @"起点";
            //                item.type = 0;
            //                [array addObject:item];
            //
            //            }
            //            if(i==size-1){
            //                RouteAnnotation* item = [[RouteAnnotation alloc]init];
            //                item.coordinate = plan.terminal.location;
            //                item.title = @"终点";
            //                item.type = 1;
            //               [array addObject:item];
            //            }
            //            //添加annotation节点
            //            RouteAnnotation* item = [[RouteAnnotation alloc]init];
            //            item.coordinate = transitStep.entrace.location;
            //            item.title = transitStep.entraceInstruction;
            //            item.degree = transitStep.direction * 30;
            //            item.type = 4;
            //            [array addObject:item];
            
            NSLog(@"%@   %@    %@", transitStep.entraceInstruction, transitStep.exitInstruction, transitStep.instruction);
            
            //轨迹点总数累计
            planPointCounts += transitStep.pointsCount;
        }
        //        // 添加途经点
        //        if (plan.wayPoints) {
        //            for (BMKPlanNode* tempNode in plan.wayPoints) {
        //                RouteAnnotation* item = [[RouteAnnotation alloc]init];
        //                item = [[RouteAnnotation alloc]init];
        //                item.coordinate = tempNode.pt;
        //                item.type = 5;
        //                item.title = tempNode.name;
        //                [array addObject:item];
        //            }
        //        }
        //轨迹点
        BMKMapPoint * temppoints = new BMKMapPoint[planPointCounts];
        NSMutableArray *points = [NSMutableArray arrayWithCapacity:planPointCounts];
        int i = 0;
        for (int j = 0; j < size; j++) {
            BMKRidingStep* transitStep = [plan.steps objectAtIndex:j];
            int k=0;
            for(k=0;k<transitStep.pointsCount;k++) {
                double x = transitStep.points[k].x;
                double y = transitStep.points[k].y;
                [points addObject:@{@"x":@(x),@"y":@(y)}];
                temppoints[i].x = transitStep.points[k].x;
                temppoints[i].y = transitStep.points[k].y;
                i++;
            }
        }
        body[@"dates"] = [NSString stringWithFormat:@"%d",plan.duration.dates];
        body[@"hours"] = [NSString stringWithFormat:@"%d",plan.duration.hours];
        body[@"minutes"] = [NSString stringWithFormat:@"%d",plan.duration.minutes];
        body[@"seconds"] = [NSString stringWithFormat:@"%d",plan.duration.seconds];
        body[@"points"] = points;
        body[@"count"] = [NSString stringWithFormat:@"%d", planPointCounts];
    } else {
        body[@"errcode"] = [NSString stringWithFormat:@"%d", error];
        body[@"errmsg"] = [self getSearchErrorInfo:error];
    }
    [self sendEvent:@"onGetPlanSearch" body:body];
   searcher.delegate = nil;
}

#pragma  mark  ---- MassTransit ---
- (void)onGetMassTransitRouteResult:(BMKRouteSearch *)searcher result:(BMKMassTransitRouteResult *)result errorCode:(BMKSearchErrorCode)error {
    NSLog(@"onGetMassTransitRouteResult error:%d", (int)error);
    NSMutableDictionary *body = [self getEmptyBody];
    if (error == BMK_SEARCH_NO_ERROR) {
        BMKMassTransitRouteLine* routeLine = (BMKMassTransitRouteLine*)[result.routes objectAtIndex:0];
        
        BOOL startCoorIsNull = YES;
        CLLocationCoordinate2D startCoor;//起点经纬度
        CLLocationCoordinate2D endCoor;//终点经纬度
        
        NSInteger size = [routeLine.steps count];
        NSInteger planPointCounts = 0;
        for (NSInteger i = 0; i < size; i++) {
            BMKMassTransitStep* transitStep = [routeLine.steps objectAtIndex:i];
            for (BMKMassTransitSubStep *subStep in transitStep.steps) {
      
                if (startCoorIsNull) {
                    startCoor = subStep.entraceCoor;
                    startCoorIsNull = NO;
                }
                endCoor = subStep.exitCoor;
                
                //轨迹点总数累计
                planPointCounts += subStep.pointsCount;
                

                NSLog(@"%@   ", subStep.instructions);
                //steps中是方案还是子路段，YES:steps是BMKMassTransitStep的子路段（A到B需要经过多个steps）;NO:steps是多个方案（A到B有多个方案选择）
                if (transitStep.isSubStep == NO) {//是子方案，只取第一条方案
                    break;
                }
                else {
                    //是子路段，需要完整遍历transitStep.steps
                }
              
            }
        }
    
        
        //轨迹点
        BMKMapPoint * temppoints = new BMKMapPoint[planPointCounts];
         NSMutableArray *points = [NSMutableArray arrayWithCapacity:planPointCounts];
        NSInteger index = 0;
        for (BMKMassTransitStep* transitStep in routeLine.steps) {
            for (BMKMassTransitSubStep *subStep in transitStep.steps) {
                for (NSInteger i = 0; i < subStep.pointsCount; i++) {
                    double x = subStep.points[i].x;
                    double y = subStep.points[i].y;
                    [points addObject:@{@"x":@(x),@"y":@(y)}];
                    temppoints[index].x = subStep.points[i].x;
                    temppoints[index].y = subStep.points[i].y;
                    index++;
                }
                
                //steps中是方案还是子路段，YES:steps是BMKMassTransitStep的子路段（A到B需要经过多个steps）;NO:steps是多个方案（A到B有多个方案选择）
                if (transitStep.isSubStep == NO) {//是子方案，只取第一条方案
                    break;
                }
                else {
                    //是子路段，需要完整遍历transitStep.steps
                }
            }
        }
        body[@"dates"] = [NSString stringWithFormat:@"%d",routeLine.duration.dates];
        body[@"hours"] = [NSString stringWithFormat:@"%d",routeLine.duration.hours];
        body[@"minutes"] = [NSString stringWithFormat:@"%d",routeLine.duration.minutes];
        body[@"seconds"] = [NSString stringWithFormat:@"%d",routeLine.duration.seconds];
        body[@"points"] = points;
        body[@"count"] = [NSString stringWithFormat:@"%ld", planPointCounts];
    } else {
        body[@"errcode"] = [NSString stringWithFormat:@"%d", error];
        body[@"errmsg"] = [self getSearchErrorInfo:error];
    }
    [self sendEvent:@"onGetPlanSearch" body:body];
    searcher.delegate = nil;
}

- (NSString *)getSearchErrorInfo:(BMKSearchErrorCode)error {
    NSString *errormsg = @"未知";
    switch (error) {
        case BMK_SEARCH_AMBIGUOUS_KEYWORD:
            errormsg = @"检索词有岐义";
            break;
        case BMK_SEARCH_AMBIGUOUS_ROURE_ADDR:
            errormsg = @"检索地址有岐义";
            break;
        case BMK_SEARCH_NOT_SUPPORT_BUS:
            errormsg = @"该城市不支持公交搜索";
            break;
        case BMK_SEARCH_NOT_SUPPORT_BUS_2CITY:
            errormsg = @"不支持跨城市公交";
            break;
        case BMK_SEARCH_RESULT_NOT_FOUND:
            errormsg = @"没有找到检索结果";
            break;
        case BMK_SEARCH_ST_EN_TOO_NEAR:
            errormsg = @"起终点太近";
            break;
        case BMK_SEARCH_KEY_ERROR:
            errormsg = @"key错误";
            break;
        case BMK_SEARCH_NETWOKR_ERROR:
            errormsg = @"网络连接错误";
            break;
        case BMK_SEARCH_NETWOKR_TIMEOUT:
            errormsg = @"网络连接超时";
            break;
        case BMK_SEARCH_PERMISSION_UNFINISHED:
            errormsg = @"还未完成鉴权，请在鉴权通过后重试";
            break;
        case BMK_SEARCH_INDOOR_ID_ERROR:
            errormsg = @"室内图ID错误";
            break;
        case BMK_SEARCH_FLOOR_ERROR:
            errormsg = @"室内图检索楼层错误";
            break;
        default:
            break;
    }
    return errormsg;
}



@end
