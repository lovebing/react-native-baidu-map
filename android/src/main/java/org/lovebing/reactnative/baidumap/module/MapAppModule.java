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

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class MapAppModule extends BaseModule {

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
    public void openBaiduMapTransitRoute(ReadableMap start, ReadableMap end) {
        RouteParaOption option = new RouteParaOption()
                .startPoint(LatLngUtil.fromReadableMap(start))
                .endPoint(LatLngUtil.fromReadableMap(end))
                .busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
        try {
            BaiduMapRoutePlan.openBaiduMapTransitRoute(option, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
