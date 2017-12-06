package org.lovebing.reactnative.baidumap;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

public class GuideModule extends ReactContextBaseJavaModule{

    RoutePlanSearch mSearch;
    ReactApplicationContext context;

    public GeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context=reactContext;
    }

    public String getName() {
        return "BaiduGuide";
    }



    private void initLocationClient() {
        android.widget.Toast.makeText(context, "initLocationClient", Toast.LENGTH_SHORT).show();
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                android.widget.Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            }

        };
        );

        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "西二旗地铁站");

        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "百度科技园");
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
        android.widget.Toast.makeText(context, "end", Toast.LENGTH_SHORT).show();
    }


    @ReactMethod
    public void getStart(int type) {
        android.widget.Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
        if(locationClient == null) {
            initLocationClient();
        }else{
            android.widget.Toast.makeText(context, "getStart", Toast.LENGTH_SHORT).show();
            mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

                public void onGetWalkingRouteResult(WalkingRouteResult result) {
                    android.widget.Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                }

            };
        );

            PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "西二旗地铁站");

            PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "百度科技园");
            mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
            android.widget.Toast.makeText(context, "end", Toast.LENGTH_SHORT).show();
        }
    }

}
