/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.listener.MapListener;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayCluster;
import org.lovebing.reactnative.baidumap.view.OverlayView;

import java.util.ArrayList;
import java.util.List;

public class MapViewManager extends ViewGroupManager<MapView> {

    private static Object EMPTY_OBJ = new Object();

    private List<Object> children = new ArrayList<>(10);
    private MapListener mapListener;
    private int childrenCount = 0;

    @Override
    public String getName() {
        return "BaiduMapView";
    }

    @Override
    public void addView(MapView parent, View child, int index) {
        Log.i("MapViewManager", "addView:" + index);
        if (index == 0 && !children.isEmpty()) {
            removeOldChildViews(parent.getMap());
        }
        if (child instanceof OverlayView) {
            if (child instanceof OverlayCluster) {
                ((OverlayCluster) child).setMapListener(mapListener);
            }
            ((OverlayView) child).addTopMap(parent.getMap());
            children.add(child);
        } else {
            children.add(EMPTY_OBJ);
        }
    }

    @Override
    public void removeViewAt(MapView parent, int index) {
        Log.i("MapViewManager", "removeViewAt:" + index);
        if (index < children.size()) {
            Object child = children.get(index);
            children.remove(index);
            if (child instanceof OverlayView) {
                ((OverlayView) child).removeFromMap(parent.getMap());
            } else {
                super.removeViewAt(parent, index);
            }
        }
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext themedReactContext) {
        MapView mapView =  new MapView(themedReactContext);
        BaiduMap map = mapView.getMap();
        mapListener = new MapListener(mapView, themedReactContext);
        map.setOnMapStatusChangeListener(mapListener);
        map.setOnMapLoadedCallback(mapListener);
        map.setOnMapClickListener(mapListener);
        map.setOnMapDoubleClickListener(mapListener);
        map.setOnMarkerClickListener(mapListener);
        return mapView;
    }

    @ReactProp(name = "zoomControlsVisible")
    public void setZoomControlsVisible(MapView mapView, boolean zoomControlsVisible) {
        mapView.showZoomControls(zoomControlsVisible);
    }

    @ReactProp(name="trafficEnabled")
    public void setTrafficEnabled(MapView mapView, boolean trafficEnabled) {
        mapView.getMap().setTrafficEnabled(trafficEnabled);
    }

    @ReactProp(name="baiduHeatMapEnabled")
    public void setBaiduHeatMapEnabled(MapView mapView, boolean baiduHeatMapEnabled) {
        mapView.getMap().setBaiduHeatMapEnabled(baiduHeatMapEnabled);
    }

    @ReactProp(name = "mapType")
    public void setMapType(MapView mapView, int mapType) {
        mapView.getMap().setMapType(mapType);
    }

    @ReactProp(name="zoom")
    public void setZoom(MapView mapView, float zoom) {
        MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);
    }

    @ReactProp(name="zoomGesturesEnabled")
    public void setGesturesEnabled(MapView mapView, boolean zoomGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setZoomGesturesEnabled(zoomGesturesEnabled);
    }

    @ReactProp(name="scrollGesturesEnabled")
    public void setScrollEnabled(MapView mapView, boolean scrollGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setScrollGesturesEnabled(scrollGesturesEnabled);
    }

    @ReactProp(name="center")
    public void setCenter(MapView mapView, ReadableMap position) {
        if(position != null) {
            LatLng point = LatLngUtil.fromReadableMap(position);
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(point)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mapView.getMap().setMapStatus(mapStatusUpdate);
        }
    }

    @ReactProp(name = "childrenCount")
    public void setChildrenCount(MapView mapView, Integer childrenCount) {
        Log.i("MapViewManager", "childrenCount:" + childrenCount);
        this.childrenCount = childrenCount;
    }

    private void removeOldChildViews(BaiduMap baiduMap) {
        for (Object child : children) {
            if (child instanceof OverlayView) {
                ((OverlayView) child).removeFromMap(baiduMap);
            }
        }
        children.clear();
    }
}
