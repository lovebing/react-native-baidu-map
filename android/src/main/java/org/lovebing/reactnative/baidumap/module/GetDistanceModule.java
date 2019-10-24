// https://www.jianshu.com/p/d5fc74b72892
// Riant 略有调整 2019/10/22

package org.lovebing.reactnative.baidumap.module;

import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.bridge.Promise;

public class GetDistanceModule extends BaseModule{

    public GetDistanceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    public String getName() {
        return "BaiduGetDistanceModule";
    }

    @ReactMethod
    public void getLocationDistance(double lat1, double lng1, double lat2, double lng2, Promise promise) {
        WritableMap params = Arguments.createMap();
        LatLng p1 = new LatLng(lat1, lng1);
        LatLng p2 = new LatLng(lat2, lng2);
        //计算p1、p2两点之间的直线距离，单位：米
        double distance = DistanceUtil.getDistance(p1, p2);
        params.putDouble("distance", distance);
        promise.resolve(params);
    }
}
