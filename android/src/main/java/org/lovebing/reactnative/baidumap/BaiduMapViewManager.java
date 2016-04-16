package org.lovebing.reactnative.baidumap;

import android.content.Context;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

/**
 * Created by lovebing on 12/20/2015.
 */
public class BaiduMapViewManager extends SimpleViewManager<MapView> {

    private static final String REACT_CLASS = "RCTBaiduMapView";

    private static MapView mMapView;

    public String getName() {
        return REACT_CLASS;
    }

    public void initSDK(Context context) {
        SDKInitializer.initialize(context);
    }

    public MapView createViewInstance(ThemedReactContext context) {
        if(mMapView != null) {
            mMapView.onDestroy();
        }
        mMapView =  new MapView(context);
        return mMapView;
    }

    public static MapView getMapView() {
        return mMapView;
    }


}
