/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class OverlayInfoWindow extends ViewGroup implements OverlayView {

    private LatLng location;
    private View children;
    private InfoWindow infoWindow;
    private BaiduMap baiduMap;

    public OverlayInfoWindow(Context context) {
        super(context);
    }

    public OverlayInfoWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayInfoWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayInfoWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
        infoWindow = new InfoWindow(children, location, -47);
    }

    public View getChildren() {
        return children;
    }

    public void setChildren(View children) {
        this.children = children;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        if (infoWindow == null) {
            infoWindow = new InfoWindow(children, location, -47);
        }
        this.baiduMap = baiduMap;
    }

    @Override
    public void remove() {

    }

    public void show() {
        baiduMap.showInfoWindow(infoWindow);
    }

    public void hide() {
        baiduMap.hideInfoWindow();
    }
}
