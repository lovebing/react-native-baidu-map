/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap;

import android.os.Build;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;

import com.facebook.react.uimanager.ViewManager;

import org.lovebing.reactnative.baidumap.module.BaiduMapManager;
import org.lovebing.reactnative.baidumap.module.GeolocationModule;
import org.lovebing.reactnative.baidumap.module.GetDistanceModule;
import org.lovebing.reactnative.baidumap.module.MapAppModule;
import org.lovebing.reactnative.baidumap.uimanager.*;
import org.lovebing.reactnative.baidumap.view.OverlayMarkerIcon;


/**
 * Created by lovebing on 4/17/16.
 */
public class BaiduMapPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.asList(
                new BaiduMapManager(reactContext),
                new GeolocationModule(reactContext),
                new GetDistanceModule(reactContext),
                new MapAppModule(reactContext)
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<ViewManager> createViewManagers(
            ReactApplicationContext reactContext) {
        init(reactContext);
        return Arrays.asList(
                new MapViewManager(),
                new OverlayClusterManager(),
                new OverlayMarkerManager(),
                new OverlayMarkerIconManager(),
                new OverlayOverlayInfoWindowManager(),
                new OverlayArcManager(),
                new OverlayCircleManager(),
                new OverlayPolygonManager(),
                new OverlayPolylineManager(),
                new OverlayTextManager(),
                new OverlayHeatMapManager()
        );
    }

    @MainThread
    protected void init(ReactApplicationContext reactContext) {
        if (Looper.myLooper() == null){
            Looper.prepare();
        }
        SDKInitializer.initialize(reactContext.getApplicationContext());
    }
}
