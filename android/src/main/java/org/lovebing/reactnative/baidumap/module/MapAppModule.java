/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.module;

import android.content.Context;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class MapAppModule extends BaseModule {

    private static final String KEY_NAME = "name";

    private Context context;

    public MapAppModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @Override
    public String getName() {
        return "BaiduMapAppModule";
    }

    @ReactMethod
    public void openDrivingRoute(ReadableMap startPoint, ReadableMap endPoint) {
        RouteParaOption paraOption = createRouteParaOption(startPoint, endPoint);
        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(paraOption, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaiduMapRoutePlan.finish(context);
    }

    @ReactMethod
    public void openTransitRoute(ReadableMap startPoint, ReadableMap endPoint) {
        RouteParaOption paraOption = createRouteParaOption(startPoint, endPoint);
        try {
            BaiduMapRoutePlan.openBaiduMapTransitRoute(paraOption, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaiduMapRoutePlan.finish(context);
    }

    @ReactMethod
    public void openWalkNavi(ReadableMap startPoint, ReadableMap endPoint) {
        NaviParaOption para = new NaviParaOption()
                .startPoint(LatLngUtil.fromReadableMap(startPoint))
                .endPoint(LatLngUtil.fromReadableMap(endPoint))
                .startName(startPoint.getString("name"))
                .endName(endPoint.getString("name"));
        try {
            BaiduMapNavigation.openBaiduMapWalkNavi(para, context);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
        }
        BaiduMapNavigation.finish(context);
    }

    @ReactMethod
    public void openPoiNearbySearch(ReadableMap options) {
        PoiParaOption para = new PoiParaOption()
                .key(options.getString("key"))
                .center(LatLngUtil.fromReadableMap(options))
                .radius(options.getInt("radius"));
        try {
            BaiduMapPoiSearch.openBaiduMapPoiNearbySearch(para, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaiduMapPoiSearch.finish(context);
    }

    @ReactMethod
    public void openPoiDetialsPage(String uid) {
        PoiParaOption para = new PoiParaOption().uid(uid);
        try {
            BaiduMapPoiSearch.openBaiduMapPoiDetialsPage(para, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaiduMapPoiSearch.finish(context);
    }

    @ReactMethod
    public void openPanoShow(String uid) {
        try {
            BaiduMapPoiSearch.openBaiduMapPanoShow(uid, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaiduMapPoiSearch.finish(context);
    }

    private RouteParaOption createRouteParaOption(ReadableMap startPoint, ReadableMap endPoint) {
        RouteParaOption paraOption = new RouteParaOption()
                .startPoint(LatLngUtil.fromReadableMap(startPoint))
                .endPoint(LatLngUtil.fromReadableMap(endPoint))
                .busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
        if (startPoint.hasKey(KEY_NAME)) {
            paraOption.startName(startPoint.getString(KEY_NAME));
        }
        if (endPoint.hasKey(KEY_NAME)) {
            paraOption.endName(endPoint.getString(KEY_NAME));
        }
        return paraOption;
    }
}
