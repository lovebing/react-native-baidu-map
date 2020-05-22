/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.mapapi.map.Gradient;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.util.ColorUtil;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayHeatMap;

/**
 * @author lovebing
 * @date 2020-05-22
 */
public class OverlayHeatMapManager extends SimpleViewManager<OverlayHeatMap> {

    private static final String TAG = OverlayHeatMapManager.class.getSimpleName();

    @NonNull
    @Override
    public String getName() {
        return "BaiduMapOverlayHeatMap";
    }

    @NonNull
    @Override
    protected OverlayHeatMap createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new OverlayHeatMap(reactContext);
    }

    @ReactProp(name = "points")
    public void setPoints(OverlayHeatMap overlayHeatMap, ReadableArray points) {
        overlayHeatMap.setPoints(LatLngUtil.fromReadableArray(points));
    }

    @ReactProp(name = "gradient")
    public void setGradient(OverlayHeatMap overlayHeatMap, ReadableMap gradient) {
        ReadableArray colorsArray = gradient.getArray("colors");
        ReadableArray startPointsArray = gradient.getArray("startPoints");
        if (colorsArray == null
                || startPointsArray == null
                || colorsArray.size() == 0
                || colorsArray.size() != startPointsArray.size()) {
            Log.w(TAG, "setGradient error: invalid params");
            return;
        }
        int[] colors = new int[colorsArray.size()];
        float[] startPoints = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            if (colorsArray.getString(i) == null) {
                Log.w(TAG, "setGradient error: invalid params");
                return;
            }
            colors[i] = ColorUtil.fromString(colorsArray.getString(i));
            startPoints[i] = (float) startPointsArray.getDouble(i);
        }
        overlayHeatMap.setGradient(new Gradient(colors, startPoints));
    }
}
