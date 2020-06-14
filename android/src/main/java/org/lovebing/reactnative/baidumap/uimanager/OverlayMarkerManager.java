/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;
import android.view.View;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.model.IconInfo;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayInfoWindow;
import org.lovebing.reactnative.baidumap.view.OverlayMarker;
import org.lovebing.reactnative.baidumap.view.OverlayMarkerIcon;

public class OverlayMarkerManager extends ViewGroupManager<OverlayMarker> {

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

    @ReactProp(name = "animateType")
    public void setAnimateType(OverlayMarker overlayMarker, String animateType) {
        overlayMarker.setAnimateType(animateType);
    }

    @ReactProp(name = "titleOffsetY")
    public void setTitleOffsetY(OverlayMarker overlayMarker, int titleOffsetY) {
        overlayMarker.setTitleOffsetY(titleOffsetY);
    }

    @ReactProp(name = "location")
    public void setLocation(OverlayMarker overlayMarker, ReadableMap position) {
        overlayMarker.setPosition(LatLngUtil.fromReadableMap(position));
    }

    @ReactProp(name = "icon")
    public void setIcon(OverlayMarker overlayMarker, ReadableMap icon) {
        if (icon != null && icon.hasKey("uri")) {
            IconInfo iconInfo = new IconInfo();
            iconInfo.setUri(icon.getString("uri"));
            if (icon.hasKey("width")) {
                iconInfo.setWidth(icon.getInt("width"));
            }
            if (icon.hasKey("height")) {
                iconInfo.setHeight(icon.getInt("height"));
            }
            Log.i("iconInfo", iconInfo.getUri());
            overlayMarker.setIcon(iconInfo);
        }
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

    @Override
    public void addView(OverlayMarker parent, View child, int index) {
        if (child instanceof OverlayInfoWindow) {
            parent.setOverlayInfoWindow((OverlayInfoWindow) child);
        } else if (child instanceof OverlayMarkerIcon) {
            parent.setIconView(child);
        }
        super.addView(parent, child, index);
    }

    @Override
    public void removeViewAt(OverlayMarker parent, int index) {
        super.removeViewAt(parent, index);
    }
}
