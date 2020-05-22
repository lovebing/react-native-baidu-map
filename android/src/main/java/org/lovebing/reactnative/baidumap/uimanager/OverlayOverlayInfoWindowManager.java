/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;
import android.view.View;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import org.lovebing.reactnative.baidumap.view.OverlayInfoWindow;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class OverlayOverlayInfoWindowManager extends ViewGroupManager<OverlayInfoWindow> {

    @Override
    public String getName() {
        return "BaiduMapOverlayInfoWindow";
    }

    @Override
    protected OverlayInfoWindow createViewInstance(ThemedReactContext reactContext) {
        return new OverlayInfoWindow(reactContext);
    }

    @Override
    public void addView(OverlayInfoWindow parent, View child, int index) {
        Log.i("infoWindow addView", parent.hashCode() + ":" + child.getClass().getName() + ":" + child.hashCode());
        super.addView(parent, child, index);
    }

    @ReactProp(name = "width")
    public void setWidth(OverlayInfoWindow overlayInfoWindow, int width) {
        overlayInfoWindow.setWidth(width);
    }

    @ReactProp(name = "height")
    public void setHeight(OverlayInfoWindow overlayInfoWindow, int height) {
        overlayInfoWindow.setHeight(height);
    }

    @ReactProp(name = "offsetY")
    public void setOffsetY(OverlayInfoWindow overlayInfoWindow, int offsetY) {
        overlayInfoWindow.setOffsetY(offsetY);
    }
}
