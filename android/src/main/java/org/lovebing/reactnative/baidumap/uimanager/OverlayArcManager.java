/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.util.StrokeUtil;
import org.lovebing.reactnative.baidumap.view.OverlayArc;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class OverlayArcManager extends SimpleViewManager<OverlayArc> {

    @Override
    public String getName() {
        return "BaiduMapOverlayArc";
    }

    @Override
    protected OverlayArc createViewInstance(ThemedReactContext reactContext) {
        return new OverlayArc(reactContext);
    }

    @ReactProp(name = "stroke")
    public void setStroke(OverlayArc overlayArc, ReadableMap stroke) {
        overlayArc.setStroke(StrokeUtil.fromReadableMap(stroke));
    }

    @ReactProp(name = "points")
    public void setPoints(OverlayArc overlayArc, ReadableArray points) {
        OverlayArc.Points _points = new OverlayArc.Points(LatLngUtil.fromReadableMap(points.getMap(0)),
                LatLngUtil.fromReadableMap(points.getMap(1)),
                LatLngUtil.fromReadableMap(points.getMap(2)));
        overlayArc.setPoints(_points);
    }

}
