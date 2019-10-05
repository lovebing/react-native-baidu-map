/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.listener.MapListener;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayView;

import java.util.ArrayList;
import java.util.List;

public class MapViewManager extends ViewGroupManager<MapView> {

    private static Object EMPTY_OBJ = new Object();

    private List<Object> children = new ArrayList<>(10);

    @Override
    public String getName() {
        return "BaiduMapView";
    }

    @Override
    public void addView(MapView parent, View child, int index) {
        Log.i("MapViewManager", "addView:" + index);
        if (index == 0 && !children.isEmpty()) {
            for (Object item : children) {
                if (item instanceof OverlayView) {
                    ((OverlayView) child).removeFromMap(parent.getMap());
                }
            }
            children.clear();
        }
        if (child instanceof OverlayView) {
            ((OverlayView) child).addTopMap(parent.getMap());
            children.add(child);
        } else {
            children.add(EMPTY_OBJ);
        }
    }

    @Override
    public void removeViewAt(MapView parent, int index) {
        super.removeViewAt(parent, index);
        Object child = children.get(index);
        children.remove(index);
        if (child instanceof OverlayView) {
            ((OverlayView) child).removeFromMap(parent.getMap());
        }
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext themedReactContext) {
        MapView mapView =  new MapView(themedReactContext);
        BaiduMap map = mapView.getMap();
        MapListener listener = new MapListener(mapView, themedReactContext);
        map.setOnMapStatusChangeListener(listener);
        map.setOnMapLoadedCallback(listener);
        map.setOnMapClickListener(listener);
        map.setOnMapDoubleClickListener(listener);
        map.setOnMarkerClickListener(listener);
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
}
