/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayMarker;

public class OverlayMarkerManager extends SimpleViewManager<OverlayMarker> {

    @Override
    public String getName() {
        return "BaiduMapOverlayMarker";
    }

    @Override
    protected OverlayMarker createViewInstance(ThemedReactContext themedReactContext) {
        return new OverlayMarker(themedReactContext);
    }

    @ReactProp(name = "title")
    public void setTitle(OverlayMarker overlayMarker, String title) {
        overlayMarker.setTitle(title);
    }

    @ReactProp(name = "location")
    public void setLocation(OverlayMarker overlayMarker, ReadableMap position) {
        overlayMarker.setPosition(LatLngUtil.fromReadableMap(position));
    }

    @ReactProp(name = "icon")
    public void setIcon(OverlayMarker overlayMarker, String uri) {
        overlayMarker.setIcon(uri);
    }

    @ReactProp(name = "perspective")
    public void setPerspective(OverlayMarker overlayMarker, boolean perspective) {
        overlayMarker.setPerspective(perspective);
    }

    @ReactProp(name = "alpha")
    public void setAlpha(OverlayMarker overlayMarker, float alpha) {
        overlayMarker.setAlpha(alpha);
    }

    @ReactProp(name = "rotate")
    public void setRotate(OverlayMarker overlayMarker, float rotate) {
        overlayMarker.setRotate(rotate);
    }

    @ReactProp(name = "flat")
    public void setFlat(OverlayMarker overlayMarker, boolean flat) {
        overlayMarker.setFlat(flat);
    }

}
