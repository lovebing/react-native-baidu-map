package org.lovebing.reactnative.baidumap;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;

import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContextBaseJavaModule;


public class GuideModule extends ReactContextBaseJavaModule{

    RoutePlanSearch mSearch;
    ReactApplicationContext context;

    public GuideModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context=reactContext;
    }

    public String getName() {
        return "BaiduGuide";
    }



    private void initLocationClient(int type) {
        mSearch = RoutePlanSearch.newInstance();
        guideType(type);
    }


    @ReactMethod
    public void getStart(int type) {
        if(mSearch == null) {
            initLocationClient(type);
        }else{
            guideType(type);
        }
    }



    public void guideType(int type){
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "西二旗地铁站");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "百度科技园");

        if(type==0){
            mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }

            });
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));

        }else if(type==1){
            mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener(){

                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }
            });

           mSearch.transitSearch(new TransitRoutePlanOption().from(stNode).to(enNode));

        }else if(type==2){
            mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }

            });
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }
        else if(type==3){
            mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }

            });
            mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
        }

    }

}
